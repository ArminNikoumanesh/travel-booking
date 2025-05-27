package com.armin.security.authentication.filter;

import com.armin.security.bl.JwtService;
import com.armin.utility.object.UserContextDto;
import com.armin.security.model.object.JwtInputAuthentication;
import com.armin.security.model.object.JwtOutputAuthentication;
import com.armin.security.statics.constants.SecurityConstant;
import com.armin.utility.object.SystemException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

/**
 * Authentication Provider Class,
 * Process Authentication in Spring Security
 *
 * @author : Armin.Nik
 * @project : shared
 * @date : 09.06.24
 */
@Component
public class JwtAuthenticationProvider implements AuthenticationProvider {

    private final JwtService jwtService;

    @Autowired
    public JwtAuthenticationProvider(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        try {
            UserContextDto userContextDto = this.jwtService.extractConfirmedUserFromToken((String) authentication.getPrincipal(), SecurityConstant.ACCESS_TOKEN_SUBJECT);
            return new JwtOutputAuthentication(userContextDto);
        } catch (SystemException e) {
            throw new JwtAuthenticationException(e);
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return JwtInputAuthentication.class.equals(authentication);
    }
}
