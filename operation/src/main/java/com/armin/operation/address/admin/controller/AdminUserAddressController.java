package com.armin.operation.address.admin.controller;

import com.armin.operation.address.admin.model.UserAddressFilter;
import com.armin.operation.address.admin.model.UserAddressOut;
import com.armin.operation.address.admin.model.UserAddressPageableFilter;
import com.armin.operation.address.admin.repository.service.AdminUserAddressService;
import com.armin.operation.address.statics.constants.UserAddressRestApi;
import com.armin.utility.object.NoLogging;
import com.armin.utility.object.SystemException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import java.util.List;

/**
 * @auther : Armin.Nik
 * @date : 20.11.22
 */
@Tag(name = "Admin User Address Controller")
@Validated
@NoLogging
@RestController
@RequestMapping(value = {"${rest.admin}"})
public class AdminUserAddressController {
    private final AdminUserAddressService userAddressService;

    @Autowired
    public AdminUserAddressController(AdminUserAddressService userAddressService) {
        this.userAddressService = userAddressService;
    }

    @Operation(description = "get all user address ")
    @GetMapping(path = UserAddressRestApi.USER_ADDRESS, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<UserAddressOut>> getAll(@Valid UserAddressPageableFilter pageableFilter, @RequestParam(value = "include", required = false) String[] include, BindingResult bindingResult, HttpServletRequest httpServletRequest) throws SystemException {
        return new ResponseEntity<>(this.userAddressService.getAll(pageableFilter, include), HttpStatus.OK);
    }

    @Operation(description = "count ")
    @GetMapping(path = UserAddressRestApi.USER_ADDRESS_COUNT, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Integer> count(@Valid UserAddressFilter filter, BindingResult bindingResult, HttpServletRequest httpServletRequest) throws SystemException {
        return new ResponseEntity<>(this.userAddressService.countEntity(filter), HttpStatus.OK);
    }
}
