package com.armin.security.bl;

import com.armin.utility.object.UserContextDto;
import com.armin.security.statics.constants.SecurityConstant;
import com.armin.utility.object.SystemError;
import com.armin.utility.object.SystemException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpServletRequest;

/**
 * Exceptions error code range: 2021-2050
 */

@Service
public class AccessService {
    private final HeaderService headerService;
    private final JwtService jwtService;

    @Autowired
    public AccessService(HeaderService headerService, JwtService jwtService) {
        this.headerService = headerService;
        this.jwtService = jwtService;
    }

    /* ****************************************************************************************************************** */

    public UserContextDto getLoggedInUser(HttpServletRequest request) throws SystemException {
        if (request == null)
            throw new SystemException(SystemError.ILLEGAL_ARGUMENT, "request", 2021);
        Object object = request.getAttribute(SecurityConstant.REQUEST_EXTENDED_ATTRIBUTE);
        if (object == null)
            throw new SystemException(SystemError.ILLEGAL_ARGUMENT, "object", 2022);
        if (!(object instanceof UserContextDto))
            throw new SystemException(SystemError.ILLEGAL_ARGUMENT, "object", 2023);
        return (UserContextDto) request.getAttribute(SecurityConstant.REQUEST_EXTENDED_ATTRIBUTE);
    }

    public String getAuthenticatedToken(HttpServletRequest request) throws SystemException {
        if (request == null)
            throw new SystemException(SystemError.ILLEGAL_ARGUMENT, "request", 2025);

        return this.headerService.extractAuthTokenClient(request);
    }

    public UserContextDto getAuthenticatedUserFromAccessToken(HttpServletRequest request) throws SystemException {
        if (request == null)
            throw new SystemException(SystemError.ILLEGAL_ARGUMENT, "request", 2028);

        String authToken = this.headerService.extractAuthTokenClient(request);
        return this.jwtService.extractConfirmedUserFromToken(authToken, SecurityConstant.ACCESS_TOKEN_SUBJECT);
    }

    public UserContextDto getAuthenticatedUserFromRefreshToken(HttpServletRequest request) throws SystemException {
        if (request == null)
            throw new SystemException(SystemError.ILLEGAL_ARGUMENT, "request", 2030);

        String authToken = this.headerService.extractAuthTokenClient(request);
        return this.jwtService.extractConfirmedUserFromToken(authToken, SecurityConstant.REFRESH_TOKEN_SUBJECT);
    }

    public Integer getSessionIdFromAccessToken(HttpServletRequest request) throws SystemException {
        if (request == null)
            throw new SystemException(SystemError.ILLEGAL_ARGUMENT, "request", 2031);

        String authToken = this.headerService.extractAuthTokenClient(request);
        return this.jwtService.extractSessionIdFromAccessToken(authToken);
    }

    public Integer getSessionIdFromRefreshToken(HttpServletRequest request) throws SystemException {
        if (request == null)
            throw new SystemException(SystemError.ILLEGAL_ARGUMENT, "request", 2032);

        String authToken = this.headerService.extractAuthTokenClient(request);
        return this.jwtService.extractSessionIdFromRefreshToken(authToken);
    }

    public UserContextDto getPublicUser(HttpServletRequest request) throws SystemException {
        if (request == null)
            throw new SystemException(SystemError.ILLEGAL_ARGUMENT, "request", 2033);

        UserContextDto userContextDto = null;
        try {
            String authToken = this.headerService.extractPublicTokenClient(request);
            if (authToken != null)
                userContextDto = this.jwtService.extractPublicUserFromToken(authToken, SecurityConstant.ACCESS_TOKEN_SUBJECT);
        } catch (SystemException ignored) {
        }
        return userContextDto;
    }

    /* ****************************************************************************************************************** */
}
