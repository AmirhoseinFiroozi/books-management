package com.books.application.startup;

import com.books.application.startup.init.IPropertyInitializer;
import com.books.utility.config.model.ApplicationProperties;
import com.books.utility.system.exception.SystemException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationPropertiesConfig {
    private final BeanFactory beanFactory;
    private final ObjectMapper objectMapper;

    @Autowired
    public ApplicationPropertiesConfig(BeanFactory beanFactory, ObjectMapper objectMapper) {
        this.beanFactory = beanFactory;
        this.objectMapper = objectMapper;
    }

    @Bean
    public ApplicationProperties applicationProperties() throws SystemException {
        IPropertyInitializer propertyInitializer = (IPropertyInitializer) beanFactory.getBean("pr-init-db");
        Object properties = propertyInitializer.initialize();
        return objectMapper.convertValue(properties, ApplicationProperties.class);
    }
}
