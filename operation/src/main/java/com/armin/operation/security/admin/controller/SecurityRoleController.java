package com.armin.operation.security.admin.controller;

import com.armin.database.user.statics.PermissionType;
import com.armin.database.user.statics.RoleType;
import com.armin.security.statics.constants.SecurityRestApi;
import com.armin.operation.security.admin.dto.role.*;
import com.armin.operation.security.admin.repository.service.RoleService;
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
import java.util.List;

/**
 * @author imax on 5/22/19
 */
@Tag(name = "Security Role")
@RestController
@RequestMapping(value = {"${rest.admin}"})
@Validated
public class SecurityRoleController {

    private final RoleService roleService;

    @Autowired
    public SecurityRoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @Operation(description = "count roles")
    @GetMapping(path = SecurityRestApi.SECURITY_ROLES_COUNT, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Integer> count(@Valid RoleFilter roleFilter, BindingResult bindingResult, HttpServletRequest httpServletRequest) {
        return new ResponseEntity<>(roleService.countEntity(roleFilter), HttpStatus.OK);
    }

    @Operation(description = "get all roles")
    @GetMapping(path = SecurityRestApi.SECURITY_ROLES, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<RoleOut>> getAll(@Valid RolePageableFilter rolePageableFilter, BindingResult bindingResult, HttpServletRequest httpServletRequest) {
        return new ResponseEntity<>(roleService.getAll(rolePageableFilter), HttpStatus.OK);
    }

    @Operation(description = "get role by id")
    @GetMapping(path = SecurityRestApi.SECURITY_ROLES_ID, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RoleOut> getById(@PathVariable("id") Integer id, @RequestParam(value = "include", required = false) String[] include, HttpServletRequest httpServletRequest) throws SystemException {
        return new ResponseEntity<>(roleService.getById(id, include), HttpStatus.OK);
    }

    @Operation(description = "create admin role")
    @PostMapping(path = SecurityRestApi.SECURITY_ROLES, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RoleOut> createAdmin(@Valid @RequestBody RoleIn roleIn, BindingResult bindingResult, HttpServletRequest httpServletRequest) throws SystemException {
        return new ResponseEntity<>(roleService.create(roleIn, httpServletRequest.getRemoteAddr(), PermissionType.ADMIN, RoleType.ADMIN), HttpStatus.OK);
    }

    @Operation(description = "update admin role")
    @PutMapping(path = SecurityRestApi.SECURITY_ROLES_ID, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RoleOut> update(@Valid @RequestBody RoleIn roleIn, BindingResult bindingResult, @PathVariable("id") Integer id, HttpServletRequest httpServletRequest) throws SystemException {
        return new ResponseEntity<>(roleService.update(id, roleIn, httpServletRequest.getRemoteAddr(), PermissionType.ADMIN), HttpStatus.OK);
    }

    @Operation(description = "delete role")
    @DeleteMapping(path = SecurityRestApi.SECURITY_ROLES_ID, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Boolean> deleteAdminRole(@PathVariable("id") Integer id, HttpServletRequest httpServletRequest) throws SystemException {
        return new ResponseEntity<>(roleService.delete(id, httpServletRequest.getRemoteAddr(), RoleType.ADMIN), HttpStatus.OK);
    }

    @Operation(description = "get all info roles")
    @GetMapping(path = SecurityRestApi.SECURITY_ROLES_INFO, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<RoleInfo>> getAllInfo(@Valid RoleInfoPageableFilter rolePageableFilter, BindingResult bindingResult, @RequestParam(value = "include", required = false) String[] include, HttpServletRequest httpServletRequest) {
        return new ResponseEntity<>(roleService.getAllInfo(rolePageableFilter, include), HttpStatus.OK);
    }

}
