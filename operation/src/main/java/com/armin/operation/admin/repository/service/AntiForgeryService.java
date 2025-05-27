package com.armin.operation.admin.repository.service;

import com.armin.database.cloud.ApplicationProperties;
import com.armin.operation.admin.model.dto.account.SecurityConfigOut;
import com.armin.utility.bl.HashService;
import com.armin.utility.object.SystemError;
import com.armin.utility.object.SystemException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;

@Slf4j
@Service
public class AntiForgeryService {
    private final RedisTemplate<String, LocalDateTime> redisTemplate;
    private final HashService hashService;
    private final ReCaptchaService reCaptchaService;
    private final ApplicationProperties applicationProperties;
    private final String PREFIX = "RT-";

    @Autowired
    public AntiForgeryService(RedisTemplate<String, LocalDateTime> redisTemplate,
                              ReCaptchaService reCaptchaService, ApplicationProperties applicationProperties, HashService hashService) {
        this.redisTemplate = redisTemplate;
        this.reCaptchaService = reCaptchaService;
        this.applicationProperties = applicationProperties;
        this.hashService = hashService;
    }

    public SecurityConfigOut generate(String key) throws SystemException {
        String hashedKey = PREFIX + hashService.encrypt(key);
        if (redisTemplate.opsForValue().get(hashedKey) != null) {
            throw new SystemException(SystemError.ILLEGAL_REQUEST, "key hasn't been expired yet", 3061);
        }
        SecurityConfigOut result;
        try {
            result = new SecurityConfigOut(BlowfishCryptoService.encrypt(hashedKey));
        } catch (Exception exception) {
            log.error(hashedKey, exception);
            throw new SystemException(SystemError.INVALID_DATA_TYPE, "crypto service failed", 3062);
        }
        redisTemplate.opsForValue().set(hashedKey, LocalDateTime.now(), Duration.ofMinutes(1));
        return result;
    }

    public boolean validate(String token) throws SystemException {
        if (!applicationProperties.getReCaptchaConfig().isActive()) {
            return true;
        }
        if (authenticateToken(token)) {
            return reCaptchaService.validate(token);
        }
        if (redisTemplate.opsForValue().get(token) == null) {
            throw new SystemException(SystemError.ACCESS_DENIED, "User Authentication Failed", 3058);
        }
        redisTemplate.delete(token);
        return true;
    }

    public boolean authenticateToken(String key) {
        return !key.startsWith(PREFIX);
    }
}
