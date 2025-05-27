package com.armin.application.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.introspect.VisibilityChecker;
import com.armin.database.cloud.ApplicationProperties;
import com.armin.infrastructure.config.init.DBPropertyInitializerService;
import com.armin.infrastructure.config.init.ELPropertyInitializerService;
import com.armin.infrastructure.config.init.FilePropertyInitializerService;
import com.armin.infrastructure.config.init.IPropertyInitializer;
import com.armin.infrastructure.config.init.constants.PropertiesFetchMode;
import com.armin.utility.object.SystemException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

@Configuration("propertyInitializer")
public class PropertyInitializer {
    private static final String TOPIC = "cloud_config_properties";
    private final FilePropertyInitializerService fileInitializer;
    private final DBPropertyInitializerService dbInitializer;
    private final ELPropertyInitializerService elInitializer;
//    private final KafkaTemplate<String, String> kafkaTemplate;
    private final BeanFactory beanFactory;
    private final ObjectMapper objectMapper;
    private final ModelMapper modelMapper;
    @Value("${spring.initialized.property.fetch.mode}")
    private PropertiesFetchMode fetchMode;
    @Value("${spring.application.name}")
    private String name;

    @Autowired
    public PropertyInitializer(FilePropertyInitializerService fileInitializer, DBPropertyInitializerService dbInitializer, ELPropertyInitializerService elInitializer,
                               BeanFactory beanFactory, ObjectMapper objectMapper, ModelMapper modelMapper) {
        this.fileInitializer = fileInitializer;
        this.dbInitializer = dbInitializer;
        this.elInitializer = elInitializer;
        this.beanFactory = beanFactory;
        this.objectMapper = objectMapper;
        this.objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        this.objectMapper.setVisibility(VisibilityChecker.Std.defaultInstance().withFieldVisibility(JsonAutoDetect.Visibility.ANY));
        this.modelMapper = modelMapper;
    }

    public ApplicationProperties initialGlobalProperties() throws SystemException, IOException {
        IPropertyInitializer propertyInitializer = (IPropertyInitializer) beanFactory.getBean(fetchMode.getValue());
        Object properties = propertyInitializer.initialize();
        return objectMapper.convertValue(properties, ApplicationProperties.class);
    }

    public void updateAllProperties(ApplicationProperties properties) throws IOException, SystemException {
        fileInitializer.updateProperties(properties);
        dbInitializer.updateProperties(properties);
//        elInitializer.updateProperties(properties);
        refreshProperties();
//        sendRefreshEvent();
    }

    public void refreshProperties() throws SystemException, IOException {
        ApplicationProperties applicationProperties = (ApplicationProperties) beanFactory.getBean("applicationProperties");
        modelMapper.map(initialGlobalProperties(), applicationProperties);
    }

//    public void sendRefreshEvent() {
//        kafkaTemplate.send(TOPIC, name);
//    }

}
