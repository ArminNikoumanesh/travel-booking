package com.armin.messaging.inappmessage.admin.controller;

import com.armin.messaging.inappmessage.admin.model.InAppMessageFilter;
import com.armin.messaging.inappmessage.admin.model.InAppMessageIn;
import com.armin.messaging.inappmessage.admin.model.InAppMessageOut;
import com.armin.messaging.inappmessage.admin.model.InAppMessagePageableFilter;
import com.armin.messaging.inappmessage.admin.repository.AdminInAppMessageService;
import com.armin.messaging.inappmessage.statics.InAppMessageRestApi;
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
 * @author : Armin.Nik
 * @project : shared
 * @date : 20.04.24
 */
@Tag(name = "Admin In App Message Controller")
@Validated
@RestController
@RequestMapping(value = {"${rest.admin}"})
public class AdminInAppMessageController {
    private final AdminInAppMessageService inAppMessageService;

    @Autowired
    public AdminInAppMessageController(AdminInAppMessageService inAppMessageService) {
        this.inAppMessageService = inAppMessageService;
    }

    @Operation(description = "Get All InAppMessages")
    @GetMapping(path = InAppMessageRestApi.IN_APP_MESSAGE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<InAppMessageOut>> getAll(@Valid InAppMessagePageableFilter filter, @RequestParam(name = "include", required = false) String[] include,
                                                        BindingResult bindingResult, HttpServletRequest request) throws SystemException {
        return new ResponseEntity<>(inAppMessageService.getAll(filter, include), HttpStatus.OK);
    }

    @Operation(description = "Get By Id InAppMessage")
    @GetMapping(path = InAppMessageRestApi.IN_APP_MESSAGE_ID, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<InAppMessageOut> getById(@PathVariable("id") Long id, @RequestParam(name = "include", required = false) String[] include,
                                                   HttpServletRequest request) throws SystemException {
        return new ResponseEntity<>(inAppMessageService.getById(id, include), HttpStatus.OK);
    }

    @Operation(description = "Count InAppMessages")
    @GetMapping(path = InAppMessageRestApi.IN_APP_MESSAGE_COUNT, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Integer> count(@Valid InAppMessageFilter filter, BindingResult bindingResult,
                                         HttpServletRequest request) throws SystemException {
        return new ResponseEntity<>(inAppMessageService.count(filter), HttpStatus.OK);
    }

    @Operation(description = "Delete By Id InAppMessage")
    @DeleteMapping(path = InAppMessageRestApi.IN_APP_MESSAGE_ID, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Boolean> delete(@PathVariable(name = "id") Long id, HttpServletRequest request) {
        return new ResponseEntity<>(inAppMessageService.delete(id), HttpStatus.OK);
    }

    @Operation(description = "Create New InAppMessage")
    @PostMapping(path = InAppMessageRestApi.IN_APP_MESSAGE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<InAppMessageOut> create(@Valid @RequestBody InAppMessageIn inModel, BindingResult bindingResult,
                                                  HttpServletRequest request) throws SystemException {
        return new ResponseEntity<>(inAppMessageService.create(inModel), HttpStatus.OK);
    }

    @Operation(description = "Update One InAppMessage")
    @PutMapping(path = InAppMessageRestApi.IN_APP_MESSAGE_ID, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<InAppMessageOut> update(@PathVariable("id") Long id, @Valid @RequestBody InAppMessageIn inModel,
                                                  BindingResult bindingResult, HttpServletRequest request) throws SystemException {
        return new ResponseEntity<>(inAppMessageService.update(id, inModel), HttpStatus.OK);
    }
}

