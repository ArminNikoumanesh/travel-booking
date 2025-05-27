package com.armin.operation.security.ident.controller;

import com.armin.security.authentication.filter.JwtUser;
import com.armin.security.bl.AccessService;
import com.armin.security.statics.constants.SecurityRestApi;
import com.armin.operation.security.ident.model.dto.UserSessionFilter;
import com.armin.operation.security.ident.model.dto.UserSessionOut;
import com.armin.operation.security.ident.model.dto.UserSessionPageableFilter;
import com.armin.operation.security.ident.repository.service.IdentUserSessionService;
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

@Tag(name = "Ident User Session")
@RestController
@RequestMapping(value = {"${rest.identified}"})
@Validated
public class IdentUserSessionController {

    private final AccessService accessService;
    private final IdentUserSessionService identUserSessionService;

    @Autowired
    public IdentUserSessionController(IdentUserSessionService identUserSessionService, AccessService accessService) {
        this.identUserSessionService = identUserSessionService;
        this.accessService = accessService;
    }

    @Operation(description = "Get a model instance by {id} from the data source")
    @GetMapping(path = SecurityRestApi.SECURITY_SESSION_BY_ID, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserSessionOut> getById(@PathVariable("id") Integer id, HttpServletRequest httpServletRequest) throws SystemException {
        UserContextDto contextDto = JwtUser.getAuthenticatedUser();
        Integer sessionId = accessService.getSessionIdFromAccessToken(httpServletRequest);
        return new ResponseEntity<>(identUserSessionService.getById(contextDto.getId(), id, sessionId), HttpStatus.OK);
    }

    @Operation(description = "Count all user sessions matched by filter from the data source")
    @GetMapping(path = SecurityRestApi.SECURITY_ID_SESSION_COUNT, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Integer> count(@Valid UserSessionFilter filter, BindingResult bindingResult, HttpServletRequest httpServletRequest) throws SystemException {
        UserContextDto contextDto = JwtUser.getAuthenticatedUser();
        filter.setUserId(contextDto.getId());
        return new ResponseEntity<>(identUserSessionService.count(filter), HttpStatus.OK);
    }

    @Operation(description = "Get all user sessions matched by filter from the data source")
    @GetMapping(path = SecurityRestApi.SECURITY_ID_SESSIONS, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<UserSessionOut>> getAll(@Valid UserSessionPageableFilter filter, BindingResult bindingResult, @RequestParam(value = "include", required = false) String[] include, HttpServletRequest httpServletRequest) throws SystemException {
        UserContextDto contextDto = JwtUser.getAuthenticatedUser();
        Integer sessionId = accessService.getSessionIdFromAccessToken(httpServletRequest);
        return new ResponseEntity<>(identUserSessionService.getAll(contextDto.getId(), sessionId, filter, include), HttpStatus.OK);
    }

    @Operation(description = "Delete a model instance by {id} from the data source")
    @DeleteMapping(path = SecurityRestApi.SECURITY_SESSION_BY_ID, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Boolean> delete(@PathVariable("id") Integer id, HttpServletRequest httpServletRequest) throws SystemException {
        UserContextDto contextDto = JwtUser.getAuthenticatedUser();
        Integer sessionId = accessService.getSessionIdFromAccessToken(httpServletRequest);
        return new ResponseEntity<>(identUserSessionService.delete(contextDto.getId(), id, sessionId), HttpStatus.OK);
    }

    @Operation(description = "Delete a model instance by {id} from the data source")
    @DeleteMapping(path = SecurityRestApi.SECURITY_ID_SESSIONS, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Boolean> deleteAll(HttpServletRequest httpServletRequest) throws SystemException {
        UserContextDto contextDto = JwtUser.getAuthenticatedUser();
        Integer sessionId = accessService.getSessionIdFromAccessToken(httpServletRequest);
        return new ResponseEntity<>(identUserSessionService.deleteAll(sessionId, contextDto.getId()), HttpStatus.OK);
    }
}
