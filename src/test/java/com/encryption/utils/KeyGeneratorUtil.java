package com.encryption.utils;

import org.apache.logging.log4j.Logger;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class KeyGeneratorUtil {

    private static final Logger logger = LoggerUtil.getLogger(KeyGeneratorUtil.class);
    private static final String ALGORITHM = "AES";
    private static final int KEY_SIZE = 256;
    private static final int IV_SIZE = 16;


    public static SecretKey generateKey() {
        try {
            KeyGenerator keyGen = KeyGenerator.getInstance(ALGORITHM);
            keyGen.init(KEY_SIZE, SecureRandom.getInstanceStrong()); // Use a strong random source
            return keyGen.generateKey();
        } catch (NoSuchAlgorithmException e) {
            logger.error("Error while generating secret key: {}", e.getMessage(), e);
            throw new IllegalArgumentException("Error while generating secret key: " + e.getMessage(), e);
        }
    }

    public static byte[] generateIV() {
        byte[] iv = new byte[IV_SIZE];
        SecureRandom secureRandom = new SecureRandom();
        secureRandom.nextBytes(iv);
        return iv;
    }
}
