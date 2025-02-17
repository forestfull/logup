package com.forestfull.logger.util;

import com.forestfull.logger.Level;

import java.io.*;
import java.nio.charset.Charset;
import java.util.Date;

public class Log {

    private final static String CHARSET_UTF_8 = "UTF-8";
    final static String newLine = System.getProperty("line.separator");

    private Log() {
    }

    /**
     * @param msg anything
     */
    public static void info(Object... msg) {
        write(Level.INFO, msg);
    }

    /**
     * @param msg anything
     */
    public static void warn(Object... msg) {
        write(Level.WARN, msg);
    }

    /**
     * @param msg anything
     */
    public static void error(Object... msg) {
        write(Level.ERROR, msg);
    }

    static void write(final Level level, final Object... messages) {
        write(level, KoLoggerFactoryBean.logFormatter.getPlaceholder(), messages);
    }

    static void write(final Level level, final String placeholder, final Object... messages) {
        if (level.compareTo(KoLoggerFactoryBean.level) < 0) return;
        if (messages == null || messages.length == 0) return;

        StackTraceElement stackTrace = Thread.currentThread().getStackTrace()[4];
        final String now = KoLoggerFactoryBean.logFormatter.getDateTimeFormat().format(new Date());
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
                .replace(LogFormatter.MessagePattern.NEW_LINE, Log.newLine);

        LogFactory.console(logMessage);
        if (KoLoggerFactoryBean.fileRecorder != null)
            LogFactory.file(logMessage);
    }

    static class LogFactory {
        protected static synchronized void console(final String msg) {
            final Writer fdOut = new PrintWriter(new FileWriter(FileDescriptor.out));

            try {
                fdOut.write(msg);
                fdOut.flush();

            } catch (IOException e) {
                e.printStackTrace(System.err);
            }
        }

        protected static synchronized void file(String msg) {
            final FileRecorder fileRecorder = KoLoggerFactoryBean.fileRecorder;

            String logFileDirectory = fileRecorder.getLogFileDirectory();

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
                outputStream.write((msg.getBytes(Charset.forName(CHARSET_UTF_8))));
                outputStream.flush();
                outputStream.close();

            } catch (IOException e) {
                e.printStackTrace(System.err);
            }
        }
    }
}
