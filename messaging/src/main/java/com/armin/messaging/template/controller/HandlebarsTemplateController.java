package com.armin.messaging.template.controller;

import com.armin.utility.object.SystemException;
import com.armin.messaging.template.repository.dto.HandlebarsTemplateEdit;
import com.armin.messaging.template.repository.dto.HandlebarsTemplateFilter;
import com.armin.messaging.template.repository.dto.HandlebarsTemplateOut;
import com.armin.messaging.template.repository.dto.HandlebarsTemplatePageableFilter;
import com.armin.messaging.template.repository.service.HandlebarsTemplateService;
import com.armin.messaging.template.statics.constants.HandlebarsTemplateRestApi;
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
 * @author : Armin.Nik
 * @project : shared
 * @date : 09.06.24
 */
@Tag(name = "Handlebars Template Controller")
@RestController
@RequestMapping(value = {"${rest.admin}"})
@Validated
public class HandlebarsTemplateController {
    private final HandlebarsTemplateService service;

    @Autowired
    public HandlebarsTemplateController(HandlebarsTemplateService service) {
        this.service = service;
    }

//    @ApiOperation(value = "get handlebars template by id ", response = HandlebarsTemplateOut.class)
    @GetMapping(path = HandlebarsTemplateRestApi.HANDLEBARS_TEMPLATE_ID, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<HandlebarsTemplateOut> getById(@PathVariable("id") Integer id, HttpServletRequest httpServletRequest) throws SystemException {
        return new ResponseEntity<>(service.getById(id), HttpStatus.OK);
    }

//    @ApiOperation(value = "update handlebars template ", response = HandlebarsTemplateOut.class)
    @PutMapping(path = HandlebarsTemplateRestApi.HANDLEBARS_TEMPLATE_ID, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<HandlebarsTemplateOut> update(@PathVariable("id") Integer id, @Valid @RequestBody HandlebarsTemplateEdit handlebarsTemplateEdit, BindingResult bindingResult, HttpServletRequest httpServletRequest) throws SystemException {
        return new ResponseEntity<>(service.update(id, handlebarsTemplateEdit), HttpStatus.OK);
    }

//    @ApiOperation(value = "get all handlebars templates ", response = HandlebarsTemplateOut.class, responseContainer = "List")
    @GetMapping(path = HandlebarsTemplateRestApi.HANDLEBARS_TEMPLATES, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<HandlebarsTemplateOut>> getAll(@Valid HandlebarsTemplatePageableFilter pageableFilter, BindingResult bindingResult, HttpServletRequest httpServletRequest) {
        return new ResponseEntity<>(service.getAll(pageableFilter), HttpStatus.OK);
    }

//    @ApiOperation(value = "count handlebars template ", response = HandlebarsTemplateOut.class)
    @GetMapping(path = HandlebarsTemplateRestApi.HANDLEBARS_TEMPLATES_COUNT, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Integer> count(@Valid HandlebarsTemplateFilter filter, BindingResult bindingResult, HttpServletRequest httpServletRequest) throws SystemException {
        return new ResponseEntity<>(service.count(filter), HttpStatus.OK);
    }
}
