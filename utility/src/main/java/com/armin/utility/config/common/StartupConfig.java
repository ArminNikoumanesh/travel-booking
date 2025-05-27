package com.armin.utility.config.common;

import com.armin.utility.file.bl.IFileService;
import com.armin.utility.file.model.object.Attachment;
import org.reflections.Reflections;
import org.reflections.scanners.FieldAnnotationsScanner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import java.lang.reflect.Field;
import java.util.Set;

@Component
public class StartupConfig {

    private final IFileService fileService;

    @Autowired
    public StartupConfig(IFileService fileService) {
        this.fileService = fileService;
    }

    @PostConstruct
    public void createProjectContainerBuckets() {
        Reflections reflections = new Reflections("com", new FieldAnnotationsScanner());

        Set<Field> ids = reflections.getFieldsAnnotatedWith(Attachment.class);
        for (Field field : ids) {
            Attachment attribute = field.getAnnotation(Attachment.class);
            if (attribute != null) {
                fileService.createContainer(attribute.container());
                fileService.createBucket(attribute.container(), attribute.bucket());
            }
        }
    }
}
