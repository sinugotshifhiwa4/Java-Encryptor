package com.encryption.tests.unit;

import com.encryption.helpers.ConfigLoader;
import com.encryption.helpers.EnvironmentLoader;
import com.encryption.utils.LoggerUtil;
import org.apache.logging.log4j.Logger;

public class EnvAndPropertiesTest {

    private static final Logger logger = LoggerUtil.getLogger(EnvAndPropertiesTest.class);

    // Constants for environment variables and properties
    private static final String PORTAL_USERNAME = "PORTAL_USERNAME";
    private static final String PORTAL_PASSWORD = "PORTAL_PASSWORD";
    private static final String PORTAL_URL = "PORTAL_URL";
    private static final String DEV_ENV = "DEV_ENV";

    private static final ConfigLoader configLoader = ConfigLoader.createDefaultLoader();

    public static void main(String[] args) {
        loadAndLogEnvironmentVariables();
        loadAndLogConfigProperties();
    }

    private static void loadAndLogEnvironmentVariables() {
        try {
            loadAndValidateEnvVariables();
        } catch (Exception e) {
            logger.error("Error while retrieving environment variables from dotenv", e);
            throw new RuntimeException("Error while retrieving environment variables from dotenv", e);
        }
    }

    private static void loadAndLogConfigProperties() {
        try {
            // Retrieve and log property key
            String baseUrl = configLoader.getPropertyKey(PORTAL_URL);
            logger.info("BASE URL: {}", baseUrl);

        } catch (Exception e) {
            logger.error("Error while retrieving config key from properties file", e);
            throw new RuntimeException("Error while retrieving config key from properties file", e);
        }
    }

    // Method to load environment for credentials
    private static EnvironmentLoader loadCredentialsEnv() {
        return EnvironmentLoader.load(configLoader.getPropertyKey(DEV_ENV));
    }

    private static void loadAndValidateEnvVariables() {
        EnvironmentLoader credentialsEnv = loadCredentialsEnv();
        String usernameEnvVariable = credentialsEnv.getEnvironmentVariable(PORTAL_USERNAME);
        String passwordEnvVariable = credentialsEnv.getEnvironmentVariable(PORTAL_PASSWORD);

        // Validate required environment variables
        credentialsEnv.validateRequiredVariables(PORTAL_USERNAME, PORTAL_PASSWORD);

        logger.info("Loaded credentials - Username: '{}' and Password: '{}'", usernameEnvVariable, passwordEnvVariable);
    }
}
