package com.encryption.helpers;

import com.encryption.utils.LoggerUtil;
import org.apache.logging.log4j.Logger;

public class ErrorHandler {

    private static final Logger logger = LoggerUtil.getLogger(ErrorHandler.class);

    public static void logExceptionError(String methodName, Exception e, Object... params) {
        logger.error("Exception occurred in method '{}': {} | Params: {}", methodName, e.getMessage(), params, e);
    }

    public static void logAssertionError(String methodName, AssertionError e) {
        logger.error("AssertionError in method '{}': {}", methodName, e.getMessage(), e);
    }

    public static void logCustomError(String methodName, String customErrorMessage, Object... params) {
        logger.error("Custom error in method '{}': {} | Params: {}", methodName, customErrorMessage, params);
    }

    public static void logInfoMessage(String methodName, String infoMessage) {
        logger.info("Info message in method '{}': {}", methodName, infoMessage);
    }

    public static void logDebugMessage(String methodName, String debugMessage) {
        logger.debug("Debug message in method '{}': {}", methodName, debugMessage);
    }
}
