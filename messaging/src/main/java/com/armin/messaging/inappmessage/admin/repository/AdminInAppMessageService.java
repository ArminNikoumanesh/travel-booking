package com.armin.messaging.inappmessage.admin.repository;

import com.armin.database.inappmessage.entity.InAppMessageEntity;
import com.armin.database.inappmessage.repository.service.BaseInAppMessageService;
import com.armin.messaging.inappmessage.admin.model.InAppMessageFilter;
import com.armin.messaging.inappmessage.admin.model.InAppMessageIn;
import com.armin.messaging.inappmessage.admin.model.InAppMessageOut;
import com.armin.messaging.inappmessage.admin.model.InAppMessagePageableFilter;
import com.armin.utility.object.SystemException;
import com.armin.utility.repository.orm.object.ReportCondition;
import com.armin.utility.repository.orm.object.ReportFilter;
import com.armin.utility.repository.orm.object.ReportOption;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author : Armin.Nik
 * @project : shared
 * @date : 20.04.24
 */
@Service
public class AdminInAppMessageService {
    private final ModelMapper modelMapper;
    private final BaseInAppMessageService baseInAppMessageService;

    @Autowired
    public AdminInAppMessageService(ModelMapper modelMapper, BaseInAppMessageService baseInAppMessageService) {
        this.modelMapper = modelMapper;
        this.baseInAppMessageService = baseInAppMessageService;
    }

    public List<InAppMessageOut> getAll(InAppMessagePageableFilter pageableFilter, String[] include) {
        List<InAppMessageEntity> entityList = baseInAppMessageService.getAllEntities(filter(pageableFilter), null);
        return entityList.stream().map(InAppMessageOut::new).collect(Collectors.toList());
    }

    public InAppMessageOut getById(long id, String[] include) throws SystemException {
        InAppMessageEntity entity = baseInAppMessageService.getEntityById(id, include);
        return new InAppMessageOut(entity);
    }

    public int count(InAppMessageFilter filter) {
        return baseInAppMessageService.countEntity(filter(filter));
    }

    public boolean delete(long id) {
        baseInAppMessageService.deleteById(id);
        return true;
    }

    public InAppMessageOut create(InAppMessageIn model) {
        InAppMessageEntity entity = modelMapper.map(model, InAppMessageEntity.class);
        entity.setLastUpdate(LocalDateTime.now());
        entity.setCreateDate(LocalDateTime.now());
        baseInAppMessageService.createEntity(entity);
        return new InAppMessageOut(entity);
    }

    public InAppMessageOut update(long id, InAppMessageIn model) throws SystemException {
        InAppMessageEntity entity = baseInAppMessageService.getEntityById(id, null);
        modelMapper.map(model, entity);
        entity.setLastUpdate(LocalDateTime.now());
        baseInAppMessageService.updateEntity(entity);
        return new InAppMessageOut(entity);
    }

    public ReportFilter filter(InAppMessageFilter filter) {
        ReportOption reportOption = new ReportOption();
        if (filter instanceof InAppMessagePageableFilter) {
            InAppMessagePageableFilter pageableFilter = (InAppMessagePageableFilter) filter;
            reportOption.setPageNumber(pageableFilter.getPage());
            reportOption.setPageSize(pageableFilter.getSize());
            reportOption.setSortOptions(pageableFilter.getSort());
        }
        ReportCondition reportCondition = new ReportCondition();
        reportCondition.addEqualCondition("id",filter.getId());
        reportCondition.addMaxTimeCondition("expirationDate",filter.getExpirationDateMax());
        reportCondition.addMinTimeCondition("expirationDate",filter.getExpirationDateMin());
        reportCondition.addMaxTimeCondition("createDate",filter.getCreateDateMax());
        reportCondition.addMinTimeCondition("createDate",filter.getCreateDateMin());
        reportCondition.addEqualCondition("isForMain",filter.getIsForMain());
        reportCondition.addEqualCondition("stayInBox",filter.getStayInBox());
        reportCondition.addLikeCondition("subject",filter.getSubject());
        reportCondition.addLikeCondition("messageText",filter.getMessageText());
        reportCondition.addEqualCondition("inAppAction",filter.getInAppAction());
        reportCondition.addLikeCondition("url",filter.getUrl());
        reportCondition.addMaxTimeCondition("lastUpdate",filter.getLastUpdateMax());
        reportCondition.addMinTimeCondition("lastUpdate",filter.getLastUpdateMin());
        reportCondition.addEqualCondition("section",filter.getSection());
        reportCondition.addEqualCondition("userId",filter.getUserId());

        return new ReportFilter(reportCondition, reportOption);
    }
}
