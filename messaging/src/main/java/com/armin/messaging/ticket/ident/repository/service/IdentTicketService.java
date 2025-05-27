package com.armin.messaging.ticket.ident.repository.service;


import com.armin.security.authentication.filter.JwtUser;
import com.armin.utility.object.UserContextDto;
import com.armin.utility.object.SystemError;
import com.armin.utility.object.SystemException;
import com.armin.utility.repository.orm.AbstractFilterableService;
import com.armin.utility.repository.orm.object.ReportCondition;
import com.armin.utility.repository.orm.object.ReportFilter;
import com.armin.utility.repository.orm.object.ReportOption;
import com.armin.database.ticket.entity.TicketEntity;
import com.armin.database.ticket.statics.TicketPriority;
import com.armin.database.ticket.statics.TicketStatus;
import com.armin.messaging.ticket.ident.model.*;
import com.armin.messaging.ticket.ident.repository.dao.IdentTicketDao;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class IdentTicketService extends AbstractFilterableService<TicketEntity, TicketFilter, IdentTicketDao> {
    private final ModelMapper modelMapper;
    private final IdentTicketMessageService identTicketMessageService;

    @Autowired
    public IdentTicketService(ModelMapper modelMapper, @Lazy IdentTicketMessageService identTicketMessageService, IdentTicketDao dao) {
        super(dao);
        this.modelMapper = modelMapper;
        this.identTicketMessageService = identTicketMessageService;
    }

    public List<TicketEntity> getAllEntities(TicketPageableFilter filter, String[] include) throws SystemException {
        UserContextDto user = JwtUser.getAuthenticatedUser();
        filter.putUserId(user.getId());
        return getAllEntities(filter(filter), include);
    }

    public List<TicketOut> getAll(TicketPageableFilter filter, String[] include) throws SystemException {
        return getAllEntities(filter, include).stream().map(TicketOut::new).collect(Collectors.toList());
    }

    public int count(TicketFilter filter) throws SystemException {
        UserContextDto user = JwtUser.getAuthenticatedUser();
        filter.putUserId(user.getId());
        return countEntity(filter(filter));
    }

    public TicketOut getById(int id, String[] include) throws SystemException {
        UserContextDto user = JwtUser.getAuthenticatedUser();
        TicketEntity entity = getDao().getByIdAndUserId(user.getId(), id, include);
        if (entity == null) {
            throw new SystemException(SystemError.DATA_NOT_FOUND, "id:" + id, 1204);
        }
        identTicketMessageService.seenTicketMessage(id);
        return new TicketOut(entity);
    }

    public TicketOut create(TicketIn ticketIn) throws SystemException {
        TicketEntity entity = modelMapper.map(ticketIn, TicketEntity.class);
        entity.setUserId(JwtUser.getAuthenticatedUser().getId());
        entity.setStatus(TicketStatus.PENDING);
        entity.setPriority(TicketPriority.NORMAL);
        createEntity(entity);
        TicketMessageOut ticketMessageOut = identTicketMessageService.create(entity.getId(), ticketIn.getMessage());
        TicketOut ticketOut = new TicketOut(entity);
        ticketOut.getMessages().add(ticketMessageOut);
        return ticketOut;
    }

    @Override
    public ReportFilter filter(TicketFilter filter) {
        ReportOption reportOption = new ReportOption();
        if (filter instanceof TicketPageableFilter) {
            TicketPageableFilter pageableFilter = (TicketPageableFilter) filter;
            reportOption.setPageNumber(pageableFilter.getPage());
            reportOption.setPageSize(pageableFilter.getSize());
            reportOption.setSortOptions(pageableFilter.getSort());
        }
        ReportCondition reportCondition = new ReportCondition();
        reportCondition.addEqualCondition("id", filter.getId());
        reportCondition.addEqualCondition("status", filter.getStatus());
        reportCondition.addLikeCondition("subject", filter.getSubject());
        reportCondition.addMinTimeCondition("created", filter.getCreatedMin());
        reportCondition.addMaxTimeCondition("created", filter.getCreatedMax());
        reportCondition.addEqualCondition("userId", filter.getUserId());
        return new ReportFilter(reportCondition, reportOption);
    }
}
