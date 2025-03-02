package com.forestfull.log.up;

import lombok.Getter;

/**
 * Represents the logging levels used in the logging framework.
 * <ul>
 *   <li><b>OFF</b> - No logging (highest level).
 *   <li><b>ERROR</b> - Error messages (high level).
 *   <li><b>WARN</b> - Warning messages.
 *   <li><b>INFO</b> - Informational messages (low level).
 *   <li><b>ALL</b> - All messages (lowest level).
 * </ul>
 *
 * @author <a href="https://vigfoot.com">Vigfoot</a>
 */
@Getter
public enum Level {
    /**
     * All messages (lowest level).
     */
    ALL(""),

    /**
     * Informational messages (low level).
     */
    INFO(COLOR.BLUE),

    /**
     * Warning messages.
     */
    WARN(COLOR.YELLOW),

    /**
     * Error messages (high level).
     */
    ERROR(COLOR.RED),

    /**
     * No logging (highest level).
     */
    OFF(COLOR.RESET);

    public String getColorName() {
        return this.getColor() + this.name() + COLOR.RESET;
    }

    private final String color;

    Level(String color) {
        this.color = color;
    }

    public static class COLOR {
        private static final String RESET = "\u001B[0m";
        private static final String RED = "\u001B[31m";
        private static final String GREEN = "\u001B[32m";
        private static final String YELLOW = "\u001B[33m";
        private static final String BLUE = "\u001B[34m";
        private static final String CYAN = "\u001B[36m";
    }
}