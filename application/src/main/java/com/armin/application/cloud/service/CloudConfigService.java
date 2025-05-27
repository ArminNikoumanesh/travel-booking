package com.armin.application.cloud.service;

import com.armin.application.cloud.model.security.AccountConfig;
import com.armin.application.config.PropertyInitializer;
import com.armin.database.cloud.ApplicationProperties;
import com.armin.utility.object.SystemException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class CloudConfigService {
    private final ApplicationProperties applicationProperties;
    private final ModelMapper modelMapper;
    private final PropertyInitializer propertyInitializer;

    @Autowired
    public CloudConfigService(ApplicationProperties applicationProperties, ModelMapper modelMapper,
                              PropertyInitializer propertyInitializer) {
        this.applicationProperties = applicationProperties;
        this.modelMapper = modelMapper;
        this.propertyInitializer = propertyInitializer;
    }

//    private void updateEventToKafka(ApplicationProperties model) throws SystemException {
//        GlobalConfigDto destination = new GlobalConfigDto(model);
//        String configInString = stringService.toPrettyJsonStringAdvanced(destination);
//        ConfigRequest configRequest = new ConfigRequest(application, profile, configInString);
//        kafkaTemplate.send(TOPIC, StringService.toJsonString(configRequest));
//    }

    //identity settings
    public AccountConfig getAccountConfigs() {
        return new AccountConfig(applicationProperties);
    }

    public AccountConfig updateAccountConfig(AccountConfig model, String ip) throws SystemException, IOException {
        modelMapper.map(model.getSignIn(), applicationProperties.getIdentitySettings().getSignIn());
        modelMapper.map(model.getRegistration(), applicationProperties.getIdentitySettings().getRegistration());
        modelMapper.map(model.getProfile(), applicationProperties.getIdentitySettings().getProfile());
        propertyInitializer.updateAllProperties(applicationProperties);
        return new AccountConfig(applicationProperties);
    }
}
