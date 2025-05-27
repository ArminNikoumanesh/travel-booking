package com.armin.infrastructure.config.init;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.introspect.VisibilityChecker;
import com.armin.infrastructure.config.init.constants.PropertyInitializeConstants;
import com.armin.infrastructure.config.init.model.ELPropertiesModel;
import com.armin.utility.object.SystemException;
import com.armin.utility.repository.odm.dao.ElasticsearchDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author : Armin.Nik
 * @project : map-helm
 * @date : 09.10.22
 */
@Service("pr-init-el")
public class ELPropertyInitializerService implements IPropertyInitializer {
    private final DBPropertyInitializerService dbPropertyInitializerService;
    private final ElasticsearchDao elasticsearchDao;
    private final ObjectMapper objectMapper;
    @Value("${spring.application.name}")
    private String name;
    @Value("${spring.cloud.config.profile}")
    private String profile;

    @Autowired
    public ELPropertyInitializerService(DBPropertyInitializerService dbPropertyInitializerService, ElasticsearchDao elasticsearchDao,
                                        ObjectMapper objectMapper) {
        this.dbPropertyInitializerService = dbPropertyInitializerService;
        this.elasticsearchDao = elasticsearchDao;
        this.objectMapper = objectMapper;
        this.objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        this.objectMapper.setVisibility(VisibilityChecker.Std.defaultInstance().withFieldVisibility(JsonAutoDetect.Visibility.ANY));
    }

    @Override
    public Object initialize() throws SystemException {
        Map<String, Object> properties;
        try {
            properties = elasticsearchDao.getById(PropertyInitializeConstants.EL_INDEX_CONFIG_FILE, returnId(), Map.class);
        } catch (Exception e) {
            return moveToNextRound();
        }
        return properties.get("property");
    }

    @Override
    public void updateProperties(Object model) {
        try {
            elasticsearchDao.update(PropertyInitializeConstants.EL_INDEX_CONFIG_FILE, returnId(),
                    objectMapper.valueToTree(new ELPropertiesModel(model)));
        } catch (SystemException ignored) {
        }
    }

    // In future we will set priority dynamically
    @Override
    public Object moveToNextRound() throws SystemException {
        return dbPropertyInitializerService.initialize();
    }

    private String returnId() {
        return this.name + ":" + this.profile;
    }

}
