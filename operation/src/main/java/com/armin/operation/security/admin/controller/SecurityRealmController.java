package com.armin.operation.security.admin.controller;

import com.armin.security.statics.constants.SecurityRestApi;
import com.armin.operation.security.admin.dto.realm.*;
import com.armin.operation.security.admin.repository.service.RealmService;
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

@Tag(name = "Security Realm")
@RestController
@RequestMapping(value = {"${rest.admin}"})
@Validated
public class SecurityRealmController {

    private final RealmService realmService;

    @Autowired
    public SecurityRealmController(RealmService realmService) {
        this.realmService = realmService;
    }

    @Operation(description = "count realms")
    @GetMapping(path = SecurityRestApi.SECURITY_REALMS_COUNT, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Integer> count(@Valid RealmFilter realmFilter, BindingResult bindingResult, HttpServletRequest httpServletRequest) {
        return new ResponseEntity<>(realmService.countEntity(realmFilter), HttpStatus.OK);
    }

    @Operation(description = "get all realms")
    @GetMapping(path = SecurityRestApi.SECURITY_REALMS, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<RealmOut>> getAll(@Valid RealmPageableFilter realmPageableFilter, BindingResult bindingResult, @RequestParam(value = "include", required = false) String[] include, HttpServletRequest httpServletRequest) {
        return new ResponseEntity<>(realmService.getAll(realmPageableFilter, include), HttpStatus.OK);
    }

    @Operation(description = "get realm by id")
    @GetMapping(path = SecurityRestApi.SECURITY_REALMS_ID, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RealmOut> getById(@PathVariable("id") Integer id, @RequestParam(value = "include", required = false) String[] include, HttpServletRequest httpServletRequest) throws SystemException {
        return new ResponseEntity<>(realmService.getById(id, include), HttpStatus.OK);
    }

    @Operation(description = "create realm")
    @PostMapping(path = SecurityRestApi.SECURITY_REALMS, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RealmOut> create(@Valid @RequestBody RealmIn realmIn, BindingResult bindingResult, HttpServletRequest httpServletRequest) {
        return new ResponseEntity<>(realmService.create(realmIn), HttpStatus.OK);
    }

    @Operation(description = "update realm")
    @PutMapping(path = SecurityRestApi.SECURITY_REALMS_ID, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RealmOut> update(@Valid @RequestBody RealmIn realmIn, BindingResult bindingResult, @PathVariable("id") Integer id, HttpServletRequest httpServletRequest) throws SystemException {
        return new ResponseEntity<>(realmService.update(id, realmIn), HttpStatus.OK);
    }

    @Operation(description = "delete realm")
    @DeleteMapping(path = SecurityRestApi.SECURITY_REALMS_ID, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Boolean> delete(@PathVariable("id") Integer id, HttpServletRequest httpServletRequest) throws SystemException {
        return new ResponseEntity<>(realmService.delete(id), HttpStatus.OK);
    }

    @Operation(description = "get all realms")
    @GetMapping(path = SecurityRestApi.SECURITY_REALMS_INFO, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<RealmInfo>> getAllInfo(@Valid RealmInfoPageableFilter realmPageableFilter, BindingResult bindingResult, @RequestParam(value = "include", required = false) String[] include, HttpServletRequest httpServletRequest) {
        return new ResponseEntity<>(realmService.getAllInfo(realmPageableFilter, include), HttpStatus.OK);
    }
}
