package com.armin.messaging.notification.bl;

import com.armin.utility.config.cloud.BaseApplicationProperties;
import com.armin.utility.object.SystemException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

/**
 * @author : Armin.Nik
 * @project : shared
 * @date : 09.06.24
 */

/**
 * Exceptions error code range: 1131-1140
 */
@Service
public class AmazonEmailService implements IEmailService {

    private final BaseApplicationProperties applicationProperties;

    @Autowired
    public AmazonEmailService(BaseApplicationProperties applicationProperties, EmailSenderConfig emailSender) {
        this.applicationProperties = applicationProperties;
        this.emailSender = emailSender;
    }

    private final EmailSenderConfig emailSender;

    @Override
    public void sendEmail(String message, String receiverEmail) throws SystemException {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(receiverEmail);
//        mailMessage.setText(message);
//        try {
//
//            emailSender.getJavaMailSender(Objects.requireNonNull(applicationProperties.getEmailProvider().getAmazon().getConfigs().stream()
//                    .min(Comparator.comparing(EmailConfig::getPriority))
//                    .orElse(null))).send(mailMessage);
//        } catch (MailException exception) {
//            throw new SystemException(SystemError.EMAIL_SEND_FAILED, "problem sending Amazon mail", 1131, null);
//        }
    }

}
