package com.forestfull.log.up.util;

import com.forestfull.log.up.Level;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Date;

/**
 * A utility class for logging messages at different levels (INFO, WARN, ERROR).
 *
 * @author <a href="https://vigfoot.com">Vigfoot</a>
 * @see Level
 */
public class Log {

    private Log() {
    }

    /**
     * Logs an informational message.
     *
     * @param msg The message to log.
     * @author <a href="https://vigfoot.com">Vigfoot</a>
     */
    public static void info(Object... msg) {
        write(Level.INFO, msg);
    }

    /**
     * Logs a warning message.
     *
     * @param msg The message to log.
     * @author <a href="https://vigfoot.com">Vigfoot</a>
     */
    public static void warn(Object... msg) {
        write(Level.WARN, msg);
    }

    /**
     * Logs an error message.
     *
     * @param msg The message to log.
     * @author <a href="https://vigfoot.com">Vigfoot</a>
     */
    public static void error(Object... msg) {
        write(Level.ERROR, msg);
    }

    /**
     * Writes a log message with a specific level and placeholder pattern.
     *
     * @param level    The log level (INFO, WARN, ERROR).
     * @param messages The messages to log.
     * @author <a href="https://vigfoot.com">Vigfoot</a>
     */
    static void write(final Level level, final Object... messages) {
        write(level, LogUpFactoryBean.logFormatter.getPlaceholder(), messages);
    }

    /**
     * Writes a log message with a specific level, placeholder pattern, and messages.
     *
     * @param level       The log level (INFO, WARN, ERROR).
     * @param placeholder The placeholder pattern for the log message.
     * @param messages    The messages to log.
     * @author <a href="https://vigfoot.com">Vigfoot</a>
     */
    static void write(final Level level, final String placeholder, final Object... messages) {
        if (level.compareTo(LogUpFactoryBean.level) < 0) return;
        if (messages == null || messages.length == 0) return;
        if (LogUpFactoryBean.logFormatter == null) return;

        StackTraceElement stackTrace = Thread.currentThread().getStackTrace()[4];
        final String now = LogUpFactoryBean.logFormatter.getDateTimeFormat().format(new Date());
        final StringBuilder msgBuilder = new StringBuilder(1024);

        final String className = stackTrace.getClassName();
        final String methodName = stackTrace.getMethodName();
        StringBuilder currentThreadName = new StringBuilder(Thread.currentThread().getName()).append(' ');

        if (className.split("\\.").length > 1) {
            String[] split = className.split("\\.");
            for (int i = 0; i < split.length - 1; i++) {
                String pack = split[i];
                currentThreadName.append(pack.charAt(0)).append('.');
            }
            currentThreadName.append(split[split.length - 1]).append('.');
        } else {
            currentThreadName.append(className).append('.');
        }

        currentThreadName.append(methodName)
                .append('(').append(stackTrace.getFileName()).append(':').append(stackTrace.getLineNumber()).append(')');

        for (Object message : messages) msgBuilder.append(message);

        final String logMessage = placeholder
                .replace(LogFormatter.MessagePattern.DATETIME, now)
                .replace(LogFormatter.MessagePattern.LEVEL, level == Level.ALL ? "----" : level.name())
                .replace(LogFormatter.MessagePattern.THREAD, currentThreadName)
                .replace(LogFormatter.MessagePattern.MESSAGE, msgBuilder.toString())
                .replace(LogFormatter.MessagePattern.NEW_LINE, System.lineSeparator());

        LogFactory.console(logMessage);
        if (LogUpFactoryBean.fileRecorder != null)
            LogFactory.file(logMessage);
    }

    /**
     * A factory class for handling log output to console and files.
     *
     * @author <a href="https://vigfoot.com">Vigfoot</a>
     */
    public static class LogFactory {
        /**
         * Outputs the log message to the console.
         *
         * @param msg The log message to output.
         * @author <a href="https://vigfoot.com">Vigfoot</a>
         */
        protected static synchronized void console(final String msg) {
            final Writer fdOut = new PrintWriter(new FileWriter(FileDescriptor.out));

            try {
                fdOut.write(msg);
                fdOut.flush();
            } catch (IOException e) {
                e.printStackTrace(System.err);
            }
        }

        /**
         * Outputs the log message to a file.
         *
         * @param msg The log message to output.
         * @author <a href="https://vigfoot.com">Vigfoot</a>
         */
        protected static synchronized void file(String msg) {
            final FileRecorder fileRecorder = LogUpFactoryBean.fileRecorder;

            String logFileDirectory = fileRecorder.getDirectory();

            if (logFileDirectory == null || logFileDirectory.isEmpty()) return;

            if (!File.separator.equals(logFileDirectory.substring(0, 1)) && !"/".equals(logFileDirectory.substring(0, 1)))
                logFileDirectory = System.getProperty("user.dir") + File.separator + logFileDirectory;

            if (!File.separator.equals(logFileDirectory.substring(logFileDirectory.length() - 1))
                    && !"/".equals(logFileDirectory.substring(logFileDirectory.length() - 1)))
                logFileDirectory += File.separator;

            try {
                final File rootDirectory = new File(logFileDirectory);
                if (!rootDirectory.exists()) {
                    if (!rootDirectory.isDirectory()) rootDirectory.deleteOnExit();
                    boolean isSucceed = rootDirectory.mkdirs();
                    if (!isSucceed) throw new IOException("Failed to create log directory: " + rootDirectory);
                }

                final File logFile = new File(logFileDirectory + File.separator + fileRecorder.getPlaceholder().replace(FileRecorder.FilePattern.DATE, ""));
                if (!logFile.exists()) {
                    boolean isSucceed = logFile.createNewFile();
                    if (!isSucceed) throw new IOException("Failed to create log file: " + logFile);
                } else if (logFile.isFile()) {
                    final String currentTimeFormatName = fileRecorder.getPlaceholder().replace(FileRecorder.FilePattern.DATE, fileRecorder.getDateFormat().format(new Date()));
                    final String existedTimeFormatName = fileRecorder.getPlaceholder().replace(FileRecorder.FilePattern.DATE, fileRecorder.getDateFormat().format(new Date(logFile.lastModified())));
                    if (!currentTimeFormatName.equals(existedTimeFormatName)) {
                        boolean isSucceed = logFile.renameTo(new File(logFileDirectory + File.separator + existedTimeFormatName));
                        if (!isSucceed) throw new IOException("Failed to rename log file: " + existedTimeFormatName);
                        logFile.deleteOnExit();
                    }
                }

                final FileOutputStream outputStream = new FileOutputStream(logFile, true);
                outputStream.write((msg.getBytes(StandardCharsets.UTF_8)));
                outputStream.flush();
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace(System.err);
            }
        }

        protected static synchronized void initConsole() {
            Level level = LogUpFactoryBean.level; // touch
            console("=================================================================================================================================================================" + System.lineSeparator());
            console("Log Up by forest full's vigfoot" + System.lineSeparator());
            console("=================================================================================================================================================================" + System.lineSeparator());
        }
    }
}