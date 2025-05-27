package com.armin.utility.config.cloud.sms;

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
public class SmsIn {

    private String message;
    private String to;

    public SmsIn(SmsIn smsIn) {
        this.message = smsIn.getMessage();
        this.to = smsIn.getTo();
    }
}
