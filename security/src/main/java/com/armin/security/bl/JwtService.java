package com.armin.security.bl;

import com.armin.utility.object.UserContextDto;
import com.armin.security.model.object.JwtClaim;
import com.armin.security.model.object.SecurityAccessRule;
import com.armin.security.repository.RestBaseDao;
import com.armin.security.statics.constants.ClientType;
import com.armin.security.statics.constants.SecurityConstant;
import com.armin.utility.bl.ValidationEngine;
import com.armin.utility.config.cloud.BaseApplicationProperties;
import com.armin.utility.object.SystemError;
import com.armin.utility.object.SystemException;
import com.armin.utility.object.TokenInfo;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * The Json Web Token (JWT) Service Class,
 * Containing Methods about JWt Management
 *
 * @author : Armin.Nik
 * @date : 09.06.24
 */

/**
 * Exceptions error code range: 2081-2100
 */
@Service
public class JwtService {
    private static final String CLAIM_USER_ID = "uid";
    private static final String CLAIM_SESSION_ID = "sid";

    private final RestBaseDao restBaseDao;
    private final AvailableService availableService;
    private final BaseApplicationProperties applicationProperties;
    private final SecretKey key;

    @Autowired
    public JwtService(RestBaseDao restBaseDao, AvailableService availableService, BaseApplicationProperties applicationProperties) {
        this.restBaseDao = restBaseDao;
        this.availableService = availableService;
        this.applicationProperties = applicationProperties;
//        this.key = Keys.hmacShaKeyFor(applicationProperties.getIdentitySettings().getJwt().getSecurityKey().getBytes(StandardCharsets.UTF_8));
        this.key = Keys.hmacShaKeyFor("v8y/B?E(H+MbPeShVmYq3t6w9z$C&F)J@NcRfTjWnZr4u7x!A%D*G-KaPdSgVkXp".getBytes(StandardCharsets.UTF_8));
    }

    /* ****************************************************************************************************************** */

    public static JwtClaim validateClaims(Claims claims) throws SystemException {
        Integer claimUserId;
        Integer claimSessionId;
        try {
            claimUserId = ValidationEngine.validateInteger(claims.get(JwtService.CLAIM_USER_ID).toString());
            claimSessionId = ValidationEngine.validateInteger(claims.get(JwtService.CLAIM_SESSION_ID).toString());
        } catch (SystemException e) {
            throw new SystemException(SystemError.TOKEN_VERIFICATION_FAILED, "claims:" + claims, 2090);
        } catch (NullPointerException | ClassCastException e) {
            throw new SystemException(SystemError.TOKEN_VERIFICATION_FAILED, "claims:" + claims, 2091);
        }
        return new JwtClaim(claimUserId, claimSessionId);
    }

    public TokenInfo create(Integer userId, Integer sessionId, ClientType clientType) throws SystemException {
        ClaimsBuilder claimsBuilder = Jwts.claims();
        claimsBuilder.add(JwtService.CLAIM_USER_ID, userId);
        claimsBuilder.add(JwtService.CLAIM_SESSION_ID, sessionId);
        Claims claims = claimsBuilder.build();

        Long accessTokenExpirationTime = this.extractAccessTokenExpirationTime(clientType);
        Long refreshTokenExpirationTime = this.extractRefreshTokenExpirationTime(clientType);
        String access = this.buildToken(claims, SecurityConstant.ACCESS_TOKEN_SUBJECT, accessTokenExpirationTime);
        String refresh = this.buildToken(claims, SecurityConstant.REFRESH_TOKEN_SUBJECT, refreshTokenExpirationTime);
        return new TokenInfo(access, refresh, accessTokenExpirationTime, refreshTokenExpirationTime);
    }

    public TokenInfo refresh(String token, ClientType clientType) throws SystemException {
        Claims claims = this.extractClaims(token, SecurityConstant.REFRESH_TOKEN_SUBJECT);

        Long accessTokenExpirationTime = this.extractAccessTokenExpirationTime(clientType);
        Long refreshTokenExpirationTime = this.extractRefreshTokenExpirationTime(clientType);

        String access = this.buildToken(claims, SecurityConstant.ACCESS_TOKEN_SUBJECT, accessTokenExpirationTime);
        String refresh = this.buildToken(claims, SecurityConstant.REFRESH_TOKEN_SUBJECT, refreshTokenExpirationTime);
        return new TokenInfo(access, refresh, accessTokenExpirationTime, refreshTokenExpirationTime);
    }

    /**
     * Extract Username from Access Token
     * <br />Possible {@link SystemException} in Function Call of {@link JwtService#extractClaims(String, String)}
     *
     * @param token A {@link String} Instance Representing Access token
     * @return A {@link String} Instance Representing Username of Verified Token
     */
    public UserContextDto extractPublicUserFromToken(String token, String tokenType) throws SystemException {
        Claims claims = this.extractClaims(token, tokenType);
        return this.availableService.getPublicUser(claims);
    }

    public UserContextDto extractUserWithSessionFromToken(String token, String tokenType) throws SystemException {
        Claims claims = this.extractClaims(token, tokenType);
        return this.availableService.getUserByAuthentication(claims);
    }

