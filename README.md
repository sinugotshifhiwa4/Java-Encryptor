---

# Java-Encryptor

## Overview
**Java-Encryptor** is a Maven-based project designed to provide a comprehensive solution for securely managing and encrypting sensitive environment variables such as usernames, passwords, and secret keys. The project includes utilities for logging, configuration management, base64 encoding/decoding, secret key generation, and encryption/decryption using AES encryption.

## Features
- **AES Encryption**: Secure environment variable encryption using AES with CBC and PKCS5Padding.
- **Thread-Safe Logging**: A custom logger utilizing `ThreadLocal` to ensure thread-specific logs.
- **Environment Management**: Dynamically load environment configurations via `.env` files, supporting different environments (development, UAT, production).
- **Base64 Utility**: Encode and decode secret keys and data strings.
- **Configurable Paths**: Centralized management for project file paths.
- **Unit Testing**: Comprehensive unit tests covering key utilities such as secret key generation and data encryption.

---

## Project Setup

### Project Structure and Dependencies

1. **Maven Project Initialization**  
   - Set up a standard Maven project structure, including the following directory layout:

     ```plaintext
     Java-Encryptor/
     │
     ├── envs/
     │   ├── .env
     │   ├── .env.dev
     │   ├── .env.uat
     │
     ├── logs/
     │   ├── test_error.log
     │   ├── test_run.log
     │
     ├── src/
     │   ├── main/
     │   │   ├── java/
     │   │   └── resources/
     │   │       └── config.properties
     │   │
     │   ├── test/
     │       ├── java/
     │       │   └── com/
     │       │       └── encryption/
     │       │           ├── helpers/
     │       │           │   ├── ConfigLoader
     │       │           │   ├── EnvironmentLoader
     │       │           │   ├── ErrorHandler
     │       │           │   └── SecureEnvManager
     │       │           │
     │       │           ├── tests/
     │       │           │   ├── functional/
     │       │           │   └── unit/
     │       │           │       └── ecryption/
     │       │           │           ├── DataEncryptor
     │       │           │           ├── SecretKeyGenerator
     │       │           │           └── EnvAndPropertiesTest
     │       │           │
     │       │           └── utils/
     │       │               ├── Base64EncodeDecodeUtil
     │       │               ├── CryptoUtil
     │       │               ├── KeyGeneratorUtil
     │       │               ├── LoggerUtil
     │       │               └── PathManagerUtil
     │       │
     │       └── resources/
     │           └── log4j2.xml
     │
     └── target/
     ```

2. **Dependencies**
   - Add necessary dependencies to the `pom.xml` file:
     - `log4j2` for logging
     - `dotenv` for environment management
     - `javax.crypto` for encryption
     
3. **Build Project Structure**
   - Organized packages under `com.encryption`, including:
     - `helpers` (ConfigLoader, EnvironmentLoader)
     - `utils` (Base64EncodeDecodeUtil, CryptoUtil)
     - `tests.unit` (Unit test classes)

---

### Configuration Setup

1. **Configure Logging (log4j2)**  
   - Set up `log4j2.xml` to manage application logs:
     - Output logs to `logs/test_run.log` and `logs/test_error.log`.
     - Ensure logs are retained for 14 days with a maximum of 400 loggers per thread.

2. **Logger Utility Class (`LoggerUtil`)**  
   - Thread-safe logging using `ThreadLocal`:
     - Each class has its own logger instance.
     - Example:
       ```java
       private static final Logger logger = LoggerUtil.getLogger(ConfigLoader.class);
       ```

3. **Configuration Properties Loader (`ConfigLoader`)**  
   - Loads properties from configuration files:
     ```java
     ConfigLoader.createDefaultLoader().getPropertyKey("DEV_ENV");
     ```

4. **Environment Variables Loader (`EnvironmentLoader`)**  
   - Manages `.env`, `.env.dev`, `.env.uat`, and `.env.prod` files.
   - Example:
     ```java
     EnvironmentLoader envLoader = EnvironmentLoader.load(ConfigLoader.createDefaultLoader().getPropertyKey("DEV_ENV"));
     ```

5. **Path Manager (`PathManagerUtil`)**  
   - Centralized utility to manage file paths for different environments.

---

### Secret Key Generation

1. **Base64 Encoding/Decoding (`Base64EncodeDecodeUtil`)**  
   - Utility to encode and decode byte arrays and strings.

2. **Secret Key Generation (`KeyGeneratorUtil`)**  
   - Generates AES secret keys using a 256-bit key size and a 16-byte IV.

3. **Secure Environment Manager (`SecureEnvManager`)**  
   - Manages secure environment variables, such as generating and storing secret keys.

4. **Unit Tests**  
   - Unit tests for secret key generation and validation.

---

### Data Encryption

1. **Encrypt and Decrypt Utility (`CryptoUtil`)**  
   - Implements AES encryption and decryption with the transformation `AES/CBC/PKCS5Padding`.
   - Example encryption usage:
     ```java
     String encryptedData = CryptoUtil.encrypt(strToEncrypt, secretKey);
     ```

2. **Encrypt Credentials in Environment (`SecureEnvManager`)**  
   - Adds an `encryptEnvVariable` method for encrypting sensitive environment variables.

3. **Unit Tests**  
   - Unit test for encryption and decryption methods.

---

## Usage

1. **Secret Key Generation**  
   - Generates secret keys and stores them in the environment variables file:
     ```java
     SecureEnvManager.generateAndStoreSecretKey("DEV_SECRET_KEY", PathManagerUtil.getDevEnvFilePath());
     ```

2. **Encryption and Decryption**  
   - Encrypt sensitive data like usernames and passwords:
     ```java
     SecureEnvManager.encryptEnvVariable("PORTAL_USERNAME", secretKey, PathManagerUtil.getDevEnvFilePath());
     ```

   - Decrypt the environment variable and log the result:
     ```java
     String decryptedValue = CryptoUtil.decrypt(encryptedValue, secretKey);
     logger.info("Decrypted value: {}", decryptedValue);
     ```

## Unit Tests
- Unit tests are available under the `tests.unit` package, ensuring the correct functionality of secret key generation, environment loading, and encryption processes.

---

## Conclusion
**Java-Encryptor** provides a robust solution for securely managing and encrypting environment variables in a Java-based project, utilizing AES encryption and best practices for configuration management and logging. The modular approach ensures flexibility for different environments and allows easy integration into existing projects.

---
