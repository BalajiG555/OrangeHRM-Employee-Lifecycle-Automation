package com.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public final class ConfigReader {

    private static final Properties PROPERTIES = new Properties();

    static {
        loadProperties();
    }

    private ConfigReader() {
    }

    private static void loadProperties() {

        String environment =
                System.getProperty("env", "local")
                        .trim()
                        .toLowerCase();

        loadFile("config/config.properties");

        if (!environment.equals("local")) {

            String environmentFile =
                    "config/config-" + environment + ".properties";

            loadFile(environmentFile);
        }
    }

    private static void loadFile(String fileName) {

        try (InputStream inputStream =
                     ConfigReader.class
                             .getClassLoader()
                             .getResourceAsStream(fileName)) {

            if (inputStream != null) {
                PROPERTIES.load(inputStream);
            }

        } catch (IOException e) {

            throw new RuntimeException(
                    "Failed to load configuration: "
                            + fileName,
                    e
            );
        }
    }

    public static String get(String key) {

        // 1. JVM system property
        String systemProperty =
                System.getProperty(key);

        if (systemProperty != null
                && !systemProperty.isBlank()) {

            return systemProperty;
        }

        // 2. Explicit OrangeHRM environment variable
        // Avoids conflicts with Windows variables such as USERNAME.
        String environmentVariable =
                System.getenv(
                        "ORANGEHRM_" +
                                toEnvironmentVariable(key)
                );

        if (environmentVariable != null
                && !environmentVariable.isBlank()) {

            return environmentVariable;
        }

        // 3. Properties file
        return PROPERTIES.getProperty(key);
    }

    public static String getRequired(String key) {

        String value = get(key);

        if (value == null || value.isBlank()) {

            throw new IllegalStateException(
                    "Required configuration is missing: "
                            + key
                            + " | Environment: "
                            + getEnvironment()
            );
        }

        return value;
    }

    public static String getEnvironment() {

        return System.getProperty(
                "env",
                "local"
        ).trim().toLowerCase();
    }

    public static boolean getBoolean(String key) {
        return getBoolean(key, false);
    }

    public static boolean getBoolean(
            String key,
            boolean defaultValue) {

        String value = get(key);

        if (value == null || value.isBlank()) {
            return defaultValue;
        }

        return Boolean.parseBoolean(value);
    }

    public static int getInt(String key) {
        return getInt(key, 0);
    }

    public static int getInt(
            String key,
            int defaultValue) {

        String value = get(key);

        if (value == null || value.isBlank()) {
            return defaultValue;
        }

        return Integer.parseInt(value);
    }

    private static String toEnvironmentVariable(
            String key) {

        return key
                .replace(".", "_")
                .replace("-", "_")
                .toUpperCase();
    }
}