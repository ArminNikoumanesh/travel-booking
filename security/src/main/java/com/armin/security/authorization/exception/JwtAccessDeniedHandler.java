package com.armin.security.authorization.exception;

import com.armin.utility.object.ErrorResult;
import com.armin.utility.object.SystemError;
import com.armin.utility.object.SystemException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * Access Denied Handler Class,
 * Handling Requests after Authorization Failure
 *
 * @author : Armin.Nik
 * @project : shared
 * @date : 09.06.24
 */

/**
 * Exceptions error code range: 2011-2020
 */
@Component
public class JwtAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
                       AccessDeniedException exp) throws IOException {
        List<ErrorResult> results = new ArrayList<>();
        ErrorResult errorResult = new ErrorResult(new SystemException(SystemError.ACCESS_DENIED, "user", 2011), response);
        results.add(errorResult);

        SecurityContextHolder.clearContext();
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getOutputStream().write(new ObjectMapper().writeValueAsBytes(results));
    }
}
