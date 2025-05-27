package com.armin.security.authentication.filter;

import com.armin.utility.object.UserContextDto;
import com.armin.utility.object.SystemError;
import com.armin.utility.object.SystemException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

public class JwtUser {

    public static UserContextDto getAuthenticatedUser() throws SystemException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.getPrincipal() instanceof UserContextDto) {
            return (UserContextDto) authentication.getPrincipal();
        }
        throw new SystemException(SystemError.TOKEN_VERIFICATION_EXPIRED, "authentication", 2233);
    }

    public static Optional<UserContextDto> getPublicUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.getPrincipal() instanceof UserContextDto) {
            return Optional.of((UserContextDto) authentication.getPrincipal());
        }
        return Optional.empty();
    }
}
