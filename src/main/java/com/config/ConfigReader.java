package com.config;

import java.io.InputStream;
import java.util.Properties;

public final class ConfigReader {

    private static final Properties PROPERTIES = new Properties();

    static {

        try (InputStream inputStream =
                     ConfigReader.class
                             .getClassLoader()
                             .getResourceAsStream("config/config.properties")) {

            if (inputStream == null) {
                throw new RuntimeException(
                        "config.properties not found"
                );
            }

            PROPERTIES.load(inputStream);

        } catch (Exception e) {

            throw new RuntimeException(
                    "Unable to load configuration",
                    e
            );
        }
    }

    private ConfigReader() {
    }

    public static String get(String key) {

        String value = System.getProperty(
                key,
                PROPERTIES.getProperty(key)
        );

        if (value == null || value.isBlank()) {

            throw new RuntimeException(
                    "Missing configuration property: " + key
            );
        }

        return value.trim();
    }

    public static boolean getBoolean(String key) {
        return Boolean.parseBoolean(get(key));
    }

    public static int getInt(String key) {
        return Integer.parseInt(get(key));
    }
}