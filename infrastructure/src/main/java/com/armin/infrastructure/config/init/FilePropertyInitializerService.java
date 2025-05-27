package com.armin.infrastructure.config.init;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.introspect.VisibilityChecker;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.armin.infrastructure.config.init.constants.PropertyInitializeConstants;
import com.armin.utility.object.SystemError;
import com.armin.utility.object.SystemException;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;

/**
 * @author : Armin.Nik
 * @project : map-helm
 * @date : 09.10.22
 */
@Service("pr-init-file")
public class FilePropertyInitializerService implements IPropertyInitializer {
    private final ObjectMapper objectMapper;

    public FilePropertyInitializerService(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
        this.objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        this.objectMapper.setVisibility(VisibilityChecker.Std.defaultInstance().withFieldVisibility(JsonAutoDetect.Visibility.ANY));
    }

    @Override
    public Object initialize() throws SystemException {
        try {
            return objectMapper.readValue(
                    new ClassPathResource(PropertyInitializeConstants.JSON_CLASS_PATH).getInputStream(), Object.class);
        } catch (Exception e) {
            throw new SystemException(SystemError.DATA_NOT_FOUND, "No Way To Init Properties", 100001);
        }
    }

    @Override
    public void updateProperties(Object model) throws IOException {
        JsonNode property = objectMapper.valueToTree(model);
        ObjectNode node = (ObjectNode) new ObjectMapper().readTree(property.toString());
        objectMapper.writeValue(new File(PropertyInitializeConstants.JSON_FILE_PATH), node);
    }

    @Override
    public Object moveToNextRound() {
        return null;
    }
}
