package com.armin.messaging.ticket.admin.repository;

import com.armin.utility.object.SystemError;
import com.armin.utility.object.SystemException;
import com.armin.utility.repository.orm.AbstractFilterableService;
import com.armin.utility.repository.orm.Dao;
import com.armin.utility.repository.orm.object.ReportCondition;
import com.armin.utility.repository.orm.object.ReportCriteriaJoinCondition;
import com.armin.utility.repository.orm.object.ReportFilter;
import com.armin.utility.repository.orm.object.ReportOption;
import com.armin.database.ticket.entity.TicketEntity;
import com.armin.database.ticket.statics.TicketStatus;
import com.armin.messaging.ticket.admin.model.*;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.criteria.JoinType;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class AdminTicketService extends AbstractFilterableService<TicketEntity, TicketFilter, Dao<TicketEntity>> {
    private final ModelMapper modelMapper;
    private final AdminTicketMessageService adminTicketMessageService;

    @Autowired
    public AdminTicketService(ModelMapper modelMapper, @Lazy AdminTicketMessageService adminTicketMessageService) {
        this.modelMapper = modelMapper;
        this.adminTicketMessageService = adminTicketMessageService;
    }

    public List<TicketEntity> getAllEntities(TicketPageableFilter filter, String[] include) {
        return getAllEntities(filter(filter), include);
    }

    public List<TicketOut> getAll(TicketPageableFilter filter, String[] include) throws SystemException {
        return getAllEntities(filter, include).stream().map(TicketOut::new).collect(Collectors.toList());
    }

    public int count(TicketFilter filter) throws SystemException {
            return countEntity(filter(filter));
        }


    public TicketOut getById(int id, String[] include) throws SystemException {
        TicketEntity entity = getEntityById(id, include);
        return new TicketOut(entity);
    }

    @Transactional
    public TicketOut create(TicketIn ticketIn) throws SystemException {
        TicketEntity entity = modelMapper.map(ticketIn, TicketEntity.class);
        createEntity(entity);
        TicketMessageOut ticketMessageOut = adminTicketMessageService.create(entity.getId(), ticketIn.getMessage());
        TicketOut ticketOut = new TicketOut(entity);
        ticketOut.getMessages().add(ticketMessageOut);
        return ticketOut;
    }

    @Transactional
    public TicketOut update(Integer id, TicketEditIn ticketIn) throws SystemException {
        TicketEntity entity = getEntityById(id, null);
        entity.setStatus(ticketIn.getStatus());
        modelMapper.map(ticketIn, entity);
        if (ticketIn.getStatus().equals(TicketStatus.CLOSED)) {
            entity.setClosed(LocalDateTime.now());
        }
        updateEntity(entity);
        return new TicketOut(entity);
    }

    @Transactional
    public void close(Integer id) throws SystemException {
        TicketEntity entity = getEntityById(id, null);
        if (entity.getClosed() != null) {
            throw new SystemException(SystemError.ILLEGAL_REQUEST, "ticket already closed", 9131);
        }
        entity.setClosed(LocalDateTime.now());
        updateEntity(entity);
    }

    @Transactional
    public void open(Integer id) throws SystemException {
        TicketEntity entity = getEntityById(id, null);
        if (entity.getClosed() == null) {
            throw new SystemException(SystemError.ILLEGAL_REQUEST, "ticket already open", 9132);
        }
        entity.setClosed(null);
        updateEntity(entity);

    }

    @Override
    public ReportFilter filter(TicketFilter filter) {
        ReportOption reportOption = new ReportOption();
        if (filter instanceof TicketPageableFilter) {
            TicketPageableFilter pageableFilter = (TicketPageableFilter) filter;
            reportOption.setSortOptions(pageableFilter.getSort());
            reportOption.setPageSize(pageableFilter.getSize());
            reportOption.setPageNumber(pageableFilter.getPage());
            reportOption.setDistinct(true);
        }
        ReportCondition reportCondition = new ReportCondition();
        reportCondition.addEqualCondition("id", filter.getId());
        reportCondition.addEqualCondition("userId", filter.getUserId());
        reportCondition.addEqualCondition("status", filter.getStatus());
        reportCondition.addLikeCondition("subject", filter.getSubject());
        reportCondition.addEqualCondition("priority", filter.getPriority());
        reportCondition.addMaxTimeCondition("created", filter.getCreatedMax());
        reportCondition.addMinTimeCondition("created", filter.getCreatedMin());

        if (filter.getOperatorId() != null) {
            ReportCriteriaJoinCondition joinCondition = new ReportCriteriaJoinCondition("messages", JoinType.LEFT);
            joinCondition.addEqualCondition("operatorId", filter.getOperatorId());
            reportCondition.addJoinCondition(joinCondition);
        }


        return new ReportFilter(reportCondition, reportOption);
    }
}
