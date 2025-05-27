package com.armin.application.cloud.model;

import com.armin.database.cloud.ApplicationProperties;
import com.armin.utility.config.cloud.email.EmailProvider;
import com.armin.utility.config.cloud.file.FileCrud;
import com.armin.utility.config.cloud.identity.IdentitySettings;
import com.armin.utility.config.cloud.sms.SmsProvider;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class GlobalConfigDto {
    private boolean initDB;
    private IdentitySettings identitySettings;
    private Integer delayMinutes;
    private SmsProvider smsProviders;
    private EmailProvider emailProvider;
    private Boolean smsOtpSandbox;
    private FileCrud fileCrud;

    public GlobalConfigDto(ApplicationProperties properties) {
        this.initDB = properties.isInitDB();
        this.identitySettings = properties.getIdentitySettings();
        this.smsProviders = properties.getSmsProviders();
        this.emailProvider = properties.getEmailProvider();
        this.smsOtpSandbox = properties.getSmsOtpSandbox();
        this.fileCrud = properties.getFileCrud();
    }
}
