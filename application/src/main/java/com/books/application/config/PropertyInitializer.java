package com.books.application.config;

import com.books.application.startup.init.DBPropertyInitializerService;
import com.books.utility.config.model.ApplicationProperties;
import com.books.utility.system.exception.SystemException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaTemplate;

import java.io.IOException;

@Configuration("propertyInitializer")
public class PropertyInitializer {
    private final DBPropertyInitializerService dbInitializer;
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final BeanFactory beanFactory;
    private final ObjectMapper objectMapper;
    private final ModelMapper modelMapper;
    @Value("${spring.application.name}")
    private String name;
    private static final String TOPIC = "cloud_config_properties";

    @Autowired
    public PropertyInitializer(DBPropertyInitializerService dbInitializer, KafkaTemplate<String, String> kafkaTemplate,
                               BeanFactory beanFactory, ObjectMapper objectMapper, ModelMapper modelMapper) {
        this.dbInitializer = dbInitializer;
        this.kafkaTemplate = kafkaTemplate;
        this.beanFactory = beanFactory;
        this.objectMapper = objectMapper;
        this.modelMapper = modelMapper;
    }

    public ApplicationProperties initialGlobalProperties() throws IOException, SystemException {
        Object properties = dbInitializer.initialize();
        return objectMapper.convertValue(properties, ApplicationProperties.class);
    }

    public void UpdateAllProperties(ApplicationProperties properties) throws IOException {
        dbInitializer.updateProperties(properties);
        sendRefreshEvent();
    }

    public void refreshProperties() throws SystemException, IOException {
        ApplicationProperties applicationProperties = (ApplicationProperties) beanFactory.getBean("applicationProperties");
        modelMapper.map(initialGlobalProperties(), applicationProperties);
    }

    public void sendRefreshEvent() {
        kafkaTemplate.send(TOPIC, name);
    }
}
