package com.books.application.config;

import com.books.utility.system.exception.SystemException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class PropertyInitializerEventService {
    private final PropertyInitializer propertyInitializer;
    private final ObjectMapper objectMapper;
    @Value("${spring.application.name}")
    private String name;

    @Autowired
    public PropertyInitializerEventService(PropertyInitializer propertyInitializer, ObjectMapper objectMapper) {
        this.propertyInitializer = propertyInitializer;
        this.objectMapper = objectMapper;
    }

    @KafkaListener(topics = "cloud_config_properties", groupId = "#{T(java.util.UUID).randomUUID().toString()}")
    public void refreshProperties(byte[] message) throws IOException, SystemException {
        if (message != null) {
            String serverName = objectMapper.readValue(message, String.class);
            if (serverName.equals(name)) propertyInitializer.refreshProperties();
        }
    }
}
