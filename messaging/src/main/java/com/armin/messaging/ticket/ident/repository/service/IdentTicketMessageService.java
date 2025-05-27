package com.armin.messaging.ticket.ident.repository.service;

import com.armin.security.authentication.filter.JwtUser;
import com.armin.utility.object.UserContextDto;
import com.armin.utility.file.bl.IFileService;
import com.armin.utility.object.SystemError;
import com.armin.utility.object.SystemException;
import com.armin.utility.repository.orm.AbstractFilterableService;
import com.armin.utility.repository.orm.object.ReportCondition;
import com.armin.utility.repository.orm.object.ReportFilter;
import com.armin.utility.repository.orm.object.ReportOption;
import com.armin.database.ticket.entity.TicketEntity;
import com.armin.database.ticket.entity.TicketMessageEntity;
import com.armin.database.ticket.statics.TicketStatus;
import com.armin.messaging.ticket.ident.model.TicketMessageFilter;
import com.armin.messaging.ticket.ident.model.TicketMessageIn;
import com.armin.messaging.ticket.ident.model.TicketMessageOut;
import com.armin.messaging.ticket.ident.model.TicketMessagePageableFilter;
import com.armin.messaging.ticket.ident.repository.dao.IdentTicketMessageDao;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class IdentTicketMessageService extends AbstractFilterableService<TicketMessageEntity, TicketMessageFilter, IdentTicketMessageDao> {
    private final ModelMapper modelMapper;
    private final IFileService fileService;
    private final IdentTicketService identTicketService;

    @Autowired
    public IdentTicketMessageService(ModelMapper modelMapper, IFileService fileService, IdentTicketService identTicketService, IdentTicketMessageDao identTicketMessageDao) {
        super(identTicketMessageDao);
        this.modelMapper = modelMapper;
        this.fileService = fileService;
        this.identTicketService = identTicketService;
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

    public TicketMessageOut getById(Integer id, String[] include) throws SystemException {
        TicketMessageEntity entity = getEntityById(id, include);
        return new TicketMessageOut(entity);
    }

    public TicketMessageOut create(int ticketId, TicketMessageIn ticketIn) throws SystemException {
        UserContextDto user = JwtUser.getAuthenticatedUser();
        TicketMessageEntity entity = modelMapper.map(ticketIn, TicketMessageEntity.class);
        TicketEntity ticket = identTicketService.getEntityById(ticketId, null);
        if (!Objects.equals(ticket.getUserId(), user.getId())) {
            throw new SystemException(SystemError.DATA_NOT_FOUND, "user not found", 7349);
        }

        ticket.setClosed(null);
        ticket.setStatus(TicketStatus.PENDING);
        identTicketService.updateEntity(ticket);

        entity.setUserId(user.getId());
        entity.setTicketId(ticketId);
        fileService.manipulateAttachments(null, entity);
        createEntity(entity);
        return new TicketMessageOut(entity);
    }

    public void seenTicketMessage(int ticketId) {
        this.getDao().seenTicketMessage(ticketId);
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
