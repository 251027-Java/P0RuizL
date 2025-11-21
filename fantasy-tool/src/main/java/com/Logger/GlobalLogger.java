package com.Logger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GlobalLogger {
    private static Logger actualLogger; // e.g., an instance of java.util.logging.Logger or org.slf4j.Logger


    // Methods to delegate to the actual logger
    public static void info(String message) {
        if (actualLogger == null) {
            actualLogger = LoggerFactory.getLogger(GlobalLogger.class);
        }
        actualLogger.info(message);
    }

    public static void error(String message) {
        if (actualLogger == null) {
            actualLogger = LoggerFactory.getLogger(GlobalLogger.class);
        }
        actualLogger.error(message); 
    }

    public static void error(String message, Throwable t) {
        if (actualLogger == null) {
            actualLogger = LoggerFactory.getLogger(GlobalLogger.class);
        }
        actualLogger.error(message, t); 
    }

    public static void debug(String message) {
        if (actualLogger == null) {
            actualLogger = LoggerFactory.getLogger(GlobalLogger.class);
        }
        actualLogger.debug(message);
    }

    public static void debug(String message, Throwable t) {
        if (actualLogger == null) {
            actualLogger = LoggerFactory.getLogger(GlobalLogger.class);
        }
        actualLogger.debug(message, t);
    }

    public static void trace(String message) {
        if (actualLogger == null) {
            actualLogger = LoggerFactory.getLogger(GlobalLogger.class);
        }
        actualLogger.trace(message);
    }

}