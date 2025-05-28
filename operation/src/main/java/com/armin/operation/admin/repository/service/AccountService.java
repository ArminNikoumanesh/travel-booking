package com.armin.operation.admin.repository.service;

import com.armin.database.cloud.ApplicationProperties;
import com.armin.database.user.entity.ProfileEntity;
import com.armin.database.user.entity.UserEntity;
import com.armin.database.user.entity.UserSessionEntity;
import com.armin.database.user.statics.UserMedium;
import com.armin.messaging.notification.bl.NotificationService;
import com.armin.messaging.notification.dto.Notification;
import com.armin.operation.admin.model.dto.account.*;
import com.armin.operation.admin.model.dto.common.UserProfileEditIn;
import com.armin.operation.admin.model.dto.common.UserProfileIn;
import com.armin.operation.admin.model.dto.common.UserProfileOut;
import com.armin.operation.admin.model.dto.common.UserRegisterIn;
import com.armin.operation.admin.model.dto.profile.ProfileImageIn;
import com.armin.operation.admin.model.dto.profile.ProfileImageOut;
import com.armin.operation.admin.model.object.HavePassword;
import com.armin.operation.admin.model.object.LoginInfo;
import com.armin.operation.security.admin.dto.session.UserSessionIn;
import com.armin.operation.security.admin.dto.session.UserSessionOut;
import com.armin.operation.security.admin.repository.service.UserSessionService;
import com.armin.security.bl.JwtService;
import com.armin.security.statics.constants.ClientType;
import com.armin.utility.bl.HashService;
import com.armin.utility.bl.NormalizeEngine;
import com.armin.utility.bl.ValidationEngine;
import com.armin.utility.bl.otp.IOtpService;
import com.armin.utility.file.bl.IFileService;
import com.armin.utility.object.*;
import com.armin.utility.statics.enums.SmsType;
import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static com.armin.utility.bl.NormalizeEngine.getNormalizedPhoneNumber;

/**
 * Exceptions error code range: 3001-3100
 */

@Service
public class AccountService {
    private final String[] INCLUDE = {"profile"};
    private final UserSessionService userSessionService;
    private final IOtpService otpService;
    private final UserService userService;
    private final HashService hashService;
    private final JwtService jwtService;
    private final ModelMapper modelMapper;
    private final ApplicationProperties applicationProperties;
    private final NotificationService notificationService;
    private final IFileService fileService;

    @Autowired
    public AccountService(UserSessionService userSessionService, IOtpService otpService, UserService userService,
                          HashService hashService, JwtService jwtService, ModelMapper modelMapper,
                          ApplicationProperties applicationProperties, NotificationService notificationService,
                          IFileService fileService) {
        this.userSessionService = userSessionService;
        this.otpService = otpService;
        this.userService = userService;
        this.hashService = hashService;
        this.jwtService = jwtService;
        this.modelMapper = modelMapper;
        this.applicationProperties = applicationProperties;
        this.notificationService = notificationService;
        this.fileService = fileService;
    }

    private LoginOut login(UserEntity userEntity, String uniqueId, String firebaseToken, ClientType clientType, ClientInfo clientInfo) throws SystemException {
        UserSessionIn userSessionIn = new UserSessionIn();
        userSessionIn.setUserId(userEntity.getId());
        userSessionIn.setClientType(clientType);
        userSessionIn.setUniqueId(uniqueId);
        userSessionIn.setAgent(clientInfo.getAgent());
        userSessionIn.setIp(clientInfo.getMainIP());
        userSessionIn.setOs(clientInfo.getClientOS());
        userSessionIn.setFirebaseToken(firebaseToken);

        UserSessionOut userSessionOut = userSessionService.getExistingSessionOrCreateNewSession(userSessionIn);
        TokenInfo tokenInfo = this.jwtService.create(userEntity.getId(), userSessionOut.getId(), clientType);

        Map<String, Object> objectMap = new HashMap<>();
        objectMap.put("sessionId", userSessionOut.getId());
        objectMap.put("userId", userEntity.getId());
        objectMap.put("firstName", userEntity.getFirstName());
        objectMap.put("lastName", userEntity.getLastName());
        return new LoginOut(userEntity, tokenInfo);
    }

    public LoginOut login(LoginIn model, ClientInfo clientInfo) throws SystemException {
        LoginInfo loginInfo = checkLogin(model.getUsername());
        UserEntity userEntity = userService.getEntityByUsername(loginInfo.getUsername());
        if (userEntity == null) {
            throw new SystemException(SystemError.USER_NOT_FOUND, "username:" + model.getUsername(), 3001);
        }

        if (userEntity.getLockExpired() != null && userEntity.getLockExpired().compareTo(LocalDateTime.now()) > 0) {
            throw new SystemException(SystemError.USER_NOT_ACTIVE, "username:" + model.getUsername(), 3002);
        }
        if (userEntity.isSuspended()) {
            throw new SystemException(SystemError.USER_NOT_ACTIVE, "userId:" + userEntity.getId(), 2056);
        }

        checkUserSuspension(userEntity);
        String securePassword = this.hashService.hash(model.getPassword());
        if (!securePassword.equals(userEntity.getHashedPassword())) {
            userEntity.setAccessFailedCount(userEntity.getAccessFailedCount() + 1);
            if (userEntity.getAccessFailedCount() > applicationProperties.getIdentitySettings().getLockout().getMaxFailedAccessAttempts()) {
                //from properties
                userEntity.setLockExpired(LocalDateTime.now().plusMinutes(5));
                userEntity.setAccessFailedCount(0);
            }
            userService.updateEntity(userEntity);
            throw new SystemException(SystemError.USERNAME_PASSWORD_NOT_MATCH, "", 3003);
        }

        if (userEntity.getAccessFailedCount() != 0) {
            userService.updateAccessFailedCount(userEntity.getId());
        }
        if (userEntity.isTwoFactorEnabled()) {
            SendOtpIn sendOtpIn = new SendOtpIn();
            sendOtpIn.setToken(model.getToken());
            if (loginInfo.isMobile()) {
                sendOtpIn.setMobile(loginInfo.getUsername());
                sendOtpIn.setSendViaSms(true);
            } else {
                sendOtpIn.setEmail(loginInfo.getUsername());
                sendOtpIn.setSendViaEmail(true);
            }
            sendOtp(sendOtpIn);
            return new LoginOut();
        }
        return login(userEntity, model.getUniqueId(), model.getFirebaseToken(), model.getClientType(), clientInfo);
    }

    public LoginOut login(UserEntity userEntity, ClientInfo clientInfo) throws SystemException {
        if (userEntity.getLockExpired() != null && userEntity.getLockExpired().compareTo(LocalDateTime.now()) > 0) {
            throw new SystemException(SystemError.USER_NOT_ACTIVE, "username:" + userEntity.getMobile(), 3002);
        }
        return login(userEntity, null, null, ClientType.WEB_SERVICE, clientInfo);
    }

    public LoginOut login(Integer userId, ClientInfo clientInfo) throws SystemException {
        UserEntity userEntity = userService.getEntityById(userId, new String[]{"profile", "roleRealms.role.permissions"});
        return login(userEntity, clientInfo);
    }

    public LoginOut refresh(UserContextDto userContextDto, Integer sessionId, String token) throws SystemException {
        UserEntity userEntity = userService.getEntityById(userContextDto.getId(), INCLUDE);
        if (userEntity.getLockExpired() != null && userEntity.getLockExpired().compareTo(LocalDateTime.now()) > 0) {
            throw new SystemException(SystemError.USER_NOT_ACTIVE, "username:" + userEntity.getMobile(), 3004);
        }

        UserSessionEntity userSessionEntity = this.userSessionService.getEntityById(sessionId, null);
        if (userSessionEntity == null) {
            throw new SystemException(SystemError.USER_NOT_LOGIN, "username:" + userEntity.getMobile(), 3005);
        }
        checkUserSuspension(userEntity);
        TokenInfo tokenInfo = jwtService.refresh(token, userSessionEntity.getClientType());
        return new LoginOut(userEntity, tokenInfo);
    }

    //reading contract general record id from config
    @Transactional
    public LoginOut register(UserRegisterIn model, ClientInfo clientInfo) throws SystemException {
        if (!applicationProperties.getIdentitySettings().getRegistration().isRegisterEnabled()) {
            throw new SystemException(SystemError.ILLEGAL_REQUEST, "register:" + applicationProperties.getIdentitySettings().getRegistration().isRegisterEnabled(), 3010);
        }

        if (applicationProperties.getIdentitySettings().getRegistration().isShowEconomicCode() && applicationProperties.getIdentitySettings().getRegistration().isRequireEconomicCode() && model.getLegal() && model.getProfile().getEconomicCode() == null) {
            throw new SystemException(SystemError.VALIDATION_EXCEPTION, "economic code is required", 123);
        }

        if (applicationProperties.getIdentitySettings().getRegistration().isRequireFirstName() && StringUtils.isBlank(model.getUser().getFirstName())) {
            throw new SystemException(SystemError.VALIDATION_EXCEPTION, "firstName is required", 3502);
        }

        if (applicationProperties.getIdentitySettings().getRegistration().isRequireLastName() && StringUtils.isBlank(model.getUser().getLastName())) {
            throw new SystemException(SystemError.VALIDATION_EXCEPTION, "lastName is required", 3503);
        }

        if (applicationProperties.getIdentitySettings().getRegistration().isRequireMobile() && StringUtils.isBlank(model.getUser().getMobile())) {
            throw new SystemException(SystemError.VALIDATION_EXCEPTION, "mobile is required", 3504);
        }

        if (applicationProperties.getIdentitySettings().getRegistration().isRequireEmail() && StringUtils.isBlank(model.getUser().getEmail())) {
            throw new SystemException(SystemError.VALIDATION_EXCEPTION, "email is required", 3506);
        }

        if (model.getPassword().length() > applicationProperties.getIdentitySettings().getPassword().getMaxLength() ||
                model.getPassword().length() < applicationProperties.getIdentitySettings().getPassword().getRequiredLength()) {
            throw new SystemException(SystemError.ILLEGAL_REQUEST, "password length problem", 3012);
        }

        UserEntity userEntity = userService.getEntityByMobileEmail(model.getUser().getMobile(), model.getUser().getEmail());
        if (userEntity != null) {
            throw new SystemException(SystemError.USERNAME_ALREADY_EXIST, "mobile:" + model.getUser().getMobile() + ",email:" + model.getUser().getEmail(), 3011);
        }

        if (applicationProperties.getIdentitySettings().getRegistration().isRequireNationalId() && StringUtils.isBlank(model.getUser().getNationalId())) {
            throw new SystemException(SystemError.VALIDATION_EXCEPTION, "national id is required", 125);
        }

        if (!model.getLegal() && StringUtils.isNotBlank(model.getUser().getNationalId())) {
            NormalizeEngine.nationalCodeChecker(model.getUser().getNationalId());
        }

        if (StringUtils.isNotBlank(model.getUser().getNationalId())) {
            userEntity = userService.getEntityByNationalId(model.getUser().getNationalId());
            if (userEntity != null) {
                throw new SystemException(SystemError.DUPLICATE_REQUEST, "nationalId", 3699);
            }
        }

        if (!model.getLegal() && applicationProperties.getIdentitySettings().getRegistration().isRequireGender() && model.getProfile().getGender() == null) {
            throw new SystemException(SystemError.VALIDATION_EXCEPTION, "gender", 100);
        }

        userEntity = modelMapper.map(model.getUser(), UserEntity.class);
        if (model.getPassword() != null) {
            userEntity.setHashedPassword(hashService.hash(model.getPassword()));
        }
        userEntity.setLegal(model.getLegal());
        userEntity.setCreated(LocalDateTime.now());
        userEntity.setAccessFailedCount(0);
        userEntity.setFullName();
        userEntity.setMedium(convertMedium(model.getClientType()));
        ProfileEntity profileEntity = new ProfileEntity();
        if (model.getProfile() != null) {
            modelMapper.map(model.getProfile(), profileEntity);
            fileService.manipulateAttachments(null, profileEntity);
        }

        userEntity.setProfile(profileEntity);
        profileEntity.setUserEntity(userEntity);
        userService.createEntity(userEntity);
        return login(userEntity, model.getUniqueId(), model.getFirebaseToken(), model.getClientType(), clientInfo);
    }

    @Transactional
    public LoginOut registerByOtp(VerifyOtpIn model, ClientInfo clientInfo, LoginInfo loginInfo) throws SystemException {
        if (!applicationProperties.getIdentitySettings().getRegistration().isRegisterEnabled()) {
            throw new SystemException(SystemError.ILLEGAL_REQUEST, "register:" + applicationProperties.getIdentitySettings().getRegistration().isRegisterEnabled(), 3015);
        }
        UserEntity userEntity = new UserEntity();
        UserEntity userEntityByDeviceId = null;
        if (model.getDeviceId() != null) {
            userEntityByDeviceId = userService.getEntityByDeviceId(model.getDeviceId());
        }
        if (userEntityByDeviceId != null) {
            return updateRegisteredUser(userEntityByDeviceId, model, clientInfo, loginInfo);
        } else {
            return registerUser(userEntity, model, clientInfo, loginInfo);
        }
    }

    private LoginOut registerUser(UserEntity userEntity, VerifyOtpIn model, ClientInfo clientInfo, LoginInfo loginInfo) throws SystemException {
        userEntity.setCreated(LocalDateTime.now());
        if (loginInfo.isMobile()) {
            userEntity.setMobile(loginInfo.getUsername());
            userEntity.setMobileConfirmed(true);
        } else {
            userEntity.setEmail(loginInfo.getUsername());
            userEntity.setEmailConfirmed(true);
        }
        userEntity.setAccessFailedCount(0);
        userEntity.setFullName();
        userEntity.setMedium(convertMedium(model.getClientType()));
        ProfileEntity profileEntity = new ProfileEntity();
        userEntity.setProfile(profileEntity);
        profileEntity.setUserEntity(userEntity);
        userService.createEntity(userEntity);
        return login(userEntity, model.getUniqueId(), null, model.getClientType(), clientInfo);
    }

    private LoginOut updateRegisteredUser(UserEntity userEntity, VerifyOtpIn model, ClientInfo clientInfo, LoginInfo loginInfo) throws SystemException {
        checkUserSuspension(userEntity);
        userEntity.setAccessFailedCount(0);
        if (loginInfo.isMobile() && !userEntity.isMobileConfirmed()) {
            userEntity.setMobileConfirmed(true);
        } else if (loginInfo.isEmail() && !userEntity.isEmailConfirmed()) {
            userEntity.setEmailConfirmed(true);
        }
        userEntity.setMobile(loginInfo.getUsername());
        userEntity.setDeviceId(null);
        userEntity.setFullName();
        userService.updateEntity(userEntity);
        return login(userEntity, model.getUniqueId(), null, model.getClientType(), clientInfo);
    }

    private UserMedium convertMedium(ClientType clientType) {
        if (clientType.equals(ClientType.IOS)) {
            return UserMedium.IOS;
        } else if (clientType.equals(ClientType.WEB)) {
            return UserMedium.WEB;
        } else if (clientType.equals(ClientType.ANDROID)) {
            return UserMedium.ANDROID;
        } else if (clientType.equals(ClientType.WEB_SERVICE)) {
            return UserMedium.WEB_SERVICE;
        } else {
            return UserMedium.UNKNOWN;
        }
    }

    public void logout(Integer sessionId) {
        userSessionService.delete(sessionId);
    }

    public void forgotPassword(ForgotPasswordIn model) throws SystemException {
        UserEntity user = userService.getEntityByMobileEmail(model.getMobile(), model.getEmail());
        checkUserSuspension(user);
        if (user != null)
            sendOtp(modelMapper.map(model, SendOtpIn.class));
    }

    public void resetPassword(ResetPasswordIn model) throws SystemException {
        LoginInfo loginInfo = checkLogin(model.getUsername());
        boolean validateOtp = otpService.validateOtp(loginInfo.getUsername(), model.getCode());
        if (!applicationProperties.getSmsOtpSandbox() && !validateOtp) {
            throw new SystemException(SystemError.ILLEGAL_ARGUMENT, "code:" + model.getCode(), 3020);
        }
        if (model.getNewPassword().length() > applicationProperties.getIdentitySettings().getPassword().getMaxLength() ||
                model.getNewPasswordConfirm().length() > applicationProperties.getIdentitySettings().getPassword().getMaxLength() ||
                model.getNewPassword().length() < applicationProperties.getIdentitySettings().getPassword().getRequiredLength() ||
                model.getNewPasswordConfirm().length() < applicationProperties.getIdentitySettings().getPassword().getRequiredLength()) {
            throw new SystemException(SystemError.ILLEGAL_REQUEST, "password length problem", 3012);
        }
        UserEntity userEntity = userService.getEntityByUsername(loginInfo.getUsername());
        if (userEntity == null) {
            throw new SystemException(SystemError.ILLEGAL_REQUEST, "username:" + model.getUsername(), 3021);
        }

        if (loginInfo.isMobile() && !userEntity.isMobileConfirmed()) {
            userEntity.setMobileConfirmed(true);
        } else if (loginInfo.isEmail() && !userEntity.isEmailConfirmed()) {
            userEntity.setEmailConfirmed(true);
        }
        userEntity.setHashedPassword(hashService.hash(model.getNewPassword()));
        userService.updateEntity(userEntity);
    }

    public void changePassword(int id, ChangePasswordIn model) throws SystemException {
        UserEntity userEntity = userService.getEntityById(id, null);
        if (userEntity.getHashedPassword() != null && !Objects.equals(hashService.hash(model.getCurrentPassword()), userEntity.getHashedPassword())) {
            throw new SystemException(SystemError.USERNAME_PASSWORD_NOT_MATCH, "id:" + id, 3022);
        }
        if (model.getNewPassword().length() > applicationProperties.getIdentitySettings().getPassword().getMaxLength() ||
                model.getNewPasswordConfirm().length() > applicationProperties.getIdentitySettings().getPassword().getMaxLength() ||
                model.getNewPassword().length() < applicationProperties.getIdentitySettings().getPassword().getRequiredLength() ||
                model.getNewPasswordConfirm().length() < applicationProperties.getIdentitySettings().getPassword().getRequiredLength()) {
            throw new SystemException(SystemError.ILLEGAL_REQUEST, "password length problem", 3012);
        }
        userEntity.setHashedPassword(hashService.hash(model.getNewPassword()));
        userService.updateEntity(userEntity);
    }

    public void changeMobile(int id, String newMobile) throws SystemException {
        String mobile = getNormalizedPhoneNumber(newMobile);
        UserEntity userEntity = userService.getEntityByUsername(mobile);
        if (userEntity != null) {
            throw new SystemException(SystemError.USERNAME_ALREADY_EXIST, "id:" + id, 3022);
        }

        userEntity = userService.getEntityById(id, null);
        if (!Objects.equals(userEntity.getMobile(), mobile)) {
            userEntity.setMobile(mobile);
            userEntity.setMobileConfirmed(false);
            userService.updateEntity(userEntity);
        }
    }

    public void changeEmail(int id, String newEmail) throws SystemException {
        UserEntity userEntity = userService.getEntityByUsername(newEmail);
        if (userEntity != null) {
            throw new SystemException(SystemError.USERNAME_ALREADY_EXIST, "id:" + id, 3022);
        }

        userEntity = userService.getEntityById(id, null);
        if (!Objects.equals(userEntity.getEmail(), newEmail)) {
            userEntity.setEmail(newEmail);
            userEntity.setEmailConfirmed(false);
            userService.updateEntity(userEntity);
        }
    }

    public void confirmMobile(int id, String code) throws SystemException {
        UserEntity userEntity = userService.getEntityById(id, null);
        if (StringUtils.isBlank(userEntity.getMobile())) {
            throw new SystemException(SystemError.ILLEGAL_ARGUMENT, "id:" + id, 3023);
        }
        VerifyOtpIn verifyOtpIn = new VerifyOtpIn(userEntity.getMobile(), code);
        if (!applicationProperties.getSmsOtpSandbox() && !checkOtp(verifyOtpIn)) {
            throw new SystemException(SystemError.ILLEGAL_REQUEST, "id:" + id, 3024);
        } else {
            userEntity.setMobileConfirmed(true);
            userService.updateEntity(userEntity);
        }
    }

    public void confirmEmail(int id, String code) throws SystemException {
        UserEntity userEntity = userService.getEntityById(id, null);
        if (StringUtils.isBlank(userEntity.getEmail())) {
            throw new SystemException(SystemError.EMAIL_ADDRESS_NOT_VALID, "id:" + id, 3025);
        }
        VerifyOtpIn verifyOtpIn = new VerifyOtpIn(userEntity.getEmail(), code);
        if (!applicationProperties.getSmsOtpSandbox() && !checkOtp(verifyOtpIn)) {
            throw new SystemException(SystemError.ILLEGAL_REQUEST, "id:" + id, 3026);
        } else {
            userEntity.setEmailConfirmed(true);
            userService.updateEntity(userEntity);
        }
    }

    public void enableTwoFactorLogin(int id) throws SystemException {
        UserEntity userEntity = userService.getEntityById(id, null);
        userEntity.setTwoFactorEnabled(true);
        userService.updateEntity(userEntity);
    }

    public void disableTwoFactorLogin(int id) throws SystemException {
        UserEntity userEntity = userService.getEntityById(id, null);
        userEntity.setTwoFactorEnabled(false);
        userService.updateEntity(userEntity);
    }

