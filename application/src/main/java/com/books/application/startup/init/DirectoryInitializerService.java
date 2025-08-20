package com.books.application.startup.init;

import com.books.utility.config.model.ApplicationProperties;
import com.books.utility.system.exception.SystemError;
import com.books.utility.system.exception.SystemException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Service to initialize required directories for file storage
 * Runs on application startup to ensure all necessary directories exist
 */
@Service
public class DirectoryInitializerService {

    private static final Logger logger = LoggerFactory.getLogger(DirectoryInitializerService.class);

    private final ApplicationProperties applicationProperties;

    @Autowired
    public DirectoryInitializerService(ApplicationProperties applicationProperties) {
        this.applicationProperties = applicationProperties;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void initializeDirectories() {
        try {
            logger.info("Initializing file storage directories...");
            createDirectoryIfNotExists(applicationProperties.getFileCrud().getBaseFilePath(), "Base file directory");
            createDirectoryIfNotExists(applicationProperties.getFileCrud().getTempFilePath(), "Temp file directory");
            logger.info("File storage directories initialized successfully");
        } catch (Exception e) {
            logger.error("Failed to initialize file storage directories", e);
            throw new RuntimeException("Directory initialization failed", e);
        }
    }

    private void createDirectoryIfNotExists(String directoryPath, String description) throws SystemException {
        try {
            Path path = resolvePath(directoryPath);
            File directory = path.toFile();
            if (!directory.exists()) {
                logger.info("Creating {}: {}", description, path.toAbsolutePath());
                if (!directory.mkdirs()) {
                    throw new SystemException(SystemError.DATA_NOT_FOUND,
                            "Failed to create " + description + ": " + path.toAbsolutePath(), 100009);
                }
                logger.info("Successfully created {}: {}", description, path.toAbsolutePath());
            } else {
                logger.debug("{} already exists: {}", description, path.toAbsolutePath());
            }
            if (!directory.canWrite()) {
                throw new SystemException(SystemError.DATA_NOT_FOUND,
                        description + " is not writable: " + path.toAbsolutePath(), 100009);
            }
        } catch (SystemException e) {
            throw e;
        } catch (Exception e) {
            throw new SystemException(SystemError.DATA_NOT_FOUND,
                    "Error creating " + description + ": " + e.getMessage(), 100009);
        }
    }

    private Path resolvePath(String directoryPath) {
        Path path = Paths.get(directoryPath);
        if (!path.isAbsolute()) {
            path = Paths.get(System.getProperty("user.dir")).resolve(directoryPath);
        }
        return path.normalize();
    }
} 