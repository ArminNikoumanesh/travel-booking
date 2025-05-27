package com.armin.messaging.ticket.admin.repository;

import com.armin.security.authentication.filter.JwtUser;
import com.armin.utility.file.bl.IFileService;
import com.armin.utility.object.SystemError;
import com.armin.utility.object.SystemException;
import com.armin.utility.repository.odm.model.entity.OperationAction;
import com.armin.utility.repository.odm.statics.ElasticsearchConstant;
import com.armin.utility.repository.orm.AbstractFilterableService;
import com.armin.utility.repository.orm.Dao;
import com.armin.utility.repository.orm.object.ReportCondition;
import com.armin.utility.repository.orm.object.ReportFilter;
import com.armin.utility.repository.orm.object.ReportOption;
import com.armin.utility.statics.enums.SmsType;
import com.armin.database.ticket.entity.TicketEntity;
import com.armin.database.ticket.entity.TicketMessageEntity;
import com.armin.messaging.notification.bl.NotificationService;
import com.armin.messaging.notification.dto.Notification;
import com.armin.messaging.ticket.admin.model.*;
import com.armin.messaging.ticket.statics.TicketTemplatePath;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AdminTicketMessageService extends AbstractFilterableService<TicketMessageEntity, TicketMessageFilter, Dao<TicketMessageEntity>> {
    private final ModelMapper modelMapper;
    private final IFileService fileService;
    private final AdminTicketService adminTicketService;
 //   private final LogGeneratorService logGeneratorService;
    private final NotificationService notificationService;

    @Autowired
    public AdminTicketMessageService(ModelMapper modelMapper, IFileService fileService, AdminTicketService adminTicketService,
                                     NotificationService notificationService) {
        this.modelMapper = modelMapper;
        this.fileService = fileService;
        this.adminTicketService = adminTicketService;
 //       this.logGeneratorService = logGeneratorService;
        this.notificationService = notificationService;
    }

    public List<TicketMessageEntity> getAllEntities(TicketMessagePageableFilter pageableFilter, String[] include) {
        return getAllEntities(filter(pageableFilter), include);
    }

    public List<TicketMessageOut> getAll(TicketMessagePageableFilter pageableFilter, String[] include) {
        return getAllEntities(pageableFilter, include).stream().map(TicketMessageOut::new).collect(Collectors.toList());
    }

    public int count(TicketMessageFilter filter) {
        return countEntity(filter(filter));
    }

    public boolean delete(int id) {
        deleteById(id);
        return true;
    }

    public TicketMessageOut getById(Integer id, String[] include) throws SystemException {
        TicketMessageEntity entity = getEntityById(id, include);
        return new TicketMessageOut(entity);
    }

    @Transactional
    public TicketMessageOut create(int ticketId, TicketMessageIn ticketIn) throws SystemException {
        TicketMessageEntity entity = modelMapper.map(ticketIn, TicketMessageEntity.class);
        TicketEntity ticket = adminTicketService.getEntityById(ticketId, new String[]{"user"});
        if (ticket.getClosed() != null) {
            ticket.setClosed(null);
            adminTicketService.updateEntity(ticket);
        }
        entity.setOperatorId(JwtUser.getAuthenticatedUser().getId());
        entity.setTicketId(ticketId);
        fileService.manipulateAttachments(null, entity);
        createEntity(entity);
        return new TicketMessageOut(entity);
    }


    public TicketMessageOut editTicketMessage(int ticketId, TicketMessageEditIn editIn, String ip) throws SystemException {
        TicketMessageEntity ticketMessageEntity = getEntityById(ticketId, null);
        TicketMessageEntity oldEntity = ticketMessageEntity.cloneAttachments();
        TicketMessageEntity source;
        try {
            source = (TicketMessageEntity) ticketMessageEntity.clone();
        } catch (CloneNotSupportedException e) {
            throw new SystemException(SystemError.ILLEGAL_REQUEST, "clone is not supported", 10021);
        }
        if (ticketMessageEntity.getSeen() == null && ticketMessageEntity.getOperatorId() != null) {
            ticketMessageEntity.setBody(editIn.getBody());
            ticketMessageEntity.setAttachments(editIn.getAttachments());
            fileService.manipulateAttachments(oldEntity, ticketMessageEntity);
            TicketMessageEntity updatedEntity = updateEntity(ticketMessageEntity);
//            logGeneratorService.generateDifferentialLog(ticketId, "Ticket", OperationAction.UPDATE, source,
//                    ticketMessageEntity, ip, ElasticsearchConstant.ADMIN_OPERATION_LOG);
            return new TicketMessageOut(updatedEntity);
        } else {
            throw new SystemException(SystemError.ILLEGAL_REQUEST, "cannot edit this ticket", 2913);
        }
    }

    private void sendSms(TicketEntity entity) throws SystemException {
        TicketTemplate template = new TicketTemplate(entity.getUser().getFullName(), entity.getId());
        Notification notification;
        if (entity.getUser() != null && entity.getUser().getMobile() != null) {
            notification = new Notification.Builder(TicketTemplatePath.ANSWER_TICKET_TEMPLATE_PATH, template)
                    .setSmsConfig(entity.getUser().getMobile(), SmsType.SERVICE).build();
            notificationService.sendSms(notification);
        }
    }


    @Override
    public ReportFilter filter(TicketMessageFilter filter) {
        ReportOption reportOption = new ReportOption();
        if (filter instanceof TicketMessagePageableFilter) {
            TicketMessagePageableFilter pageableFilter = (TicketMessagePageableFilter) filter;
            reportOption.setPageNumber(pageableFilter.getPage());
            reportOption.setPageSize(pageableFilter.getSize());
            reportOption.setSortOptions(pageableFilter.getSort());
        }
        ReportCondition reportCondition = new ReportCondition();
        reportCondition.addEqualCondition("id", filter.getId());
        reportCondition.addEqualCondition("ticketId", filter.getTicketId());
        reportCondition.addEqualCondition("userId", filter.getUserId());
        reportCondition.addLikeCondition("body", filter.getBody());
        reportCondition.addMaxTimeCondition("created", filter.getCreatedMax());
        reportCondition.addMinTimeCondition("created", filter.getCreatedMin());
        return new ReportFilter(reportCondition, reportOption);
    }
}
