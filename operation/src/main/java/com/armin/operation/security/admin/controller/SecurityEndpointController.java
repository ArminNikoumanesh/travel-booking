package com.armin.operation.security.admin.controller;

import com.armin.security.statics.constants.SecurityRestApi;
import com.armin.operation.security.admin.dto.endpoint.EndPointFilter;
import com.armin.operation.security.admin.dto.endpoint.EndPointOut;
import com.armin.operation.security.admin.dto.endpoint.EndPointPageableFilter;
import com.armin.operation.security.admin.repository.service.RestService;
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
@Tag(name = "Security Endpoint")
@RestController
@RequestMapping(value = {"${rest.admin}"})
@Validated
public class SecurityEndpointController {

    private final RestService restService;

    @Autowired
    public SecurityEndpointController(RestService restService) {
        this.restService = restService;
    }

    @Operation(description = "count endpoints")
    @GetMapping(path = SecurityRestApi.SECURITY_ENDPOINTS_COUNT, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Integer> count(@Valid EndPointFilter endPointFilter, BindingResult bindingResult, HttpServletRequest request) {
        return new ResponseEntity<>(restService.countEntity(endPointFilter), HttpStatus.OK);
    }

    @Operation(description = "get all endpoints")
    @GetMapping(path = SecurityRestApi.SECURITY_ENDPOINTS, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<EndPointOut>> getAll(@Valid EndPointPageableFilter endPointPageableFilter, BindingResult bindingResult, @RequestParam(value = "include", required = false) String[] include, HttpServletRequest request) {
        return new ResponseEntity<>(restService.getAll(endPointPageableFilter, include), HttpStatus.OK);
    }

    @Operation(description = "get endpoint by id")
    @GetMapping(path = SecurityRestApi.SECURITY_ENDPOINTS_ID, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<EndPointOut> getById(@PathVariable("id") Integer id, @RequestParam(value = "include", required = false) String[] include, HttpServletRequest request) throws SystemException {
        return new ResponseEntity<>(restService.getById(id, include), HttpStatus.OK);
    }

}
