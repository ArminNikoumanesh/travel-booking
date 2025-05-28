package com.armin.operation.admin.repository.service;

import com.armin.utility.bl.HashService;
import com.armin.utility.config.cloud.BaseApplicationProperties;
import com.armin.utility.file.bl.IFileService;
import com.armin.utility.object.SystemError;
import com.armin.utility.object.SystemException;
import com.armin.utility.repository.orm.AbstractFilterableService;
import com.armin.utility.repository.orm.object.ReportCondition;
import com.armin.utility.repository.orm.object.ReportCriteriaJoinCondition;
import com.armin.utility.repository.orm.object.ReportFilter;
import com.armin.utility.repository.orm.object.ReportOption;
import com.armin.database.user.entity.ProfileEntity;
import com.armin.database.user.entity.SecurityRoleEntity;
import com.armin.database.user.entity.SecurityUserRoleRealmEntity;
import com.armin.database.user.entity.UserEntity;
import com.armin.database.user.statics.RoleType;
import com.armin.database.user.statics.UserMedium;
import com.armin.operation.admin.model.dto.common.UserManageOut;
import com.armin.operation.admin.model.dto.common.UserProfileInfoOut;
import com.armin.operation.admin.model.dto.common.UserRoleOut;
import com.armin.operation.admin.model.dto.user.*;
import com.armin.operation.security.admin.repository.dao.UserDao;
import com.armin.operation.security.admin.repository.service.RoleService;
import com.armin.operation.security.admin.repository.service.UserSessionService;
import org.apache.commons.lang3.EnumUtils;
import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.persistence.criteria.JoinType;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static com.armin.utility.bl.NormalizeEngine.checkParsableKeyword;

/**
 * The User Service Class,
 * Containing Methods about User Management
 * <p>
 * Exceptions error code range: 3101-3150
 *
 */

@Service
public class UserService extends AbstractFilterableService<UserEntity, UserFilter, UserDao> {
    private final ModelMapper modelMapper;
    private final HashService hashService;
    private final IFileService fileService;
    private final UserSessionService userSessionService;
    private final BaseApplicationProperties applicationProperties;
    private final RoleService roleService;

    @Autowired
    public UserService(UserDao userDao, ModelMapper modelMapper, HashService hashService, IFileService fileService, UserSessionService userSessionService, BaseApplicationProperties applicationProperties, BaseApplicationProperties applicationProperties1, RoleService roleService) {
        super(userDao);
        this.modelMapper = modelMapper;
        this.hashService = hashService;
        this.fileService = fileService;
        this.userSessionService = userSessionService;
        this.applicationProperties = applicationProperties1;
        this.roleService = roleService;
    }

    public Collection<UserManageOut> getAll(UserPageableFilter filter, String[] include) {
        List<UserEntity> persons = getAllEntities(filter(filter), include);
        List<UserManageOut> result = new ArrayList<>();
        if (persons != null) {
            for (UserEntity eachPerson : persons) {
                result.add(new UserManageOut(eachPerson));
            }
        }
        return result;
    }

    public UserManageOut getById(int id, String[] include) throws SystemException {
        UserEntity userEntity = getEntityById(id, include);
        return new UserManageOut(userEntity);
    }

    public UserRoleOut getAdminUserInfoWithRoles(int id) throws SystemException {
        String[] include = {"roleRealms.role"};
        UserEntity userEntity = getEntityById(id, include);
        return new UserRoleOut(userEntity);
    }

    public UserEditOut create(UserEditIn model) throws SystemException {
        UserEntity userEntity = getEntityByMobileEmail(model.getUser().getMobile(), model.getUser().getEmail());
        if (userEntity != null) {
            throw new SystemException(SystemError.USERNAME_ALREADY_EXIST, "mobile:" + model.getUser().getMobile() + ",email:" + model.getUser().getEmail(), 3101);
        }
        if (applicationProperties.getIdentitySettings().getRegistration().isShowEconomicCode() && applicationProperties.getIdentitySettings().getRegistration().isRequireEconomicCode() && model.getUser().getLegal() && model.getProfile().getEconomicCode() == null) {
            throw new SystemException(SystemError.VALIDATION_EXCEPTION, "economic code is required", 123);
        }

        if (StringUtils.isNotBlank(model.getUser().getNationalId())) {
            userEntity = getEntityByNationalId(model.getUser().getNationalId());
            if (userEntity != null) {
                throw new SystemException(SystemError.DUPLICATE_REQUEST, "duplicate national id", 3678);
            }
        }

        userEntity = modelMapper.map(model, UserEntity.class);
        modelMapper.map(model.getUser(), userEntity);
        userEntity.setFullName();
        userEntity.setHashedPassword(hashService.hash(model.getPassword()));
        userEntity.setCreated(LocalDateTime.now());
        userEntity.setAccessFailedCount(0);
        userEntity.setMedium(UserMedium.ADMIN);
        ProfileEntity profileEntity = modelMapper.map(model.getProfile(), ProfileEntity.class);
        fileService.manipulateAttachments(null, profileEntity);

        userEntity.setProfile(profileEntity);
        profileEntity.setUserEntity(userEntity);
        createEntity(userEntity);

        UserEditOut userEditOut = modelMapper.map(userEntity, UserEditOut.class);
        modelMapper.map(userEntity, userEditOut.getUser());
        modelMapper.map(profileEntity, userEditOut.getProfile());
        return userEditOut;
    }

    public UserEditOut update(int id, UserEditIn model) throws SystemException {
        if (applicationProperties.getIdentitySettings().getProfile().isShowEconomicCode() && applicationProperties.getIdentitySettings().getProfile().isRequireEconomicCode() && model.getUser().getLegal() && model.getProfile().getEconomicCode() == null) {
            throw new SystemException(SystemError.VALIDATION_EXCEPTION, "economic code is required", 123);
        }
        if (model.getPassword() != null || model.getPasswordConfirm() != null) {
            if (model.getPassword().length() > applicationProperties.getIdentitySettings().getPassword().getMaxLength() ||
                    model.getPasswordConfirm().length() > applicationProperties.getIdentitySettings().getPassword().getMaxLength() ||
                    model.getPassword().length() < applicationProperties.getIdentitySettings().getPassword().getRequiredLength() ||
                    model.getPasswordConfirm().length() < applicationProperties.getIdentitySettings().getPassword().getRequiredLength()) {
                throw new SystemException(SystemError.ILLEGAL_REQUEST, "password length problem", 3012);
            }
        }

        UserEntity userEntity = getEntityByMobileEmail(model.getUser().getMobile(), model.getUser().getEmail(), id);
        if (userEntity != null) {
            throw new SystemException(SystemError.USERNAME_ALREADY_EXIST, "mobile:" + model.getUser().getMobile() + ",email:" + model.getUser().getEmail(), 3101);
        }
        if (StringUtils.isNotBlank(model.getUser().getNationalId())) {
            userEntity = getEntityByNationalId(model.getUser().getNationalId());
            if (userEntity != null && userEntity.getId() != id) {
                throw new SystemException(SystemError.DUPLICATE_REQUEST, "duplicate national id", 3678);
            }
        }

        String[] include = {"profile"};
        userEntity = getEntityById(id, include);
        ProfileEntity oldModel = userEntity.getProfile().cloneImages();
        setUserFieldsOnUpdate(userEntity, model);
        if (model.getPassword() != null) {
            userEntity.setHashedPassword(hashService.hash(model.getPassword()));
            userSessionService.deleteAllByUserId(id);
        }

        fileService.manipulateAttachments(oldModel, userEntity.getProfile());
        updateEntity(userEntity);
        return new UserEditOut(userEntity);
    }

    public UserEditOut updateUserRoleRealms(int id, List<RoleRealmIn> model, RoleType roleType, String ip) throws SystemException {
        String[] include = {"profile", "roleRealms.role"};
        UserEntity userEntity = getEntityById(id, include);
        setUserRoles(userEntity, model, roleType);
        mergeEntity(userEntity);
        return new UserEditOut(userEntity);
    }

    public boolean confirmEmail(Integer userId, UserConfirmEmailIn model) throws SystemException {
        UserEntity userEntity = getEntityById(userId, null);
        if (StringUtils.isBlank(userEntity.getEmail()) && model.isEmailConfirmed()) {
            throw new SystemException(SystemError.ILLEGAL_ARGUMENT, "id:" + userId, 3965);
        }
        userEntity.setEmailConfirmed(model.isEmailConfirmed());
        updateEntity(userEntity);
        return true;
    }

    public boolean confirmMobile(Integer userId, UserConfirmMobileIn model) throws SystemException {
        UserEntity userEntity = getEntityById(userId, null);
        if (StringUtils.isBlank(userEntity.getMobile()) && model.isMobileConfirmed()) {
            throw new SystemException(SystemError.ILLEGAL_ARGUMENT, "id:" + userId, 3967);
        }
        userEntity.setMobileConfirmed(model.isMobileConfirmed());
        updateEntity(userEntity);
        return true;
    }

    private void setUserFieldsOnUpdate(UserEntity userEntity, UserEditIn model) {
        userEntity.setTwoFactorEnabled(model.getTwoFactorEnabled());
        userEntity.getProfile().setImage(model.getProfile().getImage());
        userEntity.setMobile(model.getUser().getMobile());
        userEntity.setEmail(model.getUser().getEmail());
        userEntity.setFirstName(model.getUser().getFirstName());
        userEntity.setLastName(model.getUser().getLastName());
        userEntity.setNationalId(model.getUser().getNationalId());
        userEntity.setLegalName(model.getUser().getLegalName());
        userEntity.setLegal(model.getUser().getLegal());
        userEntity.setFullName();
        userEntity.getProfile().setEconomicCode(model.getProfile().getEconomicCode());
        userEntity.getProfile().setGender(model.getProfile().getGender());
        userEntity.getProfile().setBirthDate(model.getProfile().getBirthDate());
    }

    private void setUserRoles(UserEntity userEntity, List<RoleRealmIn> roleRealmIns, RoleType roleType) throws SystemException {
        for (Iterator<SecurityUserRoleRealmEntity> existRolesIterator = userEntity.getRoleRealms().iterator(); existRolesIterator.hasNext(); ) {
            boolean notFound = true;
            SecurityUserRoleRealmEntity eachRoleRealm = existRolesIterator.next();
            if (EnumUtils.isValidEnum(RoleType.class, eachRoleRealm.getRole().getType().name())) {
                for (Iterator<RoleRealmIn> iterator = roleRealmIns.iterator(); iterator.hasNext(); ) {
                    RoleRealmIn eachInput = iterator.next();
                    if (eachRoleRealm.getRoleId() == eachInput.getRoleId() &&
                            eachRoleRealm.getRealmId() == eachInput.getRealmId()) {
                        notFound = false;
                        iterator.remove();
                        break;
                    }
                }

            } else {
                notFound = false;
            }
            if (notFound) {
                eachRoleRealm.setUserEntity(null);
                existRolesIterator.remove();
            }
        }


        List<SecurityRoleEntity> roles = roleService.getEntitiesByIds(roleRealmIns.stream().map(RoleRealmIn::getRoleId).collect(Collectors.toList()));
        for (SecurityRoleEntity eachRole : roles) {
            if (!EnumUtils.isValidEnum(RoleType.class, eachRole.getType().name())) {
                throw new SystemException(SystemError.LOGICAL_UN_AUTHORIZED, "invalid role type", 3105);
            }
        }
        for (RoleRealmIn roleRealmIn : roleRealmIns) {
            SecurityUserRoleRealmEntity userRoleRealmEntity = new SecurityUserRoleRealmEntity();
            userRoleRealmEntity.setUserEntity(userEntity);
            userRoleRealmEntity.setRoleId(roleRealmIn.getRoleId());
            Optional<SecurityRoleEntity> roleEntity = roles
                    .stream()
                    .filter(securityRoleEntity -> securityRoleEntity.getId() == roleRealmIn.getRoleId())
                    .findFirst();
            roleEntity.ifPresent(userRoleRealmEntity::setRole);
            userRoleRealmEntity.setRealmId(roleRealmIn.getRealmId());
            userEntity.getRoleRealms().add(userRoleRealmEntity);
        }
    }

    public Collection<UserProfileInfoOut> getAllInfo(UserPageableFilter filter, String[] include) {
        List<UserEntity> persons = getAllEntities(filter(filter), include);
        List<UserProfileInfoOut> result = new ArrayList<>();
        for (UserEntity eachPerson : persons) {
            result.add(new UserProfileInfoOut(eachPerson));
        }
        return result;
    }

    public UserProfileInfoOut getInfoById(int id, String[] include) throws SystemException {
        UserEntity userEntity = getEntityById(id, include);
        return new UserProfileInfoOut(userEntity);
    }

    public int count(UserFilter filter) {
        return countEntity(filter(filter));
    }

