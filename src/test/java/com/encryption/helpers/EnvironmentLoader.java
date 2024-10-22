package com.encryption.helpers;

import com.encryption.utils.Base64EncodeDecodeUtil;
import com.encryption.utils.LoggerUtil;
import io.github.cdimascio.dotenv.Dotenv;
import org.apache.logging.log4j.Logger;

import javax.crypto.SecretKey;
import java.util.*;

public class EnvironmentLoader {

    private static final Logger logger = LoggerUtil.getLogger(EnvironmentLoader.class);
    private final Dotenv dotenv;

    /**
     * Constructor to load a specific environment file.
     *
     * @param envName The name of the environment file to load (e.g., ".env.uat").
     * @throws RuntimeException if the environment file cannot be loaded.
     */
    public EnvironmentLoader(String envName) {
        try {
            this.dotenv = Dotenv.configure()
                    .directory("envs")
                    .filename(envName)
                    .load();
        } catch (Exception e) {
            logger.error("Failed to load environment file: {}", envName, e);
            throw new RuntimeException("Failed to load environment file: " + envName, e);
        }
    }

    /**
     * Static factory method to load a specific environment file.
     *
     * @param envName The name of the environment file to load (e.g., ".env.uat").
     * @return an instance of {@link EnvironmentLoader} with the loaded environment file.
     */
    public static EnvironmentLoader load(String envName) {
        logger.info("Environment: '{}' was loaded successfully", envName);
        return new EnvironmentLoader(envName);
    }

    /**
     * Retrieves the value of a specific environment variable.
     *
     * @param key The key of the environment variable to retrieve.
     * @return The value of the environment variable, or null if the key does not exist.
     */
    public String getEnvironmentVariable(String key) {
        return dotenv.get(key);
    }

    public SecretKey getSecretKey(String key){

        // Retrieve the base64-encoded secret key string from the dotenv
        String encodedKey = dotenv.get(key);
        return Base64EncodeDecodeUtil.decodeSecretKey(encodedKey);
    }

    /**
     * Retrieves the value of a specific environment variable with a default fallback.
     *
     * @param key The key of the environment variable to retrieve.
     * @param defaultValue The default value to return if the key is not found.
     * @return The value of the environment variable, or the default value if the key does not exist.
     */
    public String getEnvironmentVariable(String key, String defaultValue) {
        return dotenv.get(key, defaultValue);
    }

    /**
     * Validates that the required environment variables are set in the environment file.
     * Throws an exception if any required variables are missing.
     *
     * @param requiredKeys The keys of the required environment variables.
     * @throws RuntimeException if any required environment variables are missing.
     */
    public void validateRequiredVariables(String... requiredKeys) {
        List<String> missingKeys = new ArrayList<>();
        for (String key : requiredKeys) {
            if (dotenv.get(key) == null) {
                missingKeys.add(key);
            }
        }
        if (!missingKeys.isEmpty()) {
            logger.error("Missing required environment variables: {}", String.join(", ",
                    missingKeys));
            throw new RuntimeException("Missing required environment variables: " + String.join(", ",
                    missingKeys));
        }
    }
}
