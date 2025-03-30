package com.forestfull.log.up.util;

import com.forestfull.log.up.Level;
import com.forestfull.log.up.formatter.FileRecorder;
import lombok.Setter;
import org.springframework.util.StringUtils;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

/**
 * A utility class for logging messages at different levels (INFO, WARN, ERROR).
 *
 * @author <a href="https://vigfoot.com">Vigfoot</a>
 * @see Level
 */
public class Log {

    private static final Map<String, SourceInfo> sourceInfoMap = new HashMap<>();

    private Log() {
    }

    /**
     * Creates a LogFactory instance with the stack trace information.
     * <p> Note: Ensure that the currentSourceCodeLineNumber is not duplicated for the same clazz. </p>
     * <p><b>Additionally, passing parameters as constants is recommended; otherwise, it may lead to performance degradation issues.</b></p>
     *
     * <p>Example.</p>
     * <code>Log.stacktrace(this.getClass(), 88).info("your logging Message")</code>
     * <BR>
     * <code>Log.stacktrace(YourClass.class, 100).info("your logging Message")</code>
     *
     * @param clazz                       the class from the current source code
     * @param currentSourceCodeLineNumber the line number in the current source code
     * @return a LogFactory instance with stack trace information
     * @author <a href="https://vigfoot.com">Vigfoot</a>
     */
    public static Log.LogFactory location(Class<?> clazz, int currentSourceCodeLineNumber) {
        final String key = clazz.hashCode() + "-" + currentSourceCodeLineNumber;
        SourceInfo sourceInfo = sourceInfoMap.get(key);

        final LogFactory logFactory = new LogFactory();
        if (Objects.isNull(sourceInfo)) {
            final StackTraceElement stackTraceElement = java.lang.Thread.currentThread().getStackTrace()[2];

            sourceInfo = SourceInfo.builder()
                    .className(stackTraceElement.getClassName())
                    .methodName(stackTraceElement.getMethodName())
                    .fileName(stackTraceElement.getFileName())
                    .lineNumber(stackTraceElement.getLineNumber())
                    .build();

            sourceInfoMap.put(key, sourceInfo);
        }

        logFactory.setBuffer(System.lineSeparator() + sourceInfo.getSourceInfo());

        return logFactory;
    }

    /**
     * Logs a debug message in a JUnit test.
     * <p>Example.</p>
     * <code>Log.test("your logging Message")</code>
     *
     * @param msg The message to log.
     * @author <a href="https://vigfoot.com">Vigfoot</a>
     */
    public static void test(Object... msg) {
        write(Level.TEST, msg);
    }

    /**
     * Logs an debug message.
     * <p>Example.</p>
     * <code>Log.debug("your logging Message")</code>
     *
     * @param msg The message to log.
     * @author <a href="https://vigfoot.com">Vigfoot</a>
     */
    public static void debug(Object... msg) {
        write(Level.DEBUG, msg);
    }

    /**
     * Logs an informational message.
     * <p>Example.</p>
     * <code>Log.info("your logging Message")</code>
     *
     * @param msg The message to log.
     * @author <a href="https://vigfoot.com">Vigfoot</a>
     */
    public static void info(Object... msg) {
        write(Level.INFO, msg);
    }

    /**
     * Logs a warning message.
     * <p>Example.</p>
     * <code>Log.warn("your logging Message")</code>
     *
     * @param msg The message to log.
     * @author <a href="https://vigfoot.com">Vigfoot</a>
     */
    public static void warn(Object... msg) {
        write(Level.WARN, msg);
    }

    /**
     * Logs an error message.
     * <p>Example.</p>
     * <code>Log.error("your logging Message")</code>
     *
     * @param msg The message to log.
     * @author <a href="https://vigfoot.com">Vigfoot</a>
     */
    public static void error(Object... msg) {
        write(Level.ERROR, msg);
    }

    /**
     * Writes a log message with a specific level, placeholder pattern, and messages.
     *
     * @param level    The log level (INFO, WARN, ERROR).
     * @param messages The messages to log.
     * @author <a href="https://vigfoot.com">Vigfoot</a>
     */
    static void write(final Level level, final Object... messages) {
        if (level != Level.ALL && level.compareTo(LogUpFactoryBean.logUpProperties.getLevel()) < 0) return;
        if (messages == null || messages.length == 0) return;

        final StringBuilder logMessage = new StringBuilder();

        for (MessageFormatter messageFormatter : LogUpFactoryBean.messageFormatter)
            logMessage.append(messageFormatter.call(level, messages));

        logMessage.append(System.lineSeparator());

        LogFactory.console(logMessage.toString());
        if (LogUpFactoryBean.logUpProperties.getFileRecord() != null)
            LogFactory.file(logMessage.toString());
    }