    @Override
    public UserEntity getEntityById(int id, String[] include) throws SystemException {
        UserEntity userEntity = getDao().get(id, include);
        if (userEntity == null) {
            throw new SystemException(SystemError.USER_NOT_FOUND, "id:" + id, 3102);
        }
        return userEntity;
    }

    public UserEntity getEntityByUsername(String username) {
        return getDao().getByUsername(username);
    }

    public UserEntity getEntityByNationalId(String nationalId) {
        return getDao().getByNationalId(nationalId);
    }

    public UserEntity getEntityByMobileEmail(String mobile, String email) {
        return getDao().getByUsername(mobile, email);
    }

    public UserEntity getEntityByMobileEmail(String mobile, String email, int id) {
        return getDao().getByUsername(mobile, email, id);
    }

    public UserEntity getEntityByMobileEmailWithProfile(String mobile, String email) {
        return getDao().getByUsernameWithProfile(mobile, email);
    }

    public boolean delete(int id) throws SystemException {
        if (id != -1) {
            UserEntity userEntity = getEntityById(id, null);
            userEntity.setDeleted(LocalDateTime.now());
            updateEntity(userEntity);
            return true;
        } else {
            return false;
        }
    }

    public void enableTwoFactorLogin(int id) {
        this.getDao().changeTwoFactorLogin(id, true);
    }

    public void disableTwoFactorLogin(int id) {
        this.getDao().changeTwoFactorLogin(id, false);
    }

    public void suspend(int id) throws SystemException {
        this.getDao().changeSuspend(id, true);
        userSessionService.deleteAllByUserId(id);
    }

    public void unsuspend(int id) {
        this.getDao().changeSuspend(id, false);
    }

    public void unlock(int id) {
        this.getDao().changeLockStatus(id, null);
    }

    public void updateAccessFailedCount(int id) {
        this.getDao().updateAccessFailedCount(id);
    }

    public UserEntity getEntityByDeviceId(String deviceId) {
        return getDao().getByDeviceId(deviceId);
    }

    public UserEntity getByUserId(int id) {
        return getDao().getByUserId(id);
    }

