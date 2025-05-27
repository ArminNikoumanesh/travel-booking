package com.armin.security.authentication.filter;

import com.armin.security.bl.HeaderService;
import com.armin.security.bl.JwtService;
import com.armin.utility.object.UserContextDto;
import com.armin.security.model.object.JwtOutputAuthentication;
import com.armin.security.statics.constants.SecurityConstant;
import com.armin.utility.object.SystemError;
import com.armin.utility.object.SystemException;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AnonymousAuthenticationFilter;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;

@Component
public class IdentifiableAnonymousAuthenticationFilter extends AnonymousAuthenticationFilter {
    private final JwtService jwtService;
    private final HeaderService headerService;

    public IdentifiableAnonymousAuthenticationFilter(JwtService jwtService, HeaderService headerService) {
        super("identified.key");
        this.jwtService = jwtService;
        this.headerService = headerService;
    }

    @Override
    protected Authentication createAuthentication(HttpServletRequest request) {
        try {
            if (request == null)
                throw new SystemException(SystemError.ILLEGAL_ARGUMENT, "request", 2028);

            String authToken = this.headerService.extractPublicTokenClient(request);
            if (authToken == null)
                return new JwtOutputAuthentication(null);
            try {
                UserContextDto userContextDto = this.jwtService.extractUserWithSessionFromToken(authToken, SecurityConstant.ACCESS_TOKEN_SUBJECT);
                return new JwtOutputAuthentication(userContextDto);
            } catch (SystemException e) {
                UserContextDto userContextDto = this.jwtService.extractUserWithSessionFromToken(authToken, SecurityConstant.REFRESH_TOKEN_SUBJECT);
                return new JwtOutputAuthentication(userContextDto);
            }
        } catch (SystemException e) {
            if (request != null) {
                request.setAttribute("token-expire", true);
            }
        }
        return new JwtOutputAuthentication(null);
    }
}
