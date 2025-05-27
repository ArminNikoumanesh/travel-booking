package com.armin.operation.admin.controller;

import com.armin.operation.admin.model.dto.account.*;
import com.armin.operation.admin.model.dto.common.*;
import com.armin.operation.admin.model.dto.profile.ProfileImageIn;
import com.armin.operation.admin.model.dto.profile.ProfileImageOut;
import com.armin.operation.admin.model.object.HavePassword;
import com.armin.operation.admin.repository.service.AccountService;
import com.armin.security.authentication.filter.JwtUser;
import com.armin.security.bl.AccessService;
import com.armin.utility.object.ClientInfo;
import com.armin.utility.object.NoLogging;
import com.armin.utility.object.SystemException;
import com.armin.utility.object.UserContextDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;

@Tag(name = "Account")
@RestController
@RequestMapping
@Validated
@NoLogging
public class AccountController {
    private final AccessService accessService;
    private final AccountService accountService;

    @Autowired
    public AccountController(AccountService accountService, AccessService accessService) {
        this.accountService = accountService;
        this.accessService = accessService;
    }

    @Operation(description = "User Login")
    @PostMapping(path = {"${rest.public}" + "/account/login"}, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<LoginOut> login(@RequestBody @Valid LoginIn model, BindingResult bindingResult, HttpServletRequest request) throws SystemException {
        ClientInfo clientInfo = new ClientInfo(request);
        return new ResponseEntity<>(accountService.login(model, clientInfo), HttpStatus.OK);
    }

    @Operation(description = "User Login")
    @PostMapping(path = {"${rest.public}" + "/account/login/web-service"}, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<LoginOut> loginWithToken(HttpServletRequest request) throws SystemException {
        ClientInfo clientInfo = new ClientInfo(request);
        UserContextDto authenticatedUser = JwtUser.getAuthenticatedUser();
        return new ResponseEntity<>(accountService.login(authenticatedUser.getId(), clientInfo), HttpStatus.OK);
    }

    @Operation(description = "Refresh Token")
    @GetMapping(path = {"${rest.public}" + "/account/refresh"}, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<LoginOut> refresh(HttpServletRequest request, HttpServletResponse response) throws SystemException {
        int sessionId = accessService.getSessionIdFromRefreshToken(request);
        String token = accessService.getAuthenticatedToken(request);
        UserContextDto userContextDto = accessService.getAuthenticatedUserFromRefreshToken(request);
        return new ResponseEntity<>(accountService.refresh(userContextDto, sessionId, token), HttpStatus.OK);
    }

    @Operation(description = "User Registration")
    @PostMapping(path = {"${rest.public}" + "/account/register"}, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<LoginOut> register(@RequestBody @Valid UserRegisterIn model, BindingResult bindingResult, HttpServletRequest request) throws SystemException {
        ClientInfo clientInfo = new ClientInfo(request);
        if (model.getLegal() == null && model.getUser().getLegal() != null) {
            model.setLegal(model.getUser().getLegal());
        }
        return new ResponseEntity<>(accountService.register(model, clientInfo), HttpStatus.OK);
    }

    @Operation(description = "User Logout")
    @PostMapping(path = {"${rest.public}" + "/account/logout"}, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void logout(HttpServletRequest request) throws SystemException {
        int sessionId = accessService.getSessionIdFromAccessToken(request);
        accountService.logout(sessionId);
    }

    @Operation(description = "Forgot Password")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PostMapping(path = {"${rest.public}" + "/account/forgot-password"}, produces = MediaType.APPLICATION_JSON_VALUE)
    public void forgotPassword(@RequestBody @Valid ForgotPasswordIn model, BindingResult bindingResult, HttpServletRequest request) throws SystemException, UnsupportedEncodingException {
        accountService.forgotPassword(model);
    }

    @Operation(description = "Reset Password")
    @PostMapping(path = {"${rest.public}" + "/account/reset-password"}, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void resetPassword(@RequestBody @Valid ResetPasswordIn model, BindingResult bindingResult, HttpServletRequest request) throws SystemException {
        accountService.resetPassword(model);
    }

    @Operation(description = "Change Password")
    @PatchMapping(path = {"${rest.identified}" + "/account/change-password"}, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void changePassword(@RequestBody @Valid ChangePasswordIn model, BindingResult bindingResult, HttpServletRequest request) throws SystemException {
        UserContextDto contextDto = JwtUser.getAuthenticatedUser();
        accountService.changePassword(contextDto.getId(), model);
    }

    @Operation(description = "Change Mobile Number")
    @PatchMapping(path = {"${rest.public}" + "/account/change-mobile"}, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void changeMobile(@RequestBody @NotNull StringModel newMobile, BindingResult bindingResult, HttpServletRequest request) throws SystemException {
        UserContextDto contextDto = JwtUser.getAuthenticatedUser();
        accountService.changeMobile(contextDto.getId(), newMobile.getInput());
    }

    @Operation(description = "Change Email Address")
    @PatchMapping(path = {"${rest.public}" + "/account/change-email"}, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void changeEmail(@RequestBody @NotNull StringModel newEmail, BindingResult bindingResult, HttpServletRequest request) throws SystemException {
        UserContextDto contextDto = JwtUser.getAuthenticatedUser();
        accountService.changeEmail(contextDto.getId(), newEmail.getInput());
    }

    @Operation(description = "Confirm Mobile Number")
    @PostMapping(path = {"${rest.public}" + "/account/confirm-mobile"}, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void confirmMobile(@RequestBody StringModel code, BindingResult bindingResult, HttpServletRequest request) throws SystemException {
        UserContextDto contextDto = JwtUser.getAuthenticatedUser();
        accountService.confirmMobile(contextDto.getId(), code.getInput());
    }

    @Operation(description = "Confirm Email Address")
    @PostMapping(path = {"${rest.public}" + "/account/confirm-email"}, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void confirmEmail(@RequestBody StringModel code, BindingResult bindingResult, HttpServletRequest request) throws SystemException {
        UserContextDto contextDto = JwtUser.getAuthenticatedUser();
        accountService.confirmEmail(contextDto.getId(), code.getInput());
    }

    @Operation(description = "Enable Two-Factor Authentication")
    @PatchMapping(path = {"${rest.identified}" + "/account/enable-two-factor-login"}, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void enableTwoFactorLogin(HttpServletRequest request) throws SystemException {
        UserContextDto contextDto = JwtUser.getAuthenticatedUser();
        accountService.enableTwoFactorLogin(contextDto.getId());
    }

    @Operation(description = "Disable Two-Factor Authentication")
    @PatchMapping(path = {"${rest.identified}" + "/account/disable-two-factor-login"}, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void disableTwoFactorLogin(HttpServletRequest request) throws SystemException {
        UserContextDto contextDto = JwtUser.getAuthenticatedUser();
        accountService.disableTwoFactorLogin(contextDto.getId());
    }

    @Operation(description = "Send OTP code to provided phone number or email address")
    @PostMapping(path = {"${rest.public}" + "/account/send-otp"}, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void sendOtp(@RequestBody @Valid SendOtpIn model, BindingResult bindingResult, HttpServletRequest request) throws SystemException {
        accountService.sendOtp(model);
    }

    @Operation(description = "Login / Register via OTP code")
    @PostMapping(path = {"${rest.public}" + "/account/verify-otp"}, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<LoginOut> verifyOtp(@RequestBody @Valid VerifyOtpIn model, BindingResult bindingResult, HttpServletRequest request) throws SystemException {
        ClientInfo clientInfo = new ClientInfo(request);
        return new ResponseEntity<>(accountService.verifyOtp(model, clientInfo), HttpStatus.OK);
    }

    @Operation(description = "Get user's profile")
    @GetMapping(path = {"${rest.public}" + "/account/profile"}, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserProfileOut> getProfile(HttpServletRequest request) throws SystemException {
        UserContextDto contextDto = JwtUser.getAuthenticatedUser();
        return new ResponseEntity<>(accountService.getProfile(contextDto.getId()), HttpStatus.OK);
    }

    @Operation(description = "Replace attributes for user profile")
    @PutMapping(path = {"${rest.public}" + "/account/profile"}, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserProfileOut> updateProfile(@RequestBody @Valid UserProfileEditIn model, BindingResult bindingResult, HttpServletRequest request) throws SystemException {
        UserContextDto contextDto = JwtUser.getAuthenticatedUser();
        return new ResponseEntity<>(accountService.updateProfile(contextDto.getId(), model), HttpStatus.OK);
    }

    @Operation(description = "Check user password is null or not - old way")
    @GetMapping(path = {"${rest.public}" + "/account/profile/have-pass/{username}"}, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<HavePassword> havePasswordOld(@PathVariable(name = "username") String username, HttpServletRequest request) throws SystemException {
        return new ResponseEntity<>(accountService.havePassword(username), HttpStatus.OK);
    }

    @Operation(description = "Check user password is null or not")
    @GetMapping(path = {"${rest.identified}" + "/account/profile/have-pass"}, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<HavePassword> havePassword(HttpServletRequest request) throws SystemException {
        UserContextDto currentUser = JwtUser.getAuthenticatedUser();
        return new ResponseEntity<>(accountService.havePassword(currentUser.getId()), HttpStatus.OK);
    }

    @Operation(description = "User Login")
    @PostMapping(path = {"${rest.public}" + "/account/login-register-by-device-id"}, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity loginOrRegisterByDeviceId(@RequestBody @Valid LoginRegisterByDeviceIdIn model, BindingResult bindingResult, HttpServletRequest request) throws SystemException {
        ClientInfo clientInfo = new ClientInfo(request);
        return new ResponseEntity<>(accountService.loginOrRegisterByDeviceId(model, clientInfo), HttpStatus.OK);
    }

//    @Operation(description = "Simple User Registration")
//    @PostMapping(path = {"${rest.public}" + "/account/register-simple"}, produces = MediaType.APPLICATION_JSON_VALUE)
//    public void registerSimple(@RequestBody @Valid RegisterSimpleIn model, BindingResult bindingResult, HttpServletRequest request) throws SystemException {
//        accountService.registerSimple(model);
//    }

    @Operation(description = "User existence checker")
    @PostMapping(path = {"${rest.public}" + "/account/user-exists"}, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity registerSimple(@RequestBody @Valid CheckUserExistsIn model, BindingResult bindingResult, HttpServletRequest request) throws SystemException {
        return new ResponseEntity<>(accountService.checkUserExistence(model), HttpStatus.OK);
    }

    @Operation(description = "Update Business Profile Image")
    @PutMapping(path = {"${rest.public}" + "/account/business/profile/image"}, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ProfileImageOut> updateProfileImage(@RequestBody @Valid ProfileImageIn model,
                                                              BindingResult bindingResult, HttpServletRequest request) throws SystemException {
        UserContextDto contextDto = JwtUser.getAuthenticatedUser();
        return new ResponseEntity<>(accountService.updateProfileImage(contextDto.getId(), model), HttpStatus.OK);
    }

    @Operation(description = "Get Business Profile Image")
    @GetMapping(path = {"${rest.public}" + "/account/business/profile/image"}, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ProfileImageOut> getProfileImage() throws SystemException {
        UserContextDto contextDto = JwtUser.getAuthenticatedUser();
        return new ResponseEntity<>(accountService.getProfileImage(contextDto.getId()), HttpStatus.OK);
    }
}
