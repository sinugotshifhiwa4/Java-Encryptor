package com.encryption.utils;

import org.apache.logging.log4j.Logger;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import java.nio.charset.StandardCharsets;

public class CryptoUtil {

    private static final Logger logger = LoggerUtil.getLogger(CryptoUtil.class);
    private static final String CIPHER_TRANSFORMATION = "AES/CBC/PKCS5Padding";
    private static final int IV_SIZE = 16; // 16 bytes for AES

    public static String encrypt(String strToEncrypt, SecretKey secretKey) {

        try {
            // Create a Cipher instance for AES encryption
            Cipher cipher = Cipher.getInstance(CIPHER_TRANSFORMATION);

            // Generate a random IV using the method
            byte[] iv = KeyGeneratorUtil.generateIV();

            // comment
            IvParameterSpec ivParameters = new IvParameterSpec(iv);

            // Initialize the cipher for encryption
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivParameters);

            // Encrypt the plaintext
            byte[] encryptedBytes = cipher.doFinal(strToEncrypt.getBytes(StandardCharsets.UTF_8));

            // Combine IV and ciphertext
            byte[] combined = new byte[IV_SIZE + encryptedBytes.length];
            System.arraycopy(iv, 0, combined, 0, IV_SIZE);
            System.arraycopy(encryptedBytes, 0, combined, IV_SIZE, encryptedBytes.length);

            // Return the combined IV and ciphertext as a Base64 encoded string
            return Base64EncodeDecodeUtil.encode(combined);

        } catch (Exception e) {
            logger.error("Error occurred while encrypting: {}", e.getMessage(), e);
            throw new RuntimeException("Error occurred while encrypting: " + e.getMessage(), e);
        }
    }

    public static String decrypt(String strToDecrypt, SecretKey secretKey) {

        try {
            // Decode the Base64 encoded string to get the combined IV and ciphertext
            byte[] combined = Base64EncodeDecodeUtil.decode(strToDecrypt);

            // Validate the length of the combined data
            if (combined.length < IV_SIZE) {
                throw new IllegalArgumentException("Invalid encrypted data length");
            }

            // Extract IV and ciphertext
            byte[] iv = new byte[IV_SIZE];
            System.arraycopy(combined, 0, iv, 0, IV_SIZE); // Extract IV
            byte[] encryptedBytes = new byte[combined.length - IV_SIZE];
            System.arraycopy(combined, IV_SIZE, encryptedBytes, 0, encryptedBytes.length);

            // Create a Cipher instance for AES decryption
            Cipher cipher = Cipher.getInstance(CIPHER_TRANSFORMATION);
            IvParameterSpec ivParams = new IvParameterSpec(iv);

            // Initialize the cipher for decryption
            cipher.init(Cipher.DECRYPT_MODE, secretKey, ivParams);

            // Decrypt the ciphertext
            byte[] decryptedBytes = cipher.doFinal(encryptedBytes);
            return new String(decryptedBytes, StandardCharsets.UTF_8);

        } catch (Exception e) {
            logger.error("Error while decrypting: {}", e.getMessage(), e);
            throw new RuntimeException("Error while decrypting: " + e.getMessage(), e);
        }
    }
}
