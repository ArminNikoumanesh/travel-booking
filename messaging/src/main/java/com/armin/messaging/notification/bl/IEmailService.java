package com.armin.messaging.notification.bl;

import com.armin.utility.object.SystemException;
import org.springframework.stereotype.Service;

@Service
public interface IEmailService {
    void sendEmail(String message, String receiverEmail) throws SystemException;
}
