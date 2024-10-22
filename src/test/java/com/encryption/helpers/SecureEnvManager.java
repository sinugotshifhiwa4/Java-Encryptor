package com.encryption.helpers;

import com.encryption.utils.CryptoUtil;
import com.encryption.utils.LoggerUtil;
import org.apache.logging.log4j.Logger;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

public class SecureEnvManager {

    private static final Logger logger = LoggerUtil.getLogger(SecureEnvManager.class);

    private static final String DEV_ENV = "DEV_ENV";

    public static void encryptEnvVariable(String envVariable, SecretKey secretKey, String filePath) {
        try {
            String envValue = EnvironmentLoader.load(ConfigLoader.createDefaultLoader().getPropertyKey(DEV_ENV))
                    .getEnvironmentVariable(envVariable);

            if (envValue != null) {
                String encryptedValue = CryptoUtil.encrypt(envValue, secretKey);
                logger.info("Key '{}' is encrypted successfully", envVariable);

                if (encryptedValue != null) {
                    // Set the encrypted value in the .env file
                    setEnvVariable(filePath, envVariable, encryptedValue);
                } else {
                    logger.error("Failed to save encrypted env variable: {}", envVariable);
                    throw new RuntimeException("Failed to save encrypted env variable: " + envVariable);
                }
            } else {
                throw new RuntimeException("Environment variable '" + envVariable + "' is null");
            }
        } catch (Exception e) {
            logger.error("Error occurred while encrypting credentials for: {} Message: {}", envVariable, e.getMessage());
            throw new RuntimeException("Error occurred while encrypting credentials", e);
        }
    }


    public static void setEnvVariable(String filePath, String envVariable, String value) {

        Path path = Paths.get(filePath);

        try {
            // Read all lines from the .env file
            List<String> lines = Files.readAllLines(path);

            // Track if the variable was updated using AtomicBoolean
            AtomicBoolean isUpdated = new AtomicBoolean(false);

            // Update the lines or add the new variable
            List<String> updatedLines = lines.stream()
                    .map(line -> {
                        if (line.startsWith(envVariable + "=")) {
                            isUpdated.set(true);
                            return envVariable + "=" + value; // Update the line
                        }
                        return line;
                    })
                    .collect(Collectors.toList());

            // Add the variable if it wasn't found
            if (!isUpdated.get()) {
                updatedLines.add(envVariable + "=" + value);
            }

            // Write back only if there are changes
            if (!lines.equals(updatedLines)) {
                Files.write(path, updatedLines);
            }

        } catch (IOException e) {
            logger.error("Failed to update environment variable '{}' in file: {}", envVariable, filePath, e);
            throw new RuntimeException("Failed to update environment variable '" + envVariable + "' in file: " + filePath, e);
        }
    }
}
