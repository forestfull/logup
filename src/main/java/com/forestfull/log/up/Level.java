package com.forestfull.log.up;

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
public enum Level {
    /**
     * All messages (lowest level).
     */
    ALL,

    /**
     * Informational messages (low level).
     */
    INFO,

    /**
     * Warning messages.
     */
    WARN,

    /**
     * Error messages (high level).
     */
    ERROR,

    /**
     * No logging (highest level).
     */
    OFF
}