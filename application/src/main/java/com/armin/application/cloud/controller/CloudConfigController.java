package com.armin.application.cloud.controller;


import com.armin.application.cloud.constants.CloudRestApi;
import com.armin.application.cloud.model.security.AccountConfig;
import com.armin.application.cloud.service.CloudConfigService;
import com.armin.utility.object.SystemException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import java.io.IOException;

import static com.armin.utility.object.ClientIpInfo.getMainIP;


@Tag(name = "Cloud Config Settings Controller")
@RestController
@RequestMapping(value = {"${rest.admin}"})
@Validated
public class CloudConfigController {
    private final CloudConfigService cloudConfigService;

    @Autowired
    public CloudConfigController(CloudConfigService cloudConfigService) {
        this.cloudConfigService = cloudConfigService;
    }

    @Operation(description = "Get all Account settings from cloud config")
    @GetMapping(path = CloudRestApi.ACCOUNT, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AccountConfig> getIdentitySettings(HttpServletRequest httpServletRequest) {
        return new ResponseEntity<>(cloudConfigService.getAccountConfigs(), HttpStatus.OK);
    }

    @Operation(description = "update Account settings in cloud config")
    @PutMapping(path = CloudRestApi.ACCOUNT, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AccountConfig> updateIdentitySettings(@Valid @RequestBody AccountConfig model, BindingResult bindingResult, HttpServletRequest httpServletRequest) throws JSONException, SystemException, IOException {
        return new ResponseEntity<>(cloudConfigService.updateAccountConfig(model, getMainIP(httpServletRequest)), HttpStatus.OK);
    }
}
