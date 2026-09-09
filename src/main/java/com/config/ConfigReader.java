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
        try (InputStream inputStream = ConfigReader.class
                .getClassLoader()
                .getResourceAsStream("config/config.properties")) {

            if (inputStream == null) {
                throw new RuntimeException("config.properties not found");
            }

            PROPERTIES.load(inputStream);

        } catch (IOException e) {
            throw new RuntimeException("Failed to load config.properties", e);
        }
    }

    public static String get(String key) {
        String systemProperty = System.getProperty(key);

        if (systemProperty != null && !systemProperty.isBlank()) {
            return systemProperty;
        }

        String environmentVariable = System.getenv(toEnvironmentVariable(key));

        if (environmentVariable != null && !environmentVariable.isBlank()) {
            return environmentVariable;
        }

        return PROPERTIES.getProperty(key);
    }

    public static String getRequired(String key) {
        String value = get(key);

        if (value == null || value.isBlank()) {
            throw new IllegalStateException(
                    "Required configuration is missing: " + key
            );
        }

        return value;
    }

    public static boolean getBoolean(String key, boolean defaultValue) {
        String value = get(key);

        if (value == null || value.isBlank()) {
            return defaultValue;
        }

        return Boolean.parseBoolean(value);
    }

    public static int getInt(String key, int defaultValue) {
        String value = get(key);

        if (value == null || value.isBlank()) {
            return defaultValue;
        }

        return Integer.parseInt(value);
    }

    private static String toEnvironmentVariable(String key) {
        return key
                .replace(".", "_")
                .replace("-", "_")
                .toUpperCase();
    }
}