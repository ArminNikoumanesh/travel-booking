package com.armin.operation.security.admin.repository.service;

import com.armin.utility.object.SystemException;
import com.armin.utility.repository.orm.AbstractFilterableService;
import com.armin.utility.repository.orm.object.ReportCondition;
import com.armin.utility.repository.orm.object.ReportCriteriaJoinCondition;
import com.armin.utility.repository.orm.object.ReportFilter;
import com.armin.utility.repository.orm.object.ReportOption;
import com.armin.database.user.entity.SecurityPermissionEntity;
import com.armin.database.user.entity.SecurityRestEntity;
import com.armin.database.user.statics.PermissionType;
import com.armin.operation.security.admin.dto.permission.*;
import com.armin.operation.security.admin.repository.dao.PermissionDao;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.persistence.criteria.JoinType;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Exceptions error code range: 2351-2400
 *
 * @author unascribed
 * @author Mohammad Yasin Sadeghi
 */

@Service
public class PermissionService extends AbstractFilterableService<SecurityPermissionEntity, PermissionFilter, PermissionDao> {
    private final ModelMapper modelMapper;
    private final RestService restService;

    @Autowired
    public PermissionService(PermissionDao permissionDao, ModelMapper modelMapper, RestService restService) {
        super(permissionDao);
        this.modelMapper = modelMapper;
        this.restService = restService;
    }

    public void update(SecurityPermissionEntity permissionEntity) {
        getDao().update(permissionEntity);
    }

    public SecurityPermissionEntity get(Integer id) {
        return getDao().get(id);
    }

    public List<SecurityPermissionEntity> getAllEntities(PermissionPageableFilter permissionPagableFilter, String[] include) {
        return super.getAllEntities(filter(permissionPagableFilter), include);
    }

    public int countAdmin(PermissionFilter permissionFilter) {
        permissionFilter.setType(PermissionType.ADMIN);
        return super.countEntity(filter(permissionFilter));
    }

    //
    public boolean deleteEntity(int id) {
        return super.deleteById(id);
    }

    //dto methods
    public List<PermissionOut> getAll(PermissionPageableFilter permissionPageableFilter, String[] includes) {
        List<SecurityPermissionEntity> securityList = getAllEntities(permissionPageableFilter, includes);
        return securityList.stream()
                .map(source -> modelMapper.map(source, PermissionOut.class))
                .collect(Collectors.toList());
    }

    public List<PermissionOut> getAllAdmin(PermissionFilter permissionFilter, String[] includes) {
        permissionFilter.setType(PermissionType.ADMIN);
        List<SecurityPermissionEntity> securityList = getAllEntitiesWithoutMaxLimit(filter(permissionFilter), includes);
        return securityList.stream()
                .map(source -> modelMapper.map(source, PermissionOut.class))
                .collect(Collectors.toList());
    }


    public List<PermissionInfo> getAllInfo(PermissionPageableFilter permissionPageableFilter, String[] includes) {
        List<SecurityPermissionEntity> securityList = getAllEntities(permissionPageableFilter, includes);
        return securityList.stream()
                .map(PermissionInfo::new)
                .collect(Collectors.toList());
    }

    public List<PermissionInfo> getAllInfoAdmin(PermissionPageableFilter permissionPageableFilter, String[] includes) {
        permissionPageableFilter.setType(PermissionType.ADMIN);
        List<SecurityPermissionEntity> securityList = getAllEntities(permissionPageableFilter, includes);
        return securityList.stream()
                .map(PermissionInfo::new)
                .collect(Collectors.toList());
    }


    public Collection<SecurityPermissionEntity> listByIds(List<Integer> ids) {
        return getDao().listByIds(ids);
    }

    public PermissionOut create(PermissionIn permissionIn, String ip) throws SystemException {
        List<Integer> ids = new ArrayList<>(Arrays.asList(permissionIn.getEndPointIds()));
        Collection<SecurityRestEntity> securityRestEntities = restService.listByIds(ids);
        SecurityPermissionEntity securityPermissionEntity = new SecurityPermissionEntity();
        securityPermissionEntity.setName(permissionIn.getName());
        securityPermissionEntity.setTraversal(permissionIn.getTraversal());
        securityPermissionEntity.setNodeType(permissionIn.getNodeType());
        securityPermissionEntity.setParentIdFk(permissionIn.getParentIdFk());
        securityPermissionEntity.setRests(new HashSet<>(securityRestEntities));
        super.createEntity(securityPermissionEntity);
        SecurityPermissionEntity source = new SecurityPermissionEntity();
        source.setRests(new HashSet<>());
        return modelMapper.map(securityPermissionEntity, PermissionOut.class);
    }

    public PermissionOut update(int id, PermissionIn permissionIn, String ip) throws SystemException {
        SecurityPermissionEntity securityPermissionEntity = getEntityById(id, new String[]{"rests"});
        List<Integer> ids = new ArrayList<>(Arrays.asList(permissionIn.getEndPointIds()));
        Collection<SecurityRestEntity> securityRestEntities = restService.listByIds(ids);
        securityPermissionEntity.setRests(new HashSet<>(securityRestEntities));
        modelMapper.map(permissionIn, securityPermissionEntity);
        super.updateEntity(securityPermissionEntity);
        return modelMapper.map(securityPermissionEntity, PermissionOut.class);
    }

    public boolean delete(int id) {
        return deleteEntity(id);
    }

    public PermissionOut getById(int id, String[] include) throws SystemException {
        return modelMapper.map(getEntityById(id, include), PermissionOut.class);
    }

    @Override
    public ReportFilter filter(PermissionFilter filter) {

        ReportOption reportOption = new ReportOption();

        if (filter instanceof PermissionPageableFilter) {
            PermissionPageableFilter permissionPageableFilter = (PermissionPageableFilter) filter;
            reportOption.setPageSize(permissionPageableFilter.getSize());
            reportOption.setPageNumber(permissionPageableFilter.getPage());
            reportOption.setSortOptions(permissionPageableFilter.getSort());
        }

        ReportCondition reportCondition = new ReportCondition();
        reportCondition.addMinNumberCondition("id", 0);
        reportCondition.addEqualCondition("id", filter.getId());
        reportCondition.addLikeCondition("name", filter.getName());
        reportCondition.addEqualCondition("nodeType", filter.getNodeType());
        reportCondition.addEqualCondition("traversal", filter.getTraversal());
        reportCondition.addEqualCondition("parentIdFk", filter.getParentIdFk());
        reportCondition.addEqualCondition("type", filter.getType());
        if (filter.getEndpointId() != null) {
            ReportCriteriaJoinCondition joinCondition = new ReportCriteriaJoinCondition("restEntities", JoinType.LEFT);
            joinCondition.addEqualCondition("id", filter.getEndpointId());
            reportCondition.addJoinCondition(joinCondition);
        }
        if (filter.getRoleId() != null) {
            ReportCriteriaJoinCondition joinCondition = new ReportCriteriaJoinCondition("roleEntities", JoinType.LEFT);
            joinCondition.addEqualCondition("id", filter.getRoleId());
            reportCondition.addJoinCondition(joinCondition);
        }

        return new ReportFilter(reportCondition, reportOption);
    }
}
