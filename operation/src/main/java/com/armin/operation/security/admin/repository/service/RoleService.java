package com.armin.operation.security.admin.repository.service;

import com.armin.database.user.entity.SecurityPermissionEntity;
import com.armin.database.user.entity.SecurityRoleEntity;
import com.armin.database.user.entity.SecurityUserRoleRealmEntity;
import com.armin.database.user.statics.PermissionType;
import com.armin.database.user.statics.RoleType;
import com.armin.operation.security.admin.dto.role.*;
import com.armin.operation.security.admin.repository.dao.RoleDao;
import com.armin.utility.bl.NormalizeEngine;
import com.armin.utility.object.SystemError;
import com.armin.utility.object.SystemException;
import com.armin.utility.repository.orm.AbstractFilterableService;
import com.armin.utility.repository.orm.object.ReportCondition;
import com.armin.utility.repository.orm.object.ReportCriteriaJoinCondition;
import com.armin.utility.repository.orm.object.ReportFilter;
import com.armin.utility.repository.orm.object.ReportOption;
import org.hibernate.Hibernate;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import jakarta.persistence.criteria.JoinType;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Exceptions error code range: 2151-2200
 *
 * @author unascribed
 * @author Mohammad Yasin Sadeghi
 */

@Service
public class RoleService extends AbstractFilterableService<SecurityRoleEntity, RoleFilter, RoleDao> {

    private final ModelMapper modelMapper;
    private final UserRoleRealmService userRoleRealmService;
    private final PermissionService permissionService;


    @Autowired
    public RoleService(RoleDao roleDao, UserRoleRealmService userRoleRealmService, ModelMapper modelMapper, PermissionService permissionService) {
        super(roleDao);
        this.userRoleRealmService = userRoleRealmService;
        this.modelMapper = modelMapper;
        this.permissionService = permissionService;
    }

    public SecurityRoleEntity getRole(Integer id) {
        return getDao().getRole(id);
    }

    public List<SecurityRoleEntity> listByCategory(Integer category) {
        return getDao().listByCategory(category);
    }

    public SecurityRoleEntity getRoleWithPermissions(Integer id) {
        return getDao().getWithPermissions(id);
    }

    public List<SecurityRoleEntity> getAllEntities(RolePageableFilter rolePagableFilter, String[] include) {
        return super.getAllEntities(filter(rolePagableFilter), include);
    }

    public int countEntity(RoleFilter roleFilter) {
        return super.countEntity(filter(roleFilter));

    }

    public boolean delete(int id, String ip, RoleType type) throws SystemException {
        if (id == -1 || id == -3) {
            throw new SystemException(SystemError.ILLEGAL_REQUEST, "Super admin can not be updated", 2151);
        }
        SecurityRoleEntity roleEntity = getEntityById(id, null);
        if (!type.equals(roleEntity.getType())) {
            throw new SystemException(SystemError.LOGICAL_UN_AUTHORIZED, "Incompatible Roles", 1163);
        }
        try {
            return super.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            throw new SystemException(SystemError.LOGICAL_UN_AUTHORIZED, "There are some users has this role", 1153);
        }
    }

    public UserInRole addToRole(int userId, int roleId, Integer realmId) {
        SecurityUserRoleRealmEntity roleRealmEntity = new SecurityUserRoleRealmEntity();
        roleRealmEntity.setUserId(userId);
        roleRealmEntity.setRoleId(roleId);
        roleRealmEntity.setRealmId(realmId);
        return new UserInRole(roleRealmEntity.getUserId(), roleRealmEntity.getRoleId(), roleRealmEntity.getRealmId());
    }

    public void deleteByIdList(List<Integer> ids) {
        userRoleRealmService.deleteByIdList(ids);
    }

    public void removeFromRoles(int userId, int[] roleIds, Integer realmId) {
        for (int roleId : roleIds) {
            removeFromRole(userId, roleId, realmId);
        }

    }

    public void removeFromRole(int userId, int roleId, Integer realmId) {
        userRoleRealmService.delete(roleId, realmId, userId);
    }

    public SecurityRoleEntity getRoleById(int id, String[] include) throws SystemException {
        SecurityRoleEntity role = super.getEntityById(id, include);
        if (role.isShow()) {
            return role;
        }
        throw new SystemException(SystemError.DATA_NOT_FOUND, "id:" + id, 1204);
    }

    // role dto methods for services

    public List<RoleOut> getAll(RolePageableFilter rolePageableFilter) {
        String[] includePermissions = {"permissions"};
        List<SecurityRoleEntity> securityList = getAllEntities(rolePageableFilter, includePermissions);
        List<RoleOut> roleOuts = new ArrayList<>();
        List<Integer> permissionsIds = new ArrayList<>();
        for (SecurityRoleEntity entity : securityList) {
            RoleOut roleOut = new RoleOut();
            roleOut.setId(entity.getId());
            roleOut.setName(entity.getName());
            for (SecurityPermissionEntity permissionEntity : entity.getPermissions()) {
                permissionsIds.add(permissionEntity.getId());
            }
            Integer[] permissionIdBare = new Integer[permissionsIds.size()];

            roleOut.setPermissionIds(permissionsIds.toArray(permissionIdBare));
            roleOuts.add(roleOut);
        }
        return roleOuts;
    }

