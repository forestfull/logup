package com.forestfull.logger.util;

import com.forestfull.logger.Level;
import com.forestfull.logger.config.ConfigLoader;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Date;

public class Log {

    private final static String CHARSET_UTF_8 = "UTF-8";
    private static LogFactory logFactory = null;
    private static Log instance = null;

    protected static KoLoggerFactoryBean factoryBean = null;

    protected final static String newLine = System.getProperty("line.separator");

    private Log() {
    }

    public static Log getInstance() {
        return Log.instance == null ? getInstance(KoLoggerFactoryBean.builder().build()) : Log.instance;
    }

    public static Log getInstance(KoLoggerFactoryBean factoryBean) {
        if (Log.instance == null) {
            Log.instance = new Log();

            // TODO: 스프링 연동 작업 필요
            final ConfigLoader configLoader = new ConfigLoader();

            if (Log.logFactory == null)
				Log.logFactory = new LogFactory();

			if (Log.factoryBean == null)
				Log.factoryBean = factoryBean.clone();
        }

        return Log.instance;
    }

    public Log next() {
        logFactory.console(Log.newLine);
        logFactory.file(Log.newLine);
        return this;
    }

    public static Log info(Object... msg) {
        Level configLevel = Log.factoryBean == null? Level.ALL : Log.factoryBean.getLevel();
        return configLevel.compareTo(Level.INFO) > 0 ? Log.instance : getInstanceAndWrite(Level.INFO, msg);
    }

    public static Log warn(Object... msg) {
        Level configLevel = Log.factoryBean == null? Level.ALL : Log.factoryBean.getLevel();
		return configLevel.compareTo(Level.WARN) > 0 ? Log.instance : getInstanceAndWrite(Level.WARN, msg);
    }

    public static Log error(Object... msg) {
        Level configLevel = Log.factoryBean == null? Level.ALL : Log.factoryBean.getLevel();
        return configLevel.compareTo(Level.ERROR) > 0 ? Log.instance : getInstanceAndWrite(Level.ERROR, msg);
    }

    public Log andInfo(Object... msg) {
        Level configLevel = Log.factoryBean.getLevel();
        if (configLevel.compareTo(Level.INFO) > 0){
			return Log.instance;

		} else {
            write(Level.INFO, msg);
            return this;

		}
    }

    public Log andWarn(Object... msg) {
        Level configLevel = Log.factoryBean.getLevel();
        if (configLevel.compareTo(Level.WARN) > 0){
			return Log.instance;

		} else {
            write(Level.WARN, msg);
            return this;
		}
    }

    public Log andError(Object... msg) {
        Level configLevel = Log.factoryBean.getLevel();
        if (configLevel.compareTo(Level.ERROR) > 0) {
            return Log.instance;

        } else {
            write(Level.ERROR, msg);
            return this;

        }
    }

    private static Log getInstanceAndWrite(Level level, Object... msg) {
        Log.getInstance().write(level, msg);
        return Log.instance;
    }

    protected void write(final Level level, final Object... messages) {
        if (messages == null || messages.length == 0) return;

        final String currentThreadName = Thread.currentThread().getName();
        KoLoggerFactoryBean.logConsoleExecutor.submit(new Runnable() {
            public void run() {
                LogFormatter formatter = factoryBean.getLogFormatter();
                final String now = formatter.getDateTimeFormat() != null ? formatter.getDateTimeFormat().format(new Date()) : "";
                final StringBuilder msgBuilder = new StringBuilder();

				for (int i = 0; i < messages.length; i++) {
					Object message = messages[i];
					msgBuilder.append(message);
				}

				final String logMessage = formatter
                        .getPlaceholder()
                        .replace(LogFormatter.MessagePattern.DATETIME, now)
                        .replace(LogFormatter.MessagePattern.THREAD, currentThreadName)
                        .replace(LogFormatter.MessagePattern.LEVEL, level == Level.ALL ? "----" : level.name().substring(0, 4))
                        .replace(LogFormatter.MessagePattern.MESSAGE, msgBuilder.toString())
                        .replace(LogFormatter.MessagePattern.NEW_LINE, Log.newLine);

                logFactory.console(logMessage);
                logFactory.file(logMessage);
            }
        });
    }

    private static class LogFactory {
        private void console(final String msg) {
            final byte[] msgStream = msg.getBytes(Charset.forName(CHARSET_UTF_8));
            final FileOutputStream fdOut = new FileOutputStream(FileDescriptor.out);

            try {
                fdOut.write(msgStream);
                fdOut.flush();

            } catch (IOException e) {
                e.printStackTrace(System.err);
            }
        }

        private void file(String msg) {
            final FileRecorder fileRecorder = factoryBean.getFileRecorder();

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
