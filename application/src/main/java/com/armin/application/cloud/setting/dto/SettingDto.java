package com.armin.application.cloud.setting.dto;

import com.armin.utility.config.cloud.identity.IdentitySettings;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SettingDto {
    private IdentitySettings identitySettings;
    private Boolean sandBoxSecurityConfig;
}
