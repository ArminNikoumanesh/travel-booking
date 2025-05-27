package com.armin.utility.config.common;


import com.armin.utility.object.NoLogging;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;

@Tag(name = "Public Application Controller")
@Validated
@NoLogging
@RestController
@RequestMapping(path = {"${rest.public}"})
public class PublicApplicationController {

    @GetMapping(path = "/status", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> ok(HttpServletRequest request) {
        return new ResponseEntity<>("Success", HttpStatus.OK);
    }
}