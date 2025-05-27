package com.armin.messaging.inappmessage.ident.controller;

import com.armin.messaging.inappmessage.ident.model.InAppMessageFilter;
import com.armin.messaging.inappmessage.ident.model.InAppMessageOut;
import com.armin.messaging.inappmessage.ident.model.InAppMessagePageableFilter;
import com.armin.messaging.inappmessage.ident.repository.IdentInAppMessageService;
import com.armin.messaging.inappmessage.statics.InAppMessageRestApi;
import com.armin.security.authentication.filter.JwtUser;
import com.armin.utility.object.SystemException;
import com.armin.utility.object.UserContextDto;
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
@Tag(name = "Ident In App Message Controller")
@Validated
@RestController
@RequestMapping(value = {"${rest.identified}"})
public class IdentInAppMessageController {
    private final IdentInAppMessageService inAppMessageService;

    @Autowired
    public IdentInAppMessageController(IdentInAppMessageService inAppMessageService) {
        this.inAppMessageService = inAppMessageService;
    }

    @Operation(description = "Get All InAppMessages")
    @GetMapping(path = InAppMessageRestApi.IN_APP_MESSAGE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<InAppMessageOut>> getAll(@Valid InAppMessagePageableFilter filter, @RequestParam(name = "include", required = false) String[] include,
                                                        BindingResult bindingResult, HttpServletRequest request) throws SystemException {
        UserContextDto contextDto = JwtUser.getAuthenticatedUser();
        filter.putUserId(contextDto.getId());
        return new ResponseEntity<>(inAppMessageService.getAll(filter, include), HttpStatus.OK);
    }

    @Operation(description = "Count InAppMessages")
    @GetMapping(path = InAppMessageRestApi.IN_APP_MESSAGE_COUNT, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Integer> count(@Valid InAppMessageFilter filter, BindingResult bindingResult,
                                         HttpServletRequest request) throws SystemException {
        UserContextDto contextDto = JwtUser.getAuthenticatedUser();
        filter.putUserId(contextDto.getId());
        return new ResponseEntity<>(inAppMessageService.count(filter), HttpStatus.OK);
    }

    @Operation(description = "Update One InAppMessage")
    @PutMapping(path = InAppMessageRestApi.IN_APP_MESSAGE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Boolean> update(@Valid @RequestBody List<Long> ids,
                                          BindingResult bindingResult, HttpServletRequest request) throws SystemException {
        UserContextDto contextDto = JwtUser.getAuthenticatedUser();
        return new ResponseEntity<>(inAppMessageService.updateAsRead(contextDto.getId(), ids), HttpStatus.OK);
    }
//    @Operation(description = "Get By Id InAppMessage")
//    @GetMapping(path = InAppMessageRestApi.IN_APP_MESSAGE_ID, produces = MediaType.APPLICATION_JSON_VALUE)
//    public ResponseEntity<InAppMessageOut> getById(@PathVariable("id") Long id, @RequestParam(name = "include", required = false) String[] include,
//                                                   HttpServletRequest request) throws SystemException {
//        return new ResponseEntity<>(inAppMessageService.getById(id, include), HttpStatus.OK);
//    }
}
