package com.armin.messaging.notification.bl;

import com.armin.utility.config.cloud.email.EmailConfig;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Component;

import java.util.Properties;

/**
 * @author : Armin.Nik
 * @project : shared
 * @date : 09.06.24
 */
@Component
public class EmailSenderConfig {

    public JavaMailSender getJavaMailSender(EmailConfig emailConfig) {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(emailConfig.getHost());
        mailSender.setPort(Integer.parseInt(emailConfig.getPort()));
        mailSender.setUsername(emailConfig.getUserName());
        mailSender.setPassword(emailConfig.getPassword());
        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", emailConfig.getProtocol());
        props.put("mail.smtp.auth", emailConfig.isAuth());
        props.put("mail.smtp.starttls.enable", emailConfig.isStarttls());
        props.put("mail.debug", "true");
        return mailSender;
    }

}
