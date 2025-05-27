package com.armin.operation.security.admin.controller;

import com.armin.security.statics.constants.SecurityRestApi;
import com.armin.operation.security.admin.dto.permission.*;
import com.armin.operation.security.admin.repository.service.PermissionService;
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
 * @author imax on 5/21/19
 */
@Tag(name = "Security Permission")
@RestController
@RequestMapping(value = {"${rest.admin}"})
@Validated
public class SecurityPermissionController {

    private final PermissionService permissionService;

    @Autowired
    public SecurityPermissionController(PermissionService permissionService) {
        this.permissionService = permissionService;
    }

    @Operation(description = "count permissions admin")
    @GetMapping(path = SecurityRestApi.SECURITY_PERMISSIONS_COUNT, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Integer> countAdmin(@Valid PermissionFilter permissionFilter, BindingResult bindingResult, HttpServletRequest request) {
        return new ResponseEntity<>(permissionService.countAdmin(permissionFilter), HttpStatus.OK);
    }

    @Operation(description = "get all permissions admin")
    @GetMapping(path = SecurityRestApi.SECURITY_PERMISSIONS, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<PermissionOut>> getAllAdmin(@Valid PermissionFilter permissionFilter, BindingResult bindingResult, @RequestParam(value = "include", required = false) String[] include, HttpServletRequest request) {
        return new ResponseEntity<>(permissionService.getAllAdmin(permissionFilter, include), HttpStatus.OK);
    }

    @Operation(description = "get all permissions Info admin")
    @GetMapping(path = SecurityRestApi.SECURITY_PERMISSIONS_INFO, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<PermissionInfo>> getAllInfoAdmin(@Valid PermissionPageableFilter permissionPageableFilter, BindingResult bindingResult, @RequestParam(value = "include", required = false) String[] include, HttpServletRequest request) {
        return new ResponseEntity<>(permissionService.getAllInfoAdmin(permissionPageableFilter, include), HttpStatus.OK);
    }

    @Operation(description = "get permission by id")
    @GetMapping(path = SecurityRestApi.SECURITY_PERMISSIONS_ID, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PermissionOut> getById(@PathVariable("id") Integer id, @RequestParam(value = "include", required = false) String[] include, HttpServletRequest httpServletRequest) throws SystemException {
        return new ResponseEntity<>(permissionService.getById(id, include), HttpStatus.OK);
    }

    @Operation(description = "create permission")
    @PostMapping(path = SecurityRestApi.SECURITY_PERMISSIONS, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PermissionOut> create(@Valid @RequestBody PermissionIn permission, BindingResult bindingResult, HttpServletRequest httpServletRequest) throws SystemException {
        return new ResponseEntity<>(permissionService.create(permission, httpServletRequest.getRemoteAddr()), HttpStatus.OK);
    }

    @Operation(description = "update permission")
    @PutMapping(path = SecurityRestApi.SECURITY_PERMISSIONS_ID, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PermissionOut> update(@RequestBody PermissionIn permission, BindingResult bindingResult, @PathVariable("id") int id, HttpServletRequest httpServletRequest) throws SystemException {
        return new ResponseEntity<>(permissionService.update(id, permission, httpServletRequest.getRemoteAddr()), HttpStatus.OK);
    }

}