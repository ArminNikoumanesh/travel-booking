package com.armin.utility.config.cloud.email;

import com.armin.utility.statics.enums.EmailProtocolType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author : Armin.Nik
 * @project : shared
 * @date : 09.06.24
 */
@NoArgsConstructor
@Getter
@Setter
public class EmailConfig {

    private String userName;
    private String password;
    private String host;
    private String port;
    private EmailProtocolType protocol;
    private boolean auth;
    private boolean starttls;
    private int priority;

    public EmailConfig(EmailConfig emailConfig) {
        this.userName = emailConfig.getUserName();
        this.password = emailConfig.getPassword();
        this.host = emailConfig.getHost();
        this.port = emailConfig.getPort();
        this.protocol = emailConfig.getProtocol();
        this.auth = emailConfig.isAuth();
        this.starttls = emailConfig.isStarttls();
        this.priority = emailConfig.getPriority();
    }
}