    public void sendOtp(SendOtpIn model) throws SystemException {
        String username = model.getMobile() == null ? model.getEmail() : model.getMobile();
        UserEntity userEntity = userService.getEntityByUsername(username);
        if (userEntity != null && userEntity.isSuspended()) {
            throw new SystemException(SystemError.USER_NOT_ACTIVE, "userId:" + userEntity.getId(), 2056);
        }
        if (applicationProperties.getSmsOtpSandbox()) {
            return;
        }
        if (model.getSendViaSms()) {
            String code = otpService.generateOtp(getNormalizedPhoneNumber(model.getMobile()));
            Notification notification = new Notification.Builder("template", code)
                    .setSmsConfig(model.getMobile(), SmsType.SERVICE)
                    .build();
            notificationService.sendSms(notification);
        } else {
            String code = otpService.generateOtp(model.getEmail());
            Notification notification = new Notification.Builder("template", code)
                    .setEmailConfig(model.getEmail())
                    .build();
            notificationService.sendEmail(notification);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public LoginOut verifyOtp(VerifyOtpIn model, ClientInfo clientInfo) throws SystemException {
        LoginInfo loginInfo = checkLogin(model.getKey());
        boolean validateOtp = otpService.validateOtp(loginInfo.getUsername(), model.getCode());
        if (!applicationProperties.getIdentitySettings().getSignIn().isLoginByOtp()) {
            throw new SystemException(SystemError.ILLEGAL_REQUEST, "verifyOtp:" + applicationProperties.getIdentitySettings().getSignIn().isLoginByOtp(), 3030);
        }
        if (!applicationProperties.getSmsOtpSandbox() && !validateOtp) {
            throw new SystemException(SystemError.ILLEGAL_ARGUMENT, "code:" + model.getCode(), 3031);
        }
        UserEntity userEntity = userService.getEntityByUsername(loginInfo.getUsername());
        if (userEntity != null) {
            userEntity.setAccessFailedCount(0);
            if (loginInfo.isMobile() && !userEntity.isMobileConfirmed()) {
                userEntity.setMobileConfirmed(true);
            } else if (loginInfo.isEmail() && !userEntity.isEmailConfirmed()) {
                userEntity.setEmailConfirmed(true);
            }
            userService.updateEntity(userEntity);
            return login(userEntity, model.getUniqueId(), null, model.getClientType(), clientInfo);
        } else if (applicationProperties.getIdentitySettings().getRegistration().isRegisterByOtp()) {
            return registerByOtp(model, clientInfo, loginInfo);
        }
        return null;
    }

    public UserProfileOut getProfile(int id) throws SystemException {
        return userService.getById(id, INCLUDE);
    }

    public void validateOnUpdate(UserProfileIn model, UserEntity userEntity) throws SystemException {
        if (applicationProperties.getIdentitySettings().getProfile().isShowEconomicCode() && applicationProperties.getIdentitySettings().getProfile().isRequireEconomicCode() && userEntity.isLegal() && model.getProfile().getEconomicCode() == null) {
            throw new SystemException(SystemError.VALIDATION_EXCEPTION, "economic code is required", 123);
        }
        if (userEntity.getNationalId() != null && !userEntity.getNationalId().equals("") &&
                !applicationProperties.getIdentitySettings().getProfile().isChangeNationalId()
                && !Objects.equals(model.getUser().getNationalId(), userEntity.getNationalId())) {
            throw new SystemException(SystemError.VALIDATION_EXCEPTION, "can not change national id", 1253);
        }
    }

    public UserProfileOut updateProfile(int id, UserProfileEditIn model) throws SystemException {
        UserEntity existUser = userService.getEntityByMobileEmail(model.getUser().getMobile(), null);
        UserEntity userEntity = userService.getEntityById(id, INCLUDE);

        if (applicationProperties.getIdentitySettings().getProfile().isRequireFirstName() && StringUtils.isBlank(model.getUser().getFirstName())) {
            throw new SystemException(SystemError.VALIDATION_EXCEPTION, "firstName is required", 3502);
        }

        if (applicationProperties.getIdentitySettings().getProfile().isRequireLastName() && StringUtils.isBlank(model.getUser().getLastName())) {
            throw new SystemException(SystemError.VALIDATION_EXCEPTION, "lastName is required", 3503);
        }

        if (applicationProperties.getIdentitySettings().getProfile().isRequireMobile() && StringUtils.isBlank(model.getUser().getMobile())) {
            throw new SystemException(SystemError.VALIDATION_EXCEPTION, "mobile is required", 3504);
        }

        if (applicationProperties.getIdentitySettings().getProfile().isRequireEmail() && StringUtils.isBlank(model.getUser().getEmail())) {
            throw new SystemException(SystemError.VALIDATION_EXCEPTION, "email is required", 3506);
        }

        if (existUser != null && existUser.getId() != userEntity.getId()) {
            throw new SystemException(SystemError.CHILD_RECORD_FOUND, "duplicate mobile number", 3695);
        }

        validateOnUpdate(model, userEntity);

        if (applicationProperties.getIdentitySettings().getProfile().isRequireNationalId() && StringUtils.isBlank(model.getUser().getNationalId())) {
            throw new SystemException(SystemError.VALIDATION_EXCEPTION, "national id is required", 125);
        }


        if (StringUtils.isNotBlank(model.getUser().getNationalId())) {
            existUser = userService.getEntityByNationalId(model.getUser().getNationalId());
            if (existUser != null && existUser.getId() != id) {
                throw new SystemException(SystemError.DUPLICATE_REQUEST, "duplicate national id", 3699);
            }
        }

        if (!userEntity.isLegal() && applicationProperties.getIdentitySettings().getProfile().isRequireGender() && model.getProfile().getGender() == null) {
            throw new SystemException(SystemError.VALIDATION_EXCEPTION, "gender", 100);
        }

        if (model.getUser() != null) {
            if (!Objects.equals(userEntity.getMobile(), model.getUser().getMobile())) {
                userEntity.setMobileConfirmed(false);
            }
            if (!Objects.equals(userEntity.getEmail(), model.getUser().getEmail())) {
                userEntity.setEmailConfirmed(false);
            }
            model.getUser().setLegal(userEntity.isLegal());
            modelMapper.map(model.getUser(), userEntity);
        }

        ProfileEntity oldModel = userEntity.getProfile().cloneImages();
        modelMapper.map(model.getProfile(), userEntity.getProfile());
//        userEntity.getProfile().setPaperInvoice(model.isPaperInvoice());
        fileService.manipulateAttachments(oldModel, userEntity.getProfile());
        userEntity.setFullName();
        userService.updateEntity(userEntity);

        UserProfileOut userProfileOut = new UserProfileOut();
        modelMapper.map(userEntity, userProfileOut.getUser());
        modelMapper.map(userEntity.getProfile(), userProfileOut.getProfile());
        return userProfileOut;
    }


    private void validateNationalId(String nationalId) throws SystemException {
        if (applicationProperties.getIdentitySettings().getRegistration().isRequireNationalId() && StringUtils.isBlank(nationalId)) {
            throw new SystemException(SystemError.VALIDATION_EXCEPTION, "national id is required", 125);
        }

        if (StringUtils.isNotBlank(nationalId)) {
            NormalizeEngine.nationalCodeChecker(nationalId);
        }
    }

    private LoginInfo checkLogin(String username) throws SystemException {
        LoginInfo loginInfo = new LoginInfo();
        try {
            loginInfo.setUsername(getNormalizedPhoneNumber(username));
            loginInfo.setMobile(true);
        } catch (SystemException ignored) {
            ValidationEngine.validateEmail(username);
            loginInfo.setUsername(username);
            loginInfo.setEmail(true);
        }
        if (loginInfo.isMobile() && applicationProperties.getIdentitySettings().getSignIn().isLoginByMobile()) {
            return loginInfo;
        } else if (loginInfo.isEmail() && applicationProperties.getIdentitySettings().getSignIn().isLoginByEmail()) {
            return loginInfo;
        } else {
            throw new SystemException(SystemError.INVALID_DATA_TYPE, "username:" + username, 3035);
        }
    }

    private Boolean checkOtp(VerifyOtpIn model) {
        return otpService.validateOtp(model.getKey(), model.getCode());
    }

    public HavePassword havePassword(String username) throws SystemException {
        UserEntity user = userService.getEntityByUsername(getNormalizedPhoneNumber(username));
        HavePassword havePassword = new HavePassword();
        if (user != null && user.getHashedPassword() != null) {
            havePassword.setHavePassword(true);
        }
        return havePassword;
    }

    public HavePassword havePassword(int userId) throws SystemException {
        UserEntity user = userService.getEntityById(userId, null);
        HavePassword havePassword = new HavePassword();
        if (user.getHashedPassword() != null) {
            havePassword.setHavePassword(true);
        }
        return havePassword;
    }

    public LoginOut loginOrRegisterByDeviceId(LoginRegisterByDeviceIdIn model, ClientInfo clientInfo) throws SystemException {
        UserEntity userEntity = userService.getEntityByDeviceId(model.getDeviceId());
        if (userEntity == null) {
            userEntity = new UserEntity();
            userEntity.setCreated(LocalDateTime.now());
            userEntity.setAccessFailedCount(0);
            userEntity.setFullName();
            userEntity.setMedium(convertMedium(model.getClientType()));
            userEntity.setDeviceId(model.getDeviceId());
            userEntity.setEmailConfirmed(true);
            userEntity.setMobileConfirmed(true);
            ProfileEntity profileEntity = new ProfileEntity();
            userEntity.setProfile(profileEntity);
            profileEntity.setUserEntity(userEntity);
            userService.createEntity(userEntity);
        }
        return login(userEntity, model.getUniqueId(), model.getFirebaseToken(), model.getClientType(), clientInfo);
    }

    public void registerSimple(RegisterSimpleIn model) throws SystemException {
        if (!applicationProperties.getIdentitySettings().getRegistration().isRegisterEnabled()) {
            throw new SystemException(SystemError.ILLEGAL_REQUEST, "register:" + applicationProperties.getIdentitySettings().getRegistration().isRegisterEnabled(), 3010);
        }

        if (model.getPassword().length() > applicationProperties.getIdentitySettings().getPassword().getMaxLength() ||
                model.getPassword().length() < applicationProperties.getIdentitySettings().getPassword().getRequiredLength()) {
            throw new SystemException(SystemError.ILLEGAL_REQUEST, "password length problem", 3012);
        }
        LoginInfo loginInfo = checkLogin(model.getUsername());
        UserEntity userEntity = userService.getEntityByUsername(loginInfo.getUsername());
        if (userEntity != null) {
            throw new SystemException(SystemError.USERNAME_ALREADY_EXIST, "username:" + model.getUsername(), 3011);
        }
        userEntity = new UserEntity();
        SendOtpIn sendOtpIn = new SendOtpIn();
        if (loginInfo.isMobile()) {
            userEntity.setMobile(loginInfo.getUsername());
            sendOtpIn.setMobile(loginInfo.getUsername());
            sendOtpIn.setSendViaSms(true);
        } else {
            userEntity.setEmail(loginInfo.getUsername());
            sendOtpIn.setEmail(loginInfo.getUsername());
            sendOtpIn.setSendViaEmail(true);
        }
        userEntity.setHashedPassword(hashService.hash(model.getPassword()));
        userEntity.setCreated(LocalDateTime.now());
        userEntity.setAccessFailedCount(0);
        userEntity.setFullName();
        userEntity.setMedium(convertMedium(model.getClientType()));
        ProfileEntity profileEntity = new ProfileEntity();
        userEntity.setProfile(profileEntity);
        profileEntity.setUserEntity(userEntity);
        userService.createEntity(userEntity);
        sendOtp(sendOtpIn);

    }

    public CheckUserExistsOut checkUserExistence(CheckUserExistsIn model) throws SystemException {
        LoginInfo loginInfo = checkLogin(model.getUsername());
        UserEntity userEntity = userService.getEntityByUsername(loginInfo.getUsername());
        return new CheckUserExistsOut(userEntity != null);
    }

    public ProfileImageOut updateProfileImage(int userId, ProfileImageIn model) throws SystemException {
        UserEntity userEntity = userService.getEntityById(userId, INCLUDE);
        ProfileEntity oldModel = userEntity.getProfile().cloneImages();
        modelMapper.map(model, userEntity.getProfile());
        fileService.manipulateAttachments(oldModel, userEntity.getProfile());
        userService.updateEntity(userEntity);
        return new ProfileImageOut(userEntity);
    }

    public ProfileImageOut getProfileImage(int userId) throws SystemException {
        return new ProfileImageOut(userService.getEntityById(userId, INCLUDE));
    }

    private void checkUserSuspension(UserEntity userEntity) throws SystemException {
        if (userEntity != null) {
            if (userEntity.isSuspended()) {
                throw new SystemException(SystemError.USER_NOT_ACTIVE, "userId:" + userEntity.getId(), 2056);
            }
            if (userEntity.getLockExpired() != null && userEntity.getLockExpired().isAfter(LocalDateTime.now())) {
                throw new SystemException(SystemError.USER_NOT_ACTIVE, "userId:" + userEntity.getId(), 3002);
            }
        }
    }
}
