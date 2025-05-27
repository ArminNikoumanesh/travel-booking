package com.armin.operation.security.admin.controller;

import com.armin.security.statics.constants.SecurityRestApi;
import com.armin.operation.security.admin.dto.session.UserSessionFilter;
import com.armin.operation.security.admin.dto.session.UserSessionOut;
import com.armin.operation.security.admin.dto.session.UserSessionPageableFilter;
import com.armin.operation.security.admin.repository.service.UserSessionService;
import com.armin.utility.object.SystemException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import java.util.List;

@Tag(name = "User Session")
@RestController
@RequestMapping(value = {"${rest.admin}"})
@Validated
public class UserSessionController {

    private final UserSessionService userSessionService;

    @Autowired
    public UserSessionController(UserSessionService userSessionService) {
        this.userSessionService = userSessionService;
    }

    @Operation(description = "Count all sessions matched by filter from the data source")
    @GetMapping(path = SecurityRestApi.SECURITY_ID_SESSION_COUNT, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Integer> count(UserSessionFilter filter, HttpServletRequest request) {
        return new ResponseEntity<>(this.userSessionService.count(filter), HttpStatus.OK);
    }

    @Operation(description = "Get all user's sessions matched by filter from the data source")
    @GetMapping(path = SecurityRestApi.SECURITY_ID_SESSIONS, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<UserSessionOut>> getAll(UserSessionPageableFilter filter, @RequestParam(value = "include", required = false) String[] include, HttpServletRequest request) {
        return new ResponseEntity<>(this.userSessionService.getAll(filter, include), HttpStatus.OK);
    }

    @Operation(description = "Get a model instance by {id} from the data source")
    @GetMapping(path = SecurityRestApi.SECURITY_SESSION_BY_ID, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserSessionOut> getById(@PathVariable Integer id, @RequestParam(value = "include", required = false) String[] include, HttpServletRequest request) throws SystemException {
        return new ResponseEntity<>(this.userSessionService.getById(id, include), HttpStatus.OK);
    }

    @Operation(description = "Delete a model instance by {id} from the data source")
    @DeleteMapping(path = SecurityRestApi.SECURITY_SESSION_BY_ID, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Boolean> delete(@PathVariable Integer id, HttpServletRequest request) {
        return new ResponseEntity<>(this.userSessionService.delete(id), HttpStatus.OK);
    }
}
