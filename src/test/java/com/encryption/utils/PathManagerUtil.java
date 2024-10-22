package com.encryption.utils;

public class PathManagerUtil {

    public static String getConfigFilePath() {
        return System.getProperty("user.dir") + "/src/main/resources/config.properties";
    }

    public static String getEnvFilePath() {
        return System.getProperty("user.dir") + "/envs/.env";
    }

    public static String getDevEnvFilePath() {
        return System.getProperty("user.dir") + "/envs/.env.dev";
    }

    public static String getUatEnvFilePath() {
        return System.getProperty("user.dir") + "/envs/.env.uat";
    }

}
