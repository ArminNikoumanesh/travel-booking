package com.armin.security.authentication.filter;

import com.armin.utility.object.SystemException;
import org.springframework.security.core.AuthenticationException;

/**
 * Authentication Exception Class,
 * A Customized Authentication Exception for Spring Security
 *
 * @author : Armin.Nik
 * @project : shared
 * @date : 09.06.24
 */
public class JwtAuthenticationException extends AuthenticationException {
    private final SystemException systemException;

    public JwtAuthenticationException(SystemException systemException) {
        super("", systemException);
        this.systemException = systemException;
    }

    public SystemException getSystemException() {
        return systemException;
    }
}
