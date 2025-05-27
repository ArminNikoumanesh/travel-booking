package com.armin.utility.config.cloud.sms;

import com.armin.utility.statics.enums.SmsType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author : Armin.Nik
 * @project : shared
 * @date : 09.06.24
 */
@Getter
@Setter
@NoArgsConstructor
public class SmsConfig {

    private String username;
    private String password;
    private String sourceNumber;
    private String domain;
    private SmsType smsType;
    private int priority;

    public SmsConfig(SmsConfig smsConfig) {
        this.username = smsConfig.getUsername();
        this.password = smsConfig.getPassword();
        this.sourceNumber = smsConfig.getSourceNumber();
        this.domain = smsConfig.getDomain();
        this.smsType = smsConfig.getSmsType();
        this.priority = smsConfig.getPriority();
    }
}
