package com.armin.operation.security.admin.repository.service;

import com.armin.utility.object.SystemException;
import com.armin.utility.repository.orm.AbstractFilterableService;
import com.armin.utility.repository.orm.object.ReportCondition;
import com.armin.utility.repository.orm.object.ReportCriteriaJoinCondition;
import com.armin.utility.repository.orm.object.ReportFilter;
import com.armin.utility.repository.orm.object.ReportOption;
import com.armin.database.user.entity.SecurityRestEntity;
import com.armin.operation.security.admin.dto.endpoint.EndPointFilter;
import com.armin.operation.security.admin.dto.endpoint.EndPointOut;
import com.armin.operation.security.admin.dto.endpoint.EndPointPageableFilter;
import com.armin.operation.security.admin.repository.dao.RestDao;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.persistence.criteria.JoinType;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Exceptions error code range: 2251-2300
 *
 * @author unascribed
 * @author Mohammad Yasin Sadeghi
 */

@Service
public class RestService extends AbstractFilterableService<SecurityRestEntity, EndPointFilter, RestDao> {
    private final ModelMapper modelMapper;

    @Autowired
    public RestService(RestDao restDao, ModelMapper modelMapper) {
        super(restDao);
        this.modelMapper = modelMapper;
    }

    public SecurityRestEntity getEntityById(Integer id, String[] include) throws SystemException {
        return super.getEntityById(id, include);
    }

    public List<SecurityRestEntity> getAllEntities(EndPointPageableFilter endPointPageableFilter, String[] include) {
        return super.getAllEntities(filter(endPointPageableFilter), include);
    }

    public int countEntity(EndPointFilter endPointFilter) {
        return super.countEntity(filter(endPointFilter));
    }

    boolean delete(int id) {
        return super.deleteById(id);
    }


    // dto methods service
    public List<EndPointOut> getAll(EndPointPageableFilter endPointPageableFilter, String[] include) {
        List<SecurityRestEntity> securityList = getAllEntities(endPointPageableFilter, include);
        return securityList.stream()
                .map(source -> modelMapper.map(source, EndPointOut.class))
                .collect(Collectors.toList());
    }

    public EndPointOut getById(int id, String[] include) throws SystemException {
        return modelMapper.map(getEntityById(id, include), EndPointOut.class);
    }

    @Override
    public ReportFilter filter(EndPointFilter filter) {
        ReportOption reportOption = new ReportOption();

        if (filter instanceof EndPointPageableFilter) {
            EndPointPageableFilter endPointPageableFilter = (EndPointPageableFilter) filter;
            reportOption.setPageSize(endPointPageableFilter.getSize());
            reportOption.setPageNumber(endPointPageableFilter.getPage());
            reportOption.setSortOptions(endPointPageableFilter.getSort());
        }

        ReportCondition reportCondition = new ReportCondition();
        reportCondition.addEqualCondition("id", filter.getId());
        reportCondition.addLikeCondition("url", filter.getUrl());
        reportCondition.addEqualCondition("httpMethod", filter.getHttpMethod());
        if (filter.getPermissionId() != null) {
            ReportCriteriaJoinCondition joinCondition = new ReportCriteriaJoinCondition("permissions", JoinType.LEFT);
            joinCondition.addEqualCondition("id", filter.getPermissionId());
            reportCondition.addJoinCondition(joinCondition);
        }

        return new ReportFilter(reportCondition, reportOption);
    }

    public List<SecurityRestEntity> listByIds(List<Integer> ids) {
        return getDao().listByIds(ids);
    }

}
