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
public class Amazon extends Notification<EmailConfig> {
    public Amazon(Amazon amazon) {
        super.setConfigs(amazon.getConfigs());
        super.setPriority(amazon.getPriority());
    }
}
