package com.encryption.tests.unit.ecryption;

import com.encryption.helpers.ConfigLoader;
import com.encryption.helpers.EnvironmentLoader;
import com.encryption.helpers.SecureEnvManager;
import com.encryption.utils.Base64EncodeDecodeUtil;
import com.encryption.utils.CryptoUtil;
import com.encryption.utils.LoggerUtil;
import com.encryption.utils.PathManagerUtil;
import org.apache.logging.log4j.Logger;

import javax.crypto.SecretKey;

public class DataEncryptor {

    private static final Logger logger = LoggerUtil.getLogger(DataEncryptor.class);

    // Constants for environment variables
    private static final String DEV_SECRET_KEY = "DEV_SECRET_KEY";
    private static final String PORTAL_USERNAME = "PORTAL_USERNAME";
    private static final String PORTAL_PASSWORD = "PORTAL_PASSWORD";

    public static void main(String[] args) {
        runEncryption();
        runDecryption();
    }

    private static void runEncryption() {
        try {
            SecretKey secretKey = getSecretKey();

            // Encrypt environment variables
            encryptVariable(PORTAL_USERNAME, secretKey);
            encryptVariable(PORTAL_PASSWORD, secretKey);

        } catch (Exception e) {
            logger.error("Error occurred while encrypting credentials: ", e);
            throw new RuntimeException("Error occurred while encrypting credentials", e);
        }
    }

    private static void runDecryption() {
        try {
            SecretKey secretKey = getSecretKey();

            // Decrypt environment variables and log
            decryptAndLogVariable(PORTAL_USERNAME, secretKey);
            decryptAndLogVariable(PORTAL_PASSWORD, secretKey);

        } catch (Exception e) {
            logger.error("Error occurred while decrypting credentials: ", e);
            throw new RuntimeException("Error occurred while decrypting credentials", e);
        }
    }

    // Helper method to load the secret key
    private static SecretKey getSecretKey() {
        EnvironmentLoader secretKeyEnv = loadSecretKeyEnv();
        SecretKey secretKey = secretKeyEnv.getSecretKey(DEV_SECRET_KEY);
        Base64EncodeDecodeUtil.validateNotNull(secretKey, "Secret key cannot be null");
        return secretKey;
    }

    // Encrypt a given environment variable
    private static void encryptVariable(String envVariable, SecretKey secretKey) {
        SecureEnvManager.encryptEnvVariable(envVariable, secretKey, PathManagerUtil.getDevEnvFilePath());
    }

    // Decrypt and log a given environment variable
    private static void decryptAndLogVariable(String envVariable, SecretKey secretKey) {
        EnvironmentLoader credentialsEnv = loadCredentialsEnv();
        String encryptedValue = credentialsEnv.getEnvironmentVariable(envVariable);
        String decryptedValue = CryptoUtil.decrypt(encryptedValue, secretKey);
        logger.info("Decrypted {}: {}", envVariable, decryptedValue);
    }

    // Method to load environment for credentials
    private static EnvironmentLoader loadCredentialsEnv() {
        return EnvironmentLoader.load(ConfigLoader.createDefaultLoader().getPropertyKey("DEV_ENV"));
    }

    // Method to load environment for secret key
    private static EnvironmentLoader loadSecretKeyEnv() {
        logger.info("Loading environment for secret key");
        return EnvironmentLoader.load(ConfigLoader.createDefaultLoader().getPropertyKey("SECRET_KEY_ENV"));
    }
}
