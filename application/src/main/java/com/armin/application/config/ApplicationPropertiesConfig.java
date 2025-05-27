package com.armin.application.config;

import com.armin.database.cloud.ApplicationProperties;
import com.armin.utility.object.SystemException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

@Configuration
public class ApplicationPropertiesConfig {
    private final BeanFactory beanFactory;

    @Autowired
    public ApplicationPropertiesConfig(BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    @Bean
    public ApplicationProperties applicationProperties() throws SystemException, IOException {
        PropertyInitializer propertyInitializer = (PropertyInitializer) beanFactory.getBean("propertyInitializer");
        return propertyInitializer.initialGlobalProperties();
    }
}
