package com.armin.application.cloud.setting.service;

import com.armin.application.cloud.setting.dto.SettingDto;
import com.armin.database.cloud.ApplicationProperties;
import com.armin.utility.config.cloud.identity.IdentitySettings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SettingService {
    private final ApplicationProperties applicationProperties;

    @Autowired
    public SettingService(ApplicationProperties applicationProperties) {
        this.applicationProperties = applicationProperties;
    }

    public SettingDto getApplicationSetting(String[] include) {
        SettingDto settingDto = new SettingDto();
        if (include == null) {
            return settingDto;
        }
        for (String settingInclude : include) {
            switch (settingInclude) {
                case "signIn":
                    if (settingDto.getIdentitySettings() == null) {
                        settingDto.setIdentitySettings(new IdentitySettings());
                    }
                    settingDto.getIdentitySettings().setSignIn(applicationProperties.getIdentitySettings().getSignIn());
                    break;
                case "profile":
                    if (settingDto.getIdentitySettings() == null) {
                        settingDto.setIdentitySettings(new IdentitySettings());
                    }
                    settingDto.getIdentitySettings().setProfile(applicationProperties.getIdentitySettings().getProfile());
                    settingDto.getIdentitySettings().getProfile().validateDetails();
                    break;
                case "registration":
                    if (settingDto.getIdentitySettings() == null) {
                        settingDto.setIdentitySettings(new IdentitySettings());
                    }
                    settingDto.getIdentitySettings().setRegistration(applicationProperties.getIdentitySettings().getRegistration());
                    settingDto.getIdentitySettings().getRegistration().validateDetails();
                    break;
                case "password":
                    if (settingDto.getIdentitySettings() == null) {
                        settingDto.setIdentitySettings(new IdentitySettings());
                    }
                    settingDto.getIdentitySettings().setPassword(applicationProperties.getIdentitySettings().getPassword());
                    break;
                default:
                    break;
            }
        }
        return settingDto;
    }
}
