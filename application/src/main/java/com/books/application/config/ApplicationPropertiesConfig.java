package com.books.application.config;

import com.books.utility.config.model.ApplicationProperties;
import com.books.utility.system.exception.SystemException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

@Configuration
public class ApplicationPropertiesConfig {
    private final PropertyInitializer propertyInitializer;

    @Autowired
    public ApplicationPropertiesConfig(PropertyInitializer propertyInitializer) {
        this.propertyInitializer = propertyInitializer;
    }

    @Bean
    public ApplicationProperties applicationProperties() throws SystemException, IOException {
        return propertyInitializer.initialGlobalProperties();
    }
}

