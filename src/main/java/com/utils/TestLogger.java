package com.utils;

import java.util.logging.Level;
import java.util.logging.Logger;

public final class TestLogger {

    private TestLogger() {
    }

    public static void info(
            Class<?> clazz,
            String message) {

        Logger.getLogger(clazz.getName())
                .log(Level.INFO, message);
    }

    public static void warning(
            Class<?> clazz,
            String message) {

        Logger.getLogger(clazz.getName())
                .log(Level.WARNING, message);
    }

    public static void error(
            Class<?> clazz,
            String message,
            Throwable throwable) {

        Logger.getLogger(clazz.getName())
                .log(Level.SEVERE, message, throwable);
    }
}