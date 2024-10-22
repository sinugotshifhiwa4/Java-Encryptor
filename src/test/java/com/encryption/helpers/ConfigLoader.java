package com.encryption.helpers;

import com.encryption.utils.LoggerUtil;
import com.encryption.utils.PathManagerUtil;
import org.apache.logging.log4j.Logger;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfigLoader {

    private static final Logger logger = LoggerUtil.getLogger(ConfigLoader.class);
    private final Properties properties;
    private final String path;

    public ConfigLoader(String configPath) {
        if (configPath == null || configPath.isEmpty()) {
            throw new IllegalArgumentException("Config path cannot be null or empty");
        }

        this.properties = new Properties();
        this.path = configPath;
        loadProperties();
    }

    private void loadProperties() {
        try (FileInputStream inputStream = new FileInputStream(path)) {
            properties.load(inputStream);
        } catch (IOException e) {
            logger.error("Failed to load properties from {}: {}", path, e.getMessage());
            throw new RuntimeException("Error loading properties from " + path, e);
        }
    }

    public String getPropertyKey(String key) {
        String value = properties.getProperty(key);
        logger.info("Property '{}' loaded successfully", key);

        if (value == null || value.isEmpty()) {
            logger.warn("Property '{}' is not set or empty", key);
            throw new IllegalArgumentException("Property '" + key + "' is not set or empty");
        }

        logger.info("Successfully retrieved property '{}'", key);
        return value;
    }

    // Static method to create a ConfigLoader with a default path
    public static ConfigLoader createDefaultLoader() {
        return new ConfigLoader(PathManagerUtil.getConfigFilePath());
    }
}
