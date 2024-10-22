package com.encryption.utils;

import org.apache.logging.log4j.Logger;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class Base64EncodeDecodeUtil {

    private static final Logger logger = LoggerUtil.getLogger(Base64EncodeDecodeUtil.class);
    private static final String ALGORITHM = "AES";

    // Private constructor to prevent instantiation
    private Base64EncodeDecodeUtil() {
        logger.error("Utility class cannot be instantiated");
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }

    public static String encode(byte[] data) {
        validateNotNull(data, "Input byte array cannot be null");
        return Base64.getEncoder().encodeToString(data);
    }

    public static byte[] decode(String base64String) {
        validateNotNull(base64String, "Input string cannot be null");
        return Base64.getDecoder().decode(base64String);
    }

    public static String encode(String data) {
        validateNotNull(data, "Input string cannot be null");
        return Base64.getEncoder().encodeToString(data.getBytes(StandardCharsets.UTF_8));
    }

    public static String decodeToString(String base64String) {
        validateNotNull(base64String, "Input string cannot be null");
        byte[] decodedBytes = Base64.getDecoder().decode(base64String);
        return new String(decodedBytes, StandardCharsets.UTF_8);
    }


    public static String encodeSecretKey(SecretKey secretKey) {
        validateNotNull(secretKey, "Input secret key cannot be null");
        return Base64.getEncoder().encodeToString(secretKey.getEncoded());
    }

    public static SecretKey decodeSecretKey(String encodedKey) {
        validateNotNull(encodedKey, "Input encoded key cannot be null");
        try {
            byte[] decodedKey = Base64.getDecoder().decode(encodedKey);
            return new SecretKeySpec(decodedKey, ALGORITHM);
        } catch (IllegalArgumentException e) {
            logger.error("Error while decoding secret key: {}", e.getMessage(), e);
            throw new RuntimeException("Error while decoding secret key: " + e.getMessage(), e);
        }
    }

    public static void validateNotNull(Object obj, String errorMessage) {
        if (obj == null) {
            logger.error(errorMessage);
            throw new IllegalArgumentException(errorMessage);
        }
    }
}
