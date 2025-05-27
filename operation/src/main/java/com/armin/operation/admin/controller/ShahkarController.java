package com.armin.operation.admin.controller;

import com.armin.security.authentication.filter.JwtUser;
import com.armin.operation.admin.model.dto.account.UserShahkarIn;
import com.armin.operation.admin.repository.service.ShahkarService;
import com.armin.operation.admin.statics.constants.ShahkarRestAPi;
import com.armin.utility.object.NoLogging;
import com.armin.utility.object.SystemException;
import com.armin.utility.object.UserContextDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@Tag(name = "Shahkar")
@RestController
@RequestMapping(value = {"${rest.identified}"})
@Validated
@NoLogging
public class ShahkarController {
    private final ShahkarService shahkarService;

    public ShahkarController(ShahkarService shahkarService) {
        this.shahkarService = shahkarService;
    }

    @Operation(description = "verify user identity")
    @PostMapping(path = ShahkarRestAPi.VERIFY_SHAHKAR, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Boolean> verify(@RequestBody @Valid UserShahkarIn model, BindingResult bindingResult,
                                          HttpServletRequest request) throws SystemException {
        UserContextDto contextDto = JwtUser.getAuthenticatedUser();
        return new ResponseEntity<>(shahkarService.verify(contextDto.getId(), model), HttpStatus.OK);
    }
}
