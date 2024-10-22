package com.encryption.tests.unit.ecryption;

import com.encryption.helpers.SecureEnvManager;
import com.encryption.utils.Base64EncodeDecodeUtil;
import com.encryption.utils.KeyGeneratorUtil;
import com.encryption.utils.LoggerUtil;
import com.encryption.utils.PathManagerUtil;
import org.apache.logging.log4j.Logger;

import javax.crypto.SecretKey;

public class SecretKeyGenerator {

    private static final Logger logger = LoggerUtil.getLogger(SecretKeyGenerator.class);
    private static final String SECRET_KEY_ENV_VARIABLE = "DEV_SECRET_KEY";

    public static void main(String[] args) {
        generateAndSaveSecretKey();
    }

    public static void generateAndSaveSecretKey() {
        try {
            SecretKey secretKey = generateSecretKey();
            String encodedSecretKey = encodeSecretKey(secretKey);
            saveSecretKeyToEnvFile(encodedSecretKey);
            logger.info("Secret key was generated and stored successfully");

        } catch (Exception e) {
            logger.error("Error occurred while generating and saving the secret key: {}", e.getMessage(), e);
            throw new RuntimeException("Error occurred while generating and saving the secret key", e);
        }
    }

    private static SecretKey generateSecretKey() {
        logger.info("Generating secret key...");
        return KeyGeneratorUtil.generateKey();
    }

    private static String encodeSecretKey(SecretKey secretKey) {
        logger.info("Encoding secret key to Base64...");
        return Base64EncodeDecodeUtil.encodeSecretKey(secretKey);
    }

    private static void saveSecretKeyToEnvFile(String encodedSecretKey) {
        logger.info("Saving secret key to environment file...");
        SecureEnvManager.setEnvVariable(PathManagerUtil.getEnvFilePath(), SECRET_KEY_ENV_VARIABLE, encodedSecretKey);
    }
}
