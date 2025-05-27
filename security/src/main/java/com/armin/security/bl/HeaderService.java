package com.armin.security.bl;

import com.armin.security.statics.constants.SecurityConstant;
import com.armin.utility.object.LogAnnotation;
import com.armin.utility.object.SystemError;
import com.armin.utility.object.SystemException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpServletRequest;

/**
 * Exceptions error code range: 2071-2080
 */
@Service
public class HeaderService {

    private final SecurityValidationService securityValidationService;

    @Autowired
    public HeaderService(SecurityValidationService securityValidationService) {
        this.securityValidationService = securityValidationService;
    }

    /* ****************************************************************************************************************** */

    @LogAnnotation
    public String extractAuthTokenClient(HttpServletRequest request) throws SystemException {
        if (request == null)
            throw new SystemException(SystemError.ILLEGAL_ARGUMENT, "request", 2071);

        return this.securityValidationService.validateAuthHeaderToken(request.getHeader(SecurityConstant.HEADER_TOKEN_KEY));
    }

    public String extractPublicTokenClient(HttpServletRequest request) throws SystemException {
        if (request == null)
            throw new SystemException(SystemError.ILLEGAL_ARGUMENT, "request", 2072);

        return this.securityValidationService.validatePublicHeaderToken(request.getHeader(SecurityConstant.HEADER_TOKEN_KEY));
    }

}
