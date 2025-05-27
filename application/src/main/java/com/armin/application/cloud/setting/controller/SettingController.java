package com.armin.application.cloud.setting.controller;

import com.armin.application.cloud.setting.service.SettingService;
import com.armin.application.config.PropertyInitializer;
import com.armin.utility.object.NoLogging;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;

@Tag(name = "Setting")
@RestController
@RequestMapping(value = {"${rest.public}"})
@Validated
@NoLogging
public class SettingController {

    private final SettingService settingService;
    private final PropertyInitializer propertyInitializer;

    @Autowired
    public SettingController(SettingService settingService, PropertyInitializer propertyInitializer) {
        this.settingService = settingService;
        this.propertyInitializer = propertyInitializer;
    }

    @Operation(description = "Get all coupons matched by filter from the data source")
    @GetMapping(path = "/setting", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getAll(@RequestParam(value = "include", required = false) String[] include, HttpServletRequest httpServletRequest) {
        return new ResponseEntity<>(settingService.getApplicationSetting(include), HttpStatus.OK);
    }

//    @Operation(description = "Send Refresh Event To Kafka")
//    @PostMapping(path = "/send-event", produces = MediaType.APPLICATION_JSON_VALUE)
//    public void refreshEvent(HttpServletRequest httpServletRequest) {
//        propertyInitializer.sendRefreshEvent();
//    }
}
