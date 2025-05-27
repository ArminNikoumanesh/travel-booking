package com.armin.operation.security.admin.repository.service;

import com.armin.utility.bl.NormalizeEngine;
import com.armin.utility.object.SystemError;
import com.armin.utility.object.SystemException;
import com.armin.utility.repository.orm.AbstractFilterableService;
import com.armin.utility.repository.orm.object.ReportCondition;
import com.armin.utility.repository.orm.object.ReportFilter;
import com.armin.utility.repository.orm.object.ReportOption;
import com.armin.database.user.entity.SecurityRealmEntity;
import com.armin.database.user.entity.SecurityUserRoleRealmEntity;
import com.armin.operation.security.admin.dto.realm.*;
import com.armin.operation.security.admin.repository.dao.RealmDao;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Exceptions error code range: 2301-2350
 *
 * @author unascribed
 * @author Mohammad Yasin Sadeghi
 */

@Service
public class RealmService extends AbstractFilterableService<SecurityRealmEntity, RealmFilter, RealmDao> {

    private final ModelMapper modelMapper;
    private UserRoleRealmService userRoleRealmService;

    @Autowired
    public RealmService(RealmDao realmDao, UserRoleRealmService userRoleRealmService, ModelMapper modelMapper) {
        super(realmDao);
        this.userRoleRealmService = userRoleRealmService;
        this.modelMapper = modelMapper;
    }

    public void update(SecurityRealmEntity realmEntity) {
        getDao().update(realmEntity);
    }

    public SecurityRealmEntity get(Integer id) {
        return getDao().get(id);
    }

    public void deletePersonRoleRealmByRealmId(Integer realmId) {
        getDao().deletePersonRoleRealmByRealmId(realmId);
    }

    //new methods

    public List<SecurityRealmEntity> getAllEntities(RealmPageableFilter realmOrderedFilter, String[] include) {
        return super.getAllEntities(filter(realmOrderedFilter), include);
    }

    public int countEntity(RealmFilter realmFilter) {
        return super.countEntity(filter(realmFilter));
    }

    public boolean delete(int id) throws SystemException {
        List<SecurityUserRoleRealmEntity> list = userRoleRealmService.getByRealmId(id);
        if (list != null && !list.isEmpty()) {
            throw new SystemException(SystemError.CHILD_RECORD_FOUND, "child record found", 12312);
        }
        return super.deleteById(id);
    }

    public SecurityRealmEntity getEntityById(int id, String[] includes) throws SystemException {
        return super.getEntityById(id, includes);
    }

    //// dto methods for service

    public List<RealmOut> getAll(RealmPageableFilter realmPageableFilter, String[] include) {
        List<SecurityRealmEntity> securityList = getAllEntities(realmPageableFilter, include);
        return securityList.stream()
                .map(source -> modelMapper.map(source, RealmOut.class))
                .collect(Collectors.toList());
    }

    public List<RealmInfo> getAllInfo(RealmInfoPageableFilter realmPageableFilter, String[] include) {
        List<SecurityRealmEntity> securityList = getAllEntities(filter(realmPageableFilter), include);
        return securityList.stream().map(RealmInfo::new)
                .collect(Collectors.toList());
    }

    public RealmOut getById(int id, String[] include) throws SystemException {
        return modelMapper.map(getEntityById(id, include), RealmOut.class);
    }

    public RealmOut create(RealmIn realmIn) {
        return modelMapper.map(super.createEntity(modelMapper.map(realmIn, SecurityRealmEntity.class)), RealmOut.class);
    }

    public RealmOut update(int id, RealmIn realmIn) throws SystemException {
        SecurityRealmEntity securityRealmEntity = getEntityById(id, null);
        modelMapper.map(realmIn, securityRealmEntity);
        super.updateEntity(securityRealmEntity);
        return modelMapper.map(securityRealmEntity, RealmOut.class);
    }

    public List<SecurityRealmEntity> getEntitiesByIds(List<Integer> ids) {
        return getDao().getEntitiesByIds(ids);
    }

    @Override
    public ReportFilter filter(RealmFilter filter) {
        ReportOption reportOption = new ReportOption();

        if (filter instanceof RealmPageableFilter) {
            RealmPageableFilter realmPageableFilter = (RealmPageableFilter) filter;
            reportOption.setSortOptions(realmPageableFilter.getSort());
            reportOption.setPageNumber(realmPageableFilter.getPage());
            reportOption.setPageSize(realmPageableFilter.getSize());
        }

        ReportCondition reportCondition = new ReportCondition();
        reportCondition.addEqualCondition("id", filter.getId());
        reportCondition.addLikeCondition("name", filter.getName());
//            reportCondition.addEqualCondition("type",realmFilter.getType());
        return new ReportFilter(reportCondition, reportOption);
    }

    public ReportFilter filter(RealmInfoPageableFilter filter) {
        ReportOption reportOption = new ReportOption();
        reportOption.setSortOptions(filter.getSort());
        reportOption.setPageNumber(filter.getPage());
        reportOption.setPageSize(filter.getSize());

        ReportCondition reportCondition = new ReportCondition();
        ReportCondition orCondition = new ReportCondition();

        if (filter.getKeyword() != null && NormalizeEngine.checkParsableKeyword(filter.getKeyword())) {
            orCondition.addEqualCondition("id", filter.getKeyword());
        }
        orCondition.addCaseInsensitiveLikeCondition("name", filter.getKeyword());

        reportCondition.setOrCondition(orCondition);
        reportCondition.addEqualCondition("id", filter.getId());
        return new ReportFilter(reportCondition, reportOption);
    }
}
