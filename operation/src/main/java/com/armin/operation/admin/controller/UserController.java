package com.armin.operation.admin.controller;

import com.armin.database.user.statics.RoleType;
import com.armin.operation.admin.model.dto.common.UserManageOut;
import com.armin.operation.admin.model.dto.common.UserProfileInfoOut;
import com.armin.operation.admin.model.dto.common.UserRoleOut;
import com.armin.operation.admin.model.dto.user.*;
import com.armin.operation.admin.repository.service.UserService;
import com.armin.operation.admin.statics.constants.UserRestApi;
import com.armin.utility.object.SystemException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import java.util.Collection;
import java.util.List;

@Tag(name = "User")
@RestController
@RequestMapping(value = {"${rest.admin}"})
@Validated
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Operation(description = "Count All Users")
    @GetMapping(path = UserRestApi.USERS_COUNT, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Integer> count(@Valid UserFilter filter, BindingResult bindingResult, HttpServletRequest request) {
        return new ResponseEntity<>(userService.count(filter), HttpStatus.OK);
    }

    @Operation(description = "Get All Users")
    @GetMapping(path = UserRestApi.USERS, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Collection<UserManageOut>> getAllUsers(@Valid UserPageableFilter userPageableFilter, BindingResult bindingResult,
                                                                 @RequestParam(required = false) String[] include, HttpServletRequest request) {
        return new ResponseEntity<>(userService.getAll(userPageableFilter, include), HttpStatus.OK);
    }

    @Operation(description = "Get User By Id")
    @GetMapping(path = UserRestApi.USERS_ID, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserManageOut> getById(@PathVariable Integer id,
                                                 @RequestParam(required = false) String[] include, HttpServletRequest request) throws SystemException {
        return new ResponseEntity<>(userService.getById(id, include), HttpStatus.OK);
    }

    @Operation(description = "Get All User Info")
    @GetMapping(path = UserRestApi.USERS_INFO, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Collection<UserProfileInfoOut>> getAllInfo(@Valid UserPageableFilter pageableFilter, @RequestParam(required = false) String[] include, HttpServletRequest request) throws SystemException {
        return new ResponseEntity<>(userService.getAllInfo(pageableFilter, include), HttpStatus.OK);
    }

    @Operation(description = "Create User")
    @PostMapping(path = UserRestApi.USERS, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserEditOut> create(@RequestBody @Valid UserEditIn model, BindingResult bindingResult, HttpServletRequest request) throws SystemException {
        return new ResponseEntity<>(userService.create(model), HttpStatus.OK);
    }

    @Operation(description = "Update User By Id")
    @PutMapping(path = UserRestApi.USERS_ID, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserEditOut> update(@PathVariable Integer id,
                                              @RequestBody @Valid UserEditIn model, BindingResult bindingResult, HttpServletRequest request) throws SystemException {
        return new ResponseEntity<>(userService.update(id, model), HttpStatus.OK);
    }

    @Operation(description = "Update User By Id")
    @PatchMapping(path = UserRestApi.USERS_ID_ROLE_REALM, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserEditOut> updateUserRoleRealms(@PathVariable Integer id,
                                                            @RequestBody List<RoleRealmIn> model, HttpServletRequest request) throws SystemException {
        return new ResponseEntity<>(userService.updateUserRoleRealms(id, model, RoleType.ADMIN, request.getRemoteAddr()), HttpStatus.OK);
    }

    @Operation(description = "Delete User By Id")
    @DeleteMapping(path = UserRestApi.USERS_ID, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Boolean> delete(@PathVariable Integer id, HttpServletRequest request) throws SystemException {
        return new ResponseEntity<>(userService.delete(id), HttpStatus.OK);
    }

    @Operation(description = "Enable Two Factor Login User By Id")
    @PatchMapping(path = UserRestApi.USERS_ID_ENABLE_TWO_FACTOR, produces = MediaType.APPLICATION_JSON_VALUE)
    public void enableTwoFactorLogin(@PathVariable Integer id, HttpServletRequest request) {
        userService.enableTwoFactorLogin(id);
    }

    @Operation(description = "Disable Two Factor Login User By Id")
    @PatchMapping(path = UserRestApi.USERS_ID_DISABLE_TWO_FACTOR, produces = MediaType.APPLICATION_JSON_VALUE)
    public void disableTwoFactorLogin(@PathVariable Integer id, HttpServletRequest request) {
        userService.disableTwoFactorLogin(id);
    }

    @Operation(description = "Suspend User By Id")
    @PatchMapping(path = UserRestApi.USERS_ID_SUSPEND, produces = MediaType.APPLICATION_JSON_VALUE)
    public void suspend(@PathVariable Integer id, HttpServletRequest request) throws SystemException {
        userService.suspend(id);
    }

    @Operation(description = "UnSuspend User By Id")
    @PatchMapping(path = UserRestApi.USERS_ID_UNSUSPEND, produces = MediaType.APPLICATION_JSON_VALUE)
    public void unsuspend(@PathVariable Integer id, HttpServletRequest request) {
        userService.unsuspend(id);
    }

    @Operation(description = "Unlock User By Id")
    @PatchMapping(path = UserRestApi.USERS_ID_UNLOCK, produces = MediaType.APPLICATION_JSON_VALUE)
    public void unlock(@PathVariable Integer id, HttpServletRequest request) {
        userService.unlock(id);
    }

    @Operation(description = "Confirm Email User By Id")
    @PatchMapping(path = UserRestApi.USERS_ID_CONFIRM_EMAIL, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Boolean> confirmEmail(@PathVariable Integer id, @RequestBody UserConfirmEmailIn userConfirmEmailIn, BindingResult result, HttpServletRequest request) throws SystemException {
        return new ResponseEntity<>(userService.confirmEmail(id, userConfirmEmailIn), HttpStatus.OK);
    }

    @Operation(description = "Confirm Mobile User By Id")
    @PatchMapping(path = UserRestApi.USERS_ID_CONFIRM_MOBILE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Boolean> confirmMobile(@PathVariable Integer id, @RequestBody UserConfirmMobileIn userConfirmMobileIn, BindingResult result, HttpServletRequest request) throws SystemException {
        return new ResponseEntity<>(userService.confirmMobile(id, userConfirmMobileIn), HttpStatus.OK);
    }

    @Operation(description = "Get UserInfo By Id")
    @GetMapping(path = UserRestApi.USERS_ID_INFO, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserProfileInfoOut> getInfoById(@PathVariable Integer userId,
                                                          @RequestParam(required = false) String[] include, HttpServletRequest request) throws SystemException {
        return new ResponseEntity<>(userService.getInfoById(userId, include), HttpStatus.OK);
    }

    @Operation(description = "Get Admin User Roles By Id")
    @GetMapping(path = UserRestApi.USERS_ID_ADMIN_ROLE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserRoleOut> getAdminUserRolesById(@PathVariable Integer id, HttpServletRequest request) throws SystemException {
        return new ResponseEntity<>(userService.getAdminUserInfoWithRoles(id), HttpStatus.OK);
    }

}
