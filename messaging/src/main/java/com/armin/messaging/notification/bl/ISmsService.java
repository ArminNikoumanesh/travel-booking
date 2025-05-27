package com.armin.messaging.notification.bl;

import com.armin.utility.statics.enums.SmsType;
import org.springframework.stereotype.Service;

@Service
public interface ISmsService {

    void sendUnitAsyncSms(String message, String receiverMobile, SmsType smsType);

}