    public RoleOut getById(int id, String[] include) throws SystemException {
        SecurityRoleEntity roleEntity = getRoleById(id, include);
        RoleOut roleOut = new RoleOut();
        if (roleEntity != null) {
            roleOut.setName(roleEntity.getName());
            roleOut.setId(roleEntity.getId());
            if (Hibernate.isInitialized(roleEntity.getPermissions())) {
                List<Integer> permissionIds = new ArrayList<>();
                for (SecurityPermissionEntity permissionEntity : roleEntity.getPermissions()) {
                    permissionIds.add(permissionEntity.getId());
                }
                Integer[] permissionIdBare = new Integer[permissionIds.size()];
                roleOut.setPermissionIds(permissionIds.toArray(permissionIdBare));
            }
            return roleOut;
        } else {
            return roleOut;
        }
    }


    public RoleOut create(RoleIn roleIn, String ip, PermissionType type, RoleType roleType) throws SystemException {
        Collection<SecurityPermissionEntity> securityPermissionList = validatePermissions(roleIn, type);
        return create(roleIn, ip, securityPermissionList, roleType);
    }

    private Collection<SecurityPermissionEntity> validatePermissions(RoleIn roleIn, PermissionType type) throws SystemException {
        List<Integer> ids = new ArrayList<>(Arrays.asList(roleIn.getPermissionIds()));
        Collection<SecurityPermissionEntity> securityPermissionList = permissionService.listByIds(ids);
        for (SecurityPermissionEntity eachPermission : securityPermissionList) {
            if (!eachPermission.getType().equals(type)) {
                throw new SystemException(SystemError.ILLEGAL_REQUEST, "wrong permission type:" + type, 8501);
            }
        }
        return securityPermissionList;
    }

    private RoleOut create(RoleIn roleIn, String ip, Collection<SecurityPermissionEntity> securityPermissionList, RoleType roleType) throws SystemException {
        SecurityRoleEntity securityRoleEntity = new SecurityRoleEntity();
        securityRoleEntity.setName(roleIn.getName());
        securityRoleEntity.setType(roleType);
        securityRoleEntity.setPermissions(new HashSet<>(securityPermissionList));
        securityRoleEntity.setShow(true);
        super.createEntity(securityRoleEntity);
        SecurityRoleEntity source = new SecurityRoleEntity();
        source.setPermissions(new HashSet<>());
        return modelMapper.map(securityRoleEntity, RoleOut.class);
    }

    public RoleOut update(int id, RoleIn model, String ip, PermissionType type) throws SystemException {
        Collection<SecurityPermissionEntity> securityPermissionList = validatePermissions(model, type);
        return update(id, model, ip, securityPermissionList);
    }

    public RoleOut update(int id, RoleIn model, String ip, Collection<SecurityPermissionEntity> securityPermissionList) throws SystemException {
        if (id > 0) {
            SecurityRoleEntity securityRoleEntity = getRoleById(id, new String[]{"permissions"});
            securityRoleEntity.setPermissions(new HashSet<>(securityPermissionList));
            modelMapper.map(model, securityRoleEntity);
            securityRoleEntity.setShow(true);
            super.updateEntity(securityRoleEntity);
            return modelMapper.map(securityRoleEntity, RoleOut.class);
        } else {
            throw new SystemException(SystemError.LOGICAL_UN_AUTHORIZED, "Super admin can not be updated", 2152);
        }
    }

    public List<RoleInfo> getAllInfo(RoleInfoPageableFilter pageableFilter, String[] include) {
        List<SecurityRoleEntity> entities = getAllEntities(filter(pageableFilter), include);
        return entities.stream().map(RoleInfo::new).collect(Collectors.toList());
    }


    @Override
    public ReportFilter filter(RoleFilter filter) {

        ReportOption reportOption = new ReportOption();

        if (filter instanceof RolePageableFilter rolePageableFilter) {
            reportOption.setSortOptions(rolePageableFilter.getSort());
            reportOption.setPageNumber(rolePageableFilter.getPage());
            reportOption.setPageSize(rolePageableFilter.getSize());
        }
        ReportCondition reportCondition = new ReportCondition();
        reportCondition.addEqualCondition("id", filter.getId());
        reportCondition.addLikeCondition("name", filter.getName());
        reportCondition.addEqualCondition("show", true);
        reportCondition.addEqualCondition("type", filter.getType());
        if (filter.getPermissionId() != null) {
            ReportCriteriaJoinCondition joinCondition = new ReportCriteriaJoinCondition("permissions", JoinType.LEFT);
            joinCondition.addEqualCondition("id", filter.getPermissionId());
            reportCondition.addJoinCondition(joinCondition);
        }
        return new ReportFilter(reportCondition, reportOption);
    }

    public ReportFilter filter(RoleInfoPageableFilter filter) {
        ReportOption reportOption = new ReportOption();
        reportOption.setSortOptions(filter.getSort());
        reportOption.setPageNumber(filter.getPage());
        reportOption.setPageSize(filter.getSize());

        ReportCondition reportCondition = new ReportCondition();
        ReportCondition orCondition = new ReportCondition();

        if (filter.getKeyword() != null && !NormalizeEngine.checkParsableKeyword(filter.getKeyword())) {
            orCondition.addCaseInsensitiveLikeCondition("name", filter.getKeyword());
        }else{
            orCondition.addEqualCondition("id", filter.getKeyword());
        }

        reportCondition.setOrCondition(orCondition);
        reportCondition.addEqualCondition("id", filter.getId());
        reportCondition.addEqualCondition("show", true);
        reportCondition.addEqualCondition("type", filter.getType());
        return new ReportFilter(reportCondition, reportOption);
    }
}
