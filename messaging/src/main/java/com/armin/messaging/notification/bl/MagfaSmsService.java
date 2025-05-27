package com.armin.messaging.notification.bl;

import com.armin.utility.config.cloud.BaseApplicationProperties;
import com.armin.utility.config.cloud.sms.SmsConfig;
import com.armin.utility.config.cloud.sms.SmsProvider;
import com.armin.utility.object.SystemError;
import com.armin.utility.object.SystemException;
import com.armin.utility.statics.enums.SmsType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Comparator;

/**
 * @author : Armin.Nik
 * @project : shared
 * @date : 09.06.24
 */

/**
 * Exceptions error code range: 1121-1130
 */


@Service
public class MagfaSmsService implements ISmsService {

    private static final String magfaUrl = "https://sms.magfa.com/magfaHttpService?service=Enqueue";

    private final BaseApplicationProperties applicationProperties;

    @Autowired
    public MagfaSmsService(BaseApplicationProperties applicationProperties) {
        this.applicationProperties = applicationProperties;
    }

    @Override
    public void sendUnitAsyncSms(String message, String receiverMobile, SmsType smsType) {

        SmsProvider smsProvider = applicationProperties.getSmsProviders();

        SmsConfig smsConfig = smsProvider.getMagfa().getConfigs().stream()
                .filter(x -> smsType.equals(x.getSmsType()))
                .min(Comparator.comparing(SmsConfig::getPriority))
                .orElse(null);

        StringBuilder uniCastSms = new StringBuilder(magfaUrl);
        uniCastSms.append("&username=");
        uniCastSms.append(smsConfig.getUsername());
        uniCastSms.append("&password=");
        uniCastSms.append(smsConfig.getPassword());
        uniCastSms.append("&from=");
        uniCastSms.append(smsConfig.getSourceNumber());
        uniCastSms.append("&domain=");
        uniCastSms.append(smsConfig.getDomain());
        uniCastSms.append("&message=");
        uniCastSms.append(message);
        uniCastSms.append("&to=");
        uniCastSms.append(receiverMobile);

        try {
            RestTemplate restTemplate = new RestTemplate();
            String response = restTemplate.getForObject(uniCastSms.toString(), String.class);
            if (response != null && response.length() <= 3)
                throw new SystemException(SystemError.SMS_NOT_SENT, "SMS", 1121);
        } catch (RestClientException | SystemException rs) {
        }
    }

}
