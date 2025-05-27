package com.armin.security.authentication.exception;

import com.armin.utility.object.ErrorResult;
import com.armin.utility.object.SystemError;
import com.armin.utility.object.SystemException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Authentication Failure Class,
 * Handling Requests after Authentication Failure
 *
 * @author : Armin.Nik
 * @project : shared
 * @date : 09.06.24
 */

/**
 * Exceptions error code range: 2001-2010
 */
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException exp) throws IOException {
        List<ErrorResult> results = new ArrayList<>();
        ErrorResult errorResult;
        if (request.getAttribute("token-expire") != null) {
            errorResult = new ErrorResult(new SystemException(SystemError.ACCESS_DENIED, "verification", 2098), response);
        } else {
            errorResult = new ErrorResult(new SystemException(SystemError.ACCESS_DENIED, "person", 2001), response);
        }
        results.add(errorResult);
        SecurityContextHolder.clearContext();
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getOutputStream().write(new ObjectMapper().writeValueAsBytes(results));
    }
}
