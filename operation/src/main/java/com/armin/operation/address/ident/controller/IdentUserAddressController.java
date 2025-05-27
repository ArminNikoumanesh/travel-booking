package com.armin.operation.address.ident.controller;

import com.armin.security.authentication.filter.JwtUser;
import com.armin.utility.object.NoLogging;
import com.armin.utility.object.SystemException;
import com.armin.utility.object.UserContextDto;
import com.armin.operation.address.ident.model.UserAddressFilter;
import com.armin.operation.address.ident.model.UserAddressIn;
import com.armin.operation.address.ident.model.UserAddressOut;
import com.armin.operation.address.ident.repository.service.IdentUserAddressService;
import com.armin.operation.address.statics.constants.UserAddressRestApi;
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
 * @auther : Armin.Nik
 * @date : 20.11.22
 */
@Tag(name = "Ident User Address Controller")
@Validated
@NoLogging
@RestController
@RequestMapping(value = {"${rest.identified}"})
public class IdentUserAddressController {
    private final IdentUserAddressService userAddressService;

    @Autowired
    public IdentUserAddressController(IdentUserAddressService userAddressService) {
        this.userAddressService = userAddressService;
    }

    @Operation(description = "create data")
    @PostMapping(path = UserAddressRestApi.USER_ADDRESS, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserAddressOut> create(@RequestBody @Valid UserAddressIn businessSetupIn, BindingResult bindingResult,
                                                 HttpServletRequest httpServletRequest) throws SystemException {
        UserContextDto contextDto = JwtUser.getAuthenticatedUser();
        return new ResponseEntity<>(this.userAddressService.create(businessSetupIn, contextDto.getId()), HttpStatus.OK);
    }

    @Operation(description = "update data ")
    @PutMapping(path = UserAddressRestApi.USER_ADDRESS_ID, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserAddressOut> update(@PathVariable("id") int id,
                                                 @RequestBody @Valid UserAddressIn modelIn,
                                                 BindingResult bindingResult, HttpServletRequest httpServletRequest) throws SystemException {
        UserContextDto contextDto = JwtUser.getAuthenticatedUser();
        return new ResponseEntity<>(this.userAddressService.update(modelIn, id, contextDto.getId()), HttpStatus.OK);
    }

    @Operation(description = "get all user address ")
    @GetMapping(path = UserAddressRestApi.USER_ADDRESS, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<UserAddressOut>> getAll(HttpServletRequest httpServletRequest) throws SystemException {
        UserContextDto contextDto = JwtUser.getAuthenticatedUser();
        return new ResponseEntity<>(this.userAddressService.getAll(contextDto.getId()), HttpStatus.OK);
    }

    @Operation(description = "count user address")
    @GetMapping(path = UserAddressRestApi.USER_ADDRESS_COUNT, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Integer> count(@Valid UserAddressFilter filter, BindingResult bindingResult,
                                         HttpServletRequest httpServletRequest) throws SystemException {
        UserContextDto contextDto = JwtUser.getAuthenticatedUser();
        return new ResponseEntity<>(this.userAddressService.countEntity(filter, contextDto.getId()), HttpStatus.OK);
    }

    @Operation(description = "set Default")
    @PatchMapping(path = UserAddressRestApi.USER_ADDRESS_ID, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Boolean> setDefault(@PathVariable("id") int id, HttpServletRequest httpServletRequest) throws SystemException {
        UserContextDto contextDto = JwtUser.getAuthenticatedUser();
        return new ResponseEntity<>(userAddressService.setDefault(contextDto.getId(), id), HttpStatus.OK);
    }

    @Operation(description = "delete")
    @DeleteMapping(path = UserAddressRestApi.USER_ADDRESS_ID, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Boolean> delete(@PathVariable("id") int id, HttpServletRequest httpServletRequest) throws SystemException {
        UserContextDto contextDto = JwtUser.getAuthenticatedUser();
        return new ResponseEntity<>(userAddressService.delete(contextDto.getId(), id), HttpStatus.OK);
    }
}
