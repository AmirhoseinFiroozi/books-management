package com.books.startup;

import com.books.utility.commons.annotations.Attachment;
import com.books.utility.file.service.IFileService;
import com.books.utility.system.exception.SystemException;
import jakarta.annotation.PostConstruct;
import org.reflections.Reflections;
import org.reflections.scanners.FieldAnnotationsScanner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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
    public void createProjectContainerBuckets() throws SystemException {
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
