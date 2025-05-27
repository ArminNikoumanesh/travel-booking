package com.armin.utility.config.cloud.sms;

import com.armin.utility.config.cloud.Notificaiton.Notification;
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
public class Magfa extends Notification<SmsConfig> {
    public Magfa(Magfa magfa) {
        super.setConfigs(magfa.getConfigs());
        super.setPriority(magfa.getPriority());
    }
}
