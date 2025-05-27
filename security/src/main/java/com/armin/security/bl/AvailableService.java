package com.armin.security.bl;

import com.armin.utility.object.UserContextDto;
import com.armin.security.model.object.JwtClaim;
import com.armin.security.repository.UserBaseDao;
import com.armin.security.repository.UserSessionBaseDao;
import com.armin.utility.config.cloud.BaseApplicationProperties;
import com.armin.utility.object.SystemError;
import com.armin.utility.object.SystemException;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * Exceptions error code range: 2051-2070
 */
@Service
public class AvailableService {

    private final BaseApplicationProperties applicationProperties;
    private final UserBaseDao userBaseDao;
    private final UserSessionBaseDao userSessionBaseDao;

    @Autowired
    public AvailableService(BaseApplicationProperties applicationProperties, UserBaseDao userBaseDao, UserSessionBaseDao userSessionBaseDao) {
        this.applicationProperties = applicationProperties;
        this.userBaseDao = userBaseDao;
        this.userSessionBaseDao = userSessionBaseDao;
    }

    public Integer getSessionIdByAuthentication(Claims claims) throws SystemException {
        try {
            JwtClaim jwtClaim = JwtService.validateClaims(claims);
            UserContextDto sessionWithUser = this.userSessionBaseDao.getSessionWithUser(jwtClaim.getSessionId());
            if (sessionWithUser == null) {
                throw new SystemException(SystemError.SESSION_EXPIRED, "sessionId", 1010);
            }
            if (sessionWithUser.isSuspended()) {
                throw new SystemException(SystemError.USER_NOT_ACTIVE, "userId:" + sessionWithUser.getId(), 2052);
            }
            if (sessionWithUser.getLockExpired() != null && sessionWithUser.getLockExpired().isAfter(LocalDateTime.now())) {
                throw new SystemException(SystemError.USER_NOT_ACTIVE, "userId:" + sessionWithUser.getId(), 3002);
            }
            return jwtClaim.getSessionId();
        } catch (SystemException e) {
            throw new SystemException(SystemError.SESSION_EXPIRED, "", 2053);
        }
    }

    public UserContextDto getUserByAuthentication(Claims claims) throws SystemException {
        JwtClaim jwtClaim = JwtService.validateClaims(claims);
        UserContextDto sessionWithUser = this.userSessionBaseDao.getSessionWithUser(jwtClaim.getSessionId());
        if (sessionWithUser == null) {
            throw new SystemException(SystemError.SESSION_EXPIRED, "sessionId", 1010);
        }
        if (sessionWithUser.getId() == null)
            throw new SystemException(SystemError.USER_NOT_FOUND, "user", 2055);
        if (sessionWithUser.isSuspended())
            throw new SystemException(SystemError.USER_NOT_ACTIVE, "userId:" + sessionWithUser.getId(), 2056);
        if (sessionWithUser.getLockExpired() != null && sessionWithUser.getLockExpired().isAfter(LocalDateTime.now())) {
            throw new SystemException(SystemError.USER_NOT_ACTIVE, "userId:" + sessionWithUser.getId(), 3002);
        }

        return sessionWithUser;
    }

    public UserContextDto getConfirmedUserByAuthentication(Claims claims) throws SystemException {
        JwtClaim jwtClaim = JwtService.validateClaims(claims);
        UserContextDto sessionWithUser = this.userSessionBaseDao.getSessionWithUser(jwtClaim.getSessionId());
        if (sessionWithUser == null) {
            throw new SystemException(SystemError.SESSION_EXPIRED, "sessionId", 1010);
        }
        if (sessionWithUser.getId() == null) {
            throw new SystemException(SystemError.USER_NOT_FOUND, "user", 2055);
        }
        if (sessionWithUser.isSuspended()) {
            throw new SystemException(SystemError.USER_NOT_ACTIVE, "userId:" + sessionWithUser.getId(), 2056);
        }
        if (sessionWithUser.getLockExpired() != null && sessionWithUser.getLockExpired().isAfter(LocalDateTime.now())) {
            throw new SystemException(SystemError.USER_NOT_ACTIVE, "userId:" + sessionWithUser.getId(), 3002);
        }
        if (applicationProperties.getIdentitySettings().getSignIn().isRequireConfirmedMobile() && !sessionWithUser.isMobileConfirmed()) {
            throw new SystemException(SystemError.MOBILE_NOT_CONFIRM, "userId:" + sessionWithUser.getId(), 2057);
        }
        if (applicationProperties.getIdentitySettings().getSignIn().isRequireConfirmedEmail() && !sessionWithUser.isEmailConfirmed()) {
            throw new SystemException(SystemError.EMAIL_NOT_CONFIRM, "userId:" + sessionWithUser.getId(), 2058);
        }
        return sessionWithUser;
    }

    public UserContextDto getPublicUser(Claims claims) throws SystemException {
        JwtClaim jwtClaim = JwtService.validateClaims(claims);
        UserContextDto userContextDto = this.userBaseDao.getByAuthentication(jwtClaim.getUserId());
        if (userContextDto == null) {
            throw new SystemException(SystemError.USER_NOT_FOUND, "user", 2055);
        }
        return userContextDto;
    }

    /* ************************************************************************************************************** */

}