    public UserContextDto extractConfirmedUserFromToken(String token, String tokenType) throws SystemException {
        Claims claims = this.extractClaims(token, tokenType);
        return this.availableService.getConfirmedUserByAuthentication(claims);
    }

    public Integer extractSessionIdFromRefreshToken(String token) throws SystemException {
        Claims claims = this.extractClaims(token, SecurityConstant.REFRESH_TOKEN_SUBJECT);
        return this.availableService.getSessionIdByAuthentication(claims);
    }

    public Integer extractSessionIdFromAccessToken(String token) throws SystemException {
        Claims claims = this.extractClaims(token, SecurityConstant.ACCESS_TOKEN_SUBJECT);
        return this.availableService.getSessionIdByAuthentication(claims);
    }

    /* ****************************************************************************************************************** */

    public List<SecurityAccessRule> generateAccessRule() throws SystemException {
        List<SecurityAccessRule> accessRules = this.restBaseDao.listRestsWithPermissions();
        if (accessRules.isEmpty())
            throw new SystemException(SystemError.DATA_NOT_FOUND, "restEntities", 2081);
        return accessRules;
    }

    /**
     * Build a Token Based on Requested Claims
     *
     * @param claims            A {@link Map} Instance Representing Claims of Json Web Token
     * @param subject           A {@link String} Instance Representing Subject of Json Web Token
     * @param expirationMinutes An {@link int} Instance Representing Expiration Hours of Json Web Token
     * @return A {@link String} Instance Representing Requested Token
     * @throws SystemException A Customized {@link RuntimeException} with type of {@link SystemError#TOKEN_CREATION_FAILED} when Building Token Failed
     */
    private String buildToken(Claims claims, String subject, Long expirationMinutes) throws SystemException {
        try {
            String tokenSecret = applicationProperties.getIdentitySettings().getJwt().getSecurityKey();
            if (tokenSecret == null)
                throw new SystemException(SystemError.DATA_NOT_FOUND, "tokenSecretKey", 2082);

            Timestamp now = Timestamp.valueOf(LocalDateTime.now());
            Timestamp expiration = Timestamp.valueOf(LocalDateTime.now().plusMinutes(expirationMinutes));
            return Jwts.builder()
                    .setHeaderParam("typ", "JWT")
                    .setClaims(claims)
                    .setIssuer(SecurityConstant.TOKEN_ISSUER)
                    .setSubject(subject)
                    .setExpiration(expiration)
                    .setIssuedAt(now)
                    .setId(UUID.randomUUID().toString())
                    .compressWith(CompressionCodecs.GZIP)
                    .signWith(key, SignatureAlgorithm.HS512)
                    .compact();
        } catch (RuntimeException e) {
            throw new SystemException(SystemError.TOKEN_CREATION_FAILED, "claims:" + claims + ",subject:" + subject, 2083);
        }
    }

    /**
     * Extract Claims of Input Token
     *
     * @param token A {@link String} Instance Representing Json Web Token
     * @return A {@link Map} Instance Representing Claims of Input Token
     * @throws SystemException A Customized {@link RuntimeException} with type of {@link SystemError#} when Extracting Token Failed
     */
    private Claims extractClaims(String token, String subject) throws SystemException {
        try {
            String secretKey = applicationProperties.getIdentitySettings().getJwt().getSecurityKey();
            if (secretKey == null)
                throw new SystemException(SystemError.DATA_NOT_FOUND, "tokenSecretKey", 2085);
            return Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload();
        } catch (MissingClaimException | IncorrectClaimException e) {
            throw new SystemException(SystemError.TOKEN_VERIFICATION_INVALID_TYPE, "token:" + token + ",subject:" + subject, 2086);
        } catch (ExpiredJwtException e) {
            if (subject.equals(SecurityConstant.REFRESH_TOKEN_SUBJECT)) {
                throw new SystemException(SystemError.REFRESH_TOKEN_VERIFICATION_EXPIRED, "subject:" + subject, 2087);
            } else {
                throw new SystemException(SystemError.TOKEN_VERIFICATION_EXPIRED, "subject:" + subject, 2088);
            }
        } catch (RuntimeException e) {
            throw new SystemException(SystemError.TOKEN_VERIFICATION_FAILED, "token:" + token + ",subject:" + subject, 2089);
        }
    }

    private Long extractAccessTokenExpirationTime(ClientType clientType) {
        if (clientType == ClientType.WEB) {
            return applicationProperties.getIdentitySettings().getJwt().getBrowserAccessTokenExpireMin();
        } else {
            return applicationProperties.getIdentitySettings().getJwt().getMobileAccessTokenExpireMin();
        }
    }

    private Long extractRefreshTokenExpirationTime(ClientType clientType) {
        if (clientType == ClientType.WEB) {
            return applicationProperties.getIdentitySettings().getJwt().getBrowserRefreshTokenExpireMin();
        } else {
            return applicationProperties.getIdentitySettings().getJwt().getMobileRefreshTokenExpireMin();
        }
    }
}
