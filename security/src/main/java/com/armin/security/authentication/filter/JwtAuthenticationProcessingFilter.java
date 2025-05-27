package com.armin.security.authentication.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.armin.security.bl.HeaderService;
import com.armin.security.model.object.JwtInputAuthentication;
import com.armin.security.statics.constants.SecurityConstant;
import com.armin.utility.object.ErrorResult;
import com.armin.utility.object.LogAnnotation;
import com.armin.utility.object.SystemException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.stereotype.Component;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Authentication and Authorization Processing Filter Class,
 * Main Class for Authentication and Authorization
 * which Handles Successful or Unsuccessful Situations
 *
 * @author : Armin.Nik
 * @date : 09.06.24
 */
@Component
public class JwtAuthenticationProcessingFilter extends AbstractAuthenticationProcessingFilter {

    private final HeaderService headerService;

    @Autowired
    public JwtAuthenticationProcessingFilter(JwtRequestMatcher jwtRequestMatcher, HeaderService headerService, AuthenticationConfiguration authenticationConfiguration, JwtAuthenticationProvider jwtAuthenticationProvider) throws Exception {
        super(jwtRequestMatcher, authenticationConfiguration.getAuthenticationManager());
        this.headerService = headerService;
    }

    @Override
    public  Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {
        try {
            String authTokenClient = this.headerService.extractAuthTokenClient(request);
            JwtInputAuthentication possibleToken = new JwtInputAuthentication(authTokenClient);
            return getAuthenticationManager().authenticate(possibleToken);
        } catch (SystemException e) {
            throw new JwtAuthenticationException(e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
                                            Authentication possibleUser) throws IOException, ServletException {
        request.setAttribute(SecurityConstant.REQUEST_EXTENDED_ATTRIBUTE, possibleUser.getPrincipal());
        SecurityContextHolder.getContext().setAuthentication(possibleUser);
        chain.doFilter(request, response);
    }

    @LogAnnotation
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                              AuthenticationException exception) throws IOException {
        JwtAuthenticationException exp = (JwtAuthenticationException) exception;

        ErrorResult errorResult = new ErrorResult(exp.getSystemException(), response);

        SecurityContextHolder.clearContext();
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getOutputStream().write(new ObjectMapper().writeValueAsBytes(errorResult));
    }
}
