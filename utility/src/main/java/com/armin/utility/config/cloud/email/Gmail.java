package com.armin.utility.config.cloud.email;

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
public class Gmail extends Notification<EmailConfig> {
    public Gmail(Gmail gmail) {
        super.setConfigs(gmail.getConfigs());
        super.setPriority(gmail.getPriority());
    }
}
