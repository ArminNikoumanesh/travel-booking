package com.armin.operation.admin.repository.service;

import com.armin.database.cloud.ApplicationProperties;
import com.armin.operation.admin.client.ReCaptchaClient;
import com.armin.operation.admin.model.dto.account.ReCaptchaOut;
import com.armin.utility.object.SystemError;
import com.armin.utility.object.SystemException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ReCaptchaService {

    private final ApplicationProperties applicationProperties;
    private final ReCaptchaClient reCaptchaClient;

    @Autowired
    public ReCaptchaService(ApplicationProperties applicationProperties, ReCaptchaClient reCaptchaClient) {
        this.applicationProperties = applicationProperties;
        this.reCaptchaClient = reCaptchaClient;
    }

    public boolean validate(String token) throws SystemException {
        if (!applicationProperties.getReCaptchaConfig().isActive()) {
            return true;
        }
        ReCaptchaOut reCaptchaOut;
        try {
            reCaptchaOut = reCaptchaClient.reCaptchaAssessment(applicationProperties.getReCaptchaConfig().getSecretKey(),
                    token, "{}");
        } catch (Exception exception) {
            log.error("error: ", exception);
            throw new SystemException(SystemError.TOKEN_VERIFICATION_FAILED, "validation Failed", 3060);
        }
        if (!reCaptchaOut.isSuccess()) {
            throw new SystemException(SystemError.ACCESS_DENIED, "User Authentication Failed", 3056);
        }
        return true;
    }
}