    @Override
    public ReportFilter filter(UserFilter filter) {
        boolean profileJoin = false;
        ReportOption reportOption = new ReportOption();
        if (filter instanceof UserPageableFilter) {
            UserPageableFilter userPageableFilter = (UserPageableFilter) filter;
            reportOption.setPageNumber(userPageableFilter.getPage());
            reportOption.setPageSize(userPageableFilter.getSize());
            reportOption.setSortOptions(userPageableFilter.getSort());
            reportOption.setExport(filter.isExport());
        }
        reportOption.setDistinct(true);

        ReportCondition reportCondition = new ReportCondition();
        reportCondition.addNullCondition("deleted");
        reportCondition.addMinNumberCondition("id", 0);
        if (filter.getQueryParam() == null || filter.getQueryParam().equals("")) {

            reportCondition.addEqualCondition("id", filter.getId());
            reportCondition.addLikeCondition("mobile", filter.getMobile());
            reportCondition.addEqualCondition("mobileConfirmed", filter.getMobileConfirmed());
            reportCondition.addCaseInsensitiveLikeCondition("email", filter.getEmail());
            reportCondition.addEqualCondition("emailConfirmed", filter.getEmailConfirmed());
            reportCondition.addEqualCondition("legal", filter.getLegal());
            reportCondition.addEqualCondition("medium", filter.getMedium());
            reportCondition.addCaseInsensitiveLikeCondition("firstName", filter.getFirstName());
            reportCondition.addCaseInsensitiveLikeCondition("lastName", filter.getLastName());
            reportCondition.addCaseInsensitiveLikeCondition("legalName", filter.getLegalName());
            reportCondition.addLikeCondition("nationalId", filter.getNationalId());
            reportCondition.addEqualCondition("twoFactorEnabled", filter.getTwoFactorEnabled());
            reportCondition.addEqualCondition("suspended", filter.getSuspended());
            reportCondition.addMinTimeCondition("created", filter.getCreatedMin());
            reportCondition.addMaxTimeCondition("created", filter.getCreatedMax());
            reportCondition.addMinTimeCondition("lockExpired", filter.getLockExpireMin());
            reportCondition.addMaxTimeCondition("lockExpired", filter.getLockExpireMax());
            if (filter.getPod() != null) {
                if (filter.getPod()) {
                    reportCondition.addNotNullCondition("podId");
                } else {
                    reportCondition.addNullCondition("podId");
                }
            }
            reportCondition.addCaseInsensitiveLikeCondition("fullName", filter.getFullName());
            if (filter.getLock() != null) {
                if (filter.getLock()) {
                    reportCondition.addMinTimeCondition("lockExpired", LocalDateTime.now());
                } else {
                    ReportCondition orCondition = new ReportCondition();
                    orCondition.addMaxTimeCondition("lockExpired", LocalDateTime.now());
                    orCondition.addNullCondition("lockExpired");
                    reportCondition.setOrCondition(orCondition);
                }
            }
            ReportCriteriaJoinCondition profileJoinCondition = new ReportCriteriaJoinCondition("profile", JoinType.LEFT);
            if (filter.getGender() != null) {
                profileJoinCondition.addEqualCondition("gender", filter.getGender());
                reportCondition.addJoinCondition(profileJoinCondition);
                profileJoin = true;
            }
            if (filter.getHaveReferer() != null) {
                profileJoin = true;
                if (filter.getHaveReferer()) {
                    profileJoinCondition.addNotNullCondition("refererUserId");
                } else {
                    profileJoinCondition.addNullCondition("refererUserId");
                }
            }
            if (filter.getRoleId() != null || filter.getType() != null) {
                ReportCriteriaJoinCondition joinCondition = new ReportCriteriaJoinCondition("roleRealms", JoinType.INNER);
                joinCondition.addEqualCondition("roleId", filter.getRoleId());
                if (filter.getType() != null) {
                    ReportCriteriaJoinCondition roleJoinCondition = new ReportCriteriaJoinCondition("role", JoinType.INNER);
                    roleJoinCondition.addEqualCondition("type", filter.getType());
                    joinCondition.addJoinCondition(roleJoinCondition);
                }
                reportCondition.addJoinCondition(joinCondition);
            }
            if (filter.getUserLevelId() != null) {
                profileJoinCondition.addEqualCondition("userLevelId", filter.getUserLevelId());
            }
            if (filter.getDepartmentId() != null) {
                ReportCriteriaJoinCondition departmentJoin = new ReportCriteriaJoinCondition("departments", JoinType.LEFT);
                departmentJoin.addEqualCondition("id", filter.getDepartmentId());
                reportCondition.addJoinCondition(departmentJoin);
            }
            if (filter.getRefererId() != null) {
                profileJoinCondition.addEqualCondition("refererUserId", filter.getRefererId());
            }
            reportCondition.addJoinCondition(profileJoinCondition);
        } else {
            if (filter.getQueryParam().startsWith("0") && filter.getQueryParam().length() > 1)
                filter.setQueryParam(filter.getQueryParam().substring(1));
            ReportCondition orCondition = new ReportCondition();
            if (filter.getQueryParam() != null && checkParsableKeyword(filter.getQueryParam()) && filter.getQueryParam().length() < 10) {
                orCondition.addEqualCondition("id", filter.getQueryParam());
            }
            orCondition.addLikeCondition("mobile", filter.getQueryParam());
            orCondition.addCaseInsensitiveLikeCondition("email", filter.getQueryParam());
            orCondition.addCaseInsensitiveLikeCondition("firstName", filter.getQueryParam());
            orCondition.addCaseInsensitiveLikeCondition("lastName", filter.getQueryParam());
            orCondition.addCaseInsensitiveLikeCondition("legalName", filter.getQueryParam());
            orCondition.addLikeCondition("nationalId", filter.getQueryParam());
            orCondition.addCaseInsensitiveLikeCondition("fullName", filter.getQueryParam());
            reportCondition.setOrCondition(orCondition);
        }

        ReportCondition orCondition = new ReportCondition();
        if(filter.getCompleteInfo() != null && filter.getCompleteInfo()){
            orCondition.addNotNullCondition("mobile");
            orCondition.addNotNullCondition("email");
            orCondition.addNotNullCondition("firstName");
            orCondition.addNotNullCondition("lastName");
            orCondition.addNotNullCondition("nationalId");
            reportCondition.setOrCondition(orCondition);
        }else if (filter.getCompleteInfo() != null && !filter.getCompleteInfo()){
            reportCondition.addNullCondition("mobile");
            reportCondition.addNullCondition("email");
            reportCondition.addNullCondition("nationalId");
        }

        return new ReportFilter(reportCondition, reportOption);
    }
}