package com.armin.operation.admin.controller;

import com.armin.operation.admin.model.dto.account.SecurityConfigOut;
import com.armin.operation.admin.repository.service.AntiForgeryService;
import com.armin.operation.admin.statics.constants.AntiForgeryRestApi;
import com.armin.utility.object.NoLogging;
import com.armin.utility.object.SystemException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;

@Tag(name = "AntiForgery")
@RestController
@RequestMapping(value = {"${rest.public}"})
@Validated
@NoLogging
public class AntiForgeryController {
    private final AntiForgeryService antiForgeryService;

    public AntiForgeryController(AntiForgeryService antiForgeryService) {
        this.antiForgeryService = antiForgeryService;
    }

    @Operation(description = "generate anti-forgery token")
    @GetMapping(path = AntiForgeryRestApi.ANTI_FORGERY, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SecurityConfigOut> getToken(@PathVariable("key") String key, HttpServletRequest request) throws SystemException {
        return new ResponseEntity<>(antiForgeryService.generate(key), HttpStatus.OK);
    }
}
