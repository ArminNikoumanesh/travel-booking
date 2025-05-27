package com.armin.messaging.notification.dto;

import com.armin.utility.statics.enums.SmsType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@NoArgsConstructor
public class NotificationElasticLog {
    private String message;
    private String mobile;
    private String email;
    private SmsType smsType;
    private LocalDateTime created = LocalDateTime.now();

    public NotificationElasticLog(Notification model, String message) {
        this.mobile = model.getMobile();
        this.email = model.getEmail();
        this.smsType = model.getSmsType();
        this.message = message;
    }
}
