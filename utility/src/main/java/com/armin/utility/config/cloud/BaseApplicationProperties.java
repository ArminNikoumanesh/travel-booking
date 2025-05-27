package com.armin.utility.config.cloud;

import com.armin.utility.config.cloud.email.EmailProvider;
import com.armin.utility.config.cloud.minio.MinioConfig;
import com.armin.utility.config.cloud.sms.SmsProvider;
import com.armin.utility.config.cloud.file.FileCrud;
import com.armin.utility.config.cloud.identity.IdentitySettings;
import com.armin.utility.config.cloud.oap.OpenApiConfig;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class BaseApplicationProperties {
    private boolean initDB;
    private IdentitySettings identitySettings;
    private SmsProvider smsProviders;
    private EmailProvider emailProvider;
    private Boolean smsOtpSandbox;
    private FileCrud fileCrud;
    private List<String> widgetAreas;
    private OpenApiConfig openApiConfig;
    private MinioConfig minioConfig;
}
