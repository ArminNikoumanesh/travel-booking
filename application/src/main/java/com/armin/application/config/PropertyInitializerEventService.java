package com.armin.application.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class PropertyInitializerEventService {
    private final PropertyInitializer propertyInitializer;
    @Value("${spring.application.name}")
    private String name;

    @Autowired
    public PropertyInitializerEventService(PropertyInitializer propertyInitializer) {
        this.propertyInitializer = propertyInitializer;
    }

//    @KafkaListener(topics = "cloud_config_properties", groupId = "#{T(java.util.UUID).randomUUID().toString()}")
//    public void storePlaceChanges(String message) throws IOException, SystemException {
//        if (message != null && !message.equals("")) {
//            if (message.equals(name)) propertyInitializer.refreshProperties();
//        }
//    }
}