    static void writeWithoutMessageFormatter(final Level level, final Object... messages) {
        if (level != Level.ALL && level.compareTo(LogUpFactoryBean.logUpProperties.getLevel()) < 0) return;
        if (messages == null || messages.length == 0) return;

        final StringBuilder logMessage = new StringBuilder();

        for (Object message : messages)
            logMessage.append(message);

        logMessage.append(System.lineSeparator());

        LogFactory.console(logMessage.toString());
        if (LogUpFactoryBean.logUpProperties.getFileRecord() != null)
            LogFactory.file(logMessage.toString());
    }

    /**
     * A factory class for handling log output to console and files.
     *
     * @author <a href="https://vigfoot.com">Vigfoot</a>
     */
    @Setter
    public static class LogFactory {
        private String buffer;

        /**
         * Outputs the log message to the console.
         *
         * @param msg The log message to output.
         * @author <a href="https://vigfoot.com">Vigfoot</a>
         */
        protected static void console(final String msg) {
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
        protected static void file(String msg) {
            final FileRecorder fileRecorder = LogUpFactoryBean.logUpProperties.getFileRecord(); // readonly

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
                String plainMessage = msg.replaceAll("\\u001B\\[[0-9]+m", ""); // TIP: 치환 작업 - 파일 쓰기에는 콘솔 색상 아스키 코드 포함 안함

                outputStream.write(plainMessage.getBytes(StandardCharsets.UTF_8));
                outputStream.flush();
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace(System.err);
            }
        }


        /**
         * Logs a debug message in a JUnit test.
         *
         * @param msg The message to log.
         * @author <a href="https://vigfoot.com">Vigfoot</a>
         */
        public void test(Object... msg) {
            final List<Object> msgList = new ArrayList<>(Arrays.asList(msg));
            if (StringUtils.hasText(buffer)) msgList.add(buffer);

            write(Level.TEST, msgList.stream()
                    .map(String::valueOf)
                    .map(m -> m.replaceAll("\\u001B\\[[0-9]+m", ""))
                    .toArray());
        }

        /**
         * Logs an debug message.
         *
         * @param msg The message to log.
         * @author <a href="https://vigfoot.com">Vigfoot</a>
         */
        public void debug(Object... msg) {
            final List<Object> msgList = new ArrayList<>(Arrays.asList(msg));
            if (StringUtils.hasText(buffer)) msgList.add(buffer);

            write(Level.DEBUG, msgList.toArray());
        }

        /**
         * Logs an informational message.
         *
         * @param msg The message to log.
         * @author <a href="https://vigfoot.com">Vigfoot</a>
         */
        public void info(Object... msg) {
            final List<Object> msgList = new ArrayList<>(Arrays.asList(msg));
            if (StringUtils.hasText(buffer)) msgList.add(buffer);

            write(Level.INFO, msgList.toArray());
        }

        /**
         * Logs a warning message.
         *
         * @param msg The message to log.
         * @author <a href="https://vigfoot.com">Vigfoot</a>
         */
        public void warn(Object... msg) {
            final List<Object> msgList = new ArrayList<>(Arrays.asList(msg));
            if (StringUtils.hasText(buffer)) msgList.add(buffer);

            write(Level.WARN, msgList.toArray());
        }

        /**
         * Logs an error message.
         *
         * @param msg The message to log.
         * @author <a href="https://vigfoot.com">Vigfoot</a>
         */
        public void error(Object... msg) {
            final List<Object> msgList = new ArrayList<>(Arrays.asList(msg));
            if (StringUtils.hasText(buffer)) msgList.add(buffer);

            write(Level.ERROR, msgList.toArray());
        }

        protected static void initConsole() {
            LogUpFactoryBean.initialize(); // touch
            console("=================================================================================================================================================================" + System.lineSeparator());
            console("Log Up by forest full's vigfoot" + System.lineSeparator());
            console("=================================================================================================================================================================" + System.lineSeparator());
        }
    }
}