package com.armin.infrastructure.config.init;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.introspect.VisibilityChecker;
import com.armin.database.global.entity.GlobalPropertyEntity;
import com.armin.database.global.repository.service.BaseGlobalPropertyService;
import com.armin.utility.object.SystemException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * @author : Armin.Nik
 */
@Service("pr-init-db")
public class DBPropertyInitializerService implements IPropertyInitializer {
    private final ObjectMapper objectMapper;
    private final FilePropertyInitializerService filePropertyInitializerService;
    private final BaseGlobalPropertyService baseGlobalPropertyService;
    @Value("${spring.application.name}")
    private String name;
    @Value("${spring.cloud.config.profile}")
    private String profile;

    @Autowired
    public DBPropertyInitializerService(ObjectMapper objectMapper,
                                        FilePropertyInitializerService filePropertyInitializerService, BaseGlobalPropertyService baseGlobalPropertyService) {
        this.objectMapper = objectMapper;
        this.filePropertyInitializerService = filePropertyInitializerService;
        this.objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        this.objectMapper.setVisibility(VisibilityChecker.Std.defaultInstance().withFieldVisibility(JsonAutoDetect.Visibility.ANY));
        this.baseGlobalPropertyService = baseGlobalPropertyService;
    }

    @Override
    public Object initialize() throws SystemException {
        Object properties = new Object();
        try {
            properties = baseGlobalPropertyService.getById(name, profile).getProperty();
        } catch (Exception e) {
            properties = moveToNextRound();
        }
        return properties;
    }

    @Override
    public void updateProperties(Object model) {
        GlobalPropertyEntity entity = baseGlobalPropertyService.getById(name, profile);
        entity.setProperty(objectMapper.valueToTree(model));
        baseGlobalPropertyService.updateEntity(entity);
    }

    @Override
    public Object moveToNextRound() throws SystemException {
        return filePropertyInitializerService.initialize();
    }
}
