package com.books.application.config;

import com.books.application.startup.init.DBPropertyInitializerService;
import com.books.utility.config.model.ApplicationProperties;
import com.books.utility.system.exception.SystemException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

@Configuration("propertyInitializer")
public class PropertyInitializer {
    private final DBPropertyInitializerService dbInitializer;
    private final ObjectMapper objectMapper;

    @Autowired
    public PropertyInitializer(DBPropertyInitializerService dbInitializer,
                               ObjectMapper objectMapper) {
        this.dbInitializer = dbInitializer;
        this.objectMapper = objectMapper;
    }

    public ApplicationProperties initialGlobalProperties() throws SystemException {
        Object properties = dbInitializer.initialize();
        return objectMapper.convertValue(properties, ApplicationProperties.class);
    }
}
