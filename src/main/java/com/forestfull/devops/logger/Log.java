package com.forestfull.devops.logger;


import com.forestfull.devops.config.Observable;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;
import java.util.logging.Level;


public class Log {

    public static class MessagePattern {
        private final static String DEFAULT = MessagePattern.DATETIME + " [" + MessagePattern.THREAD + ":" + MessagePattern.LEVEL + "] - " + MessagePattern.MESSAGE + MessagePattern.NEW_LINE;
        private final static String DATETIME = "{datetime}";
        private final static String THREAD = "{thread}";
        private final static String LEVEL = "{level}";
        private final static String MESSAGE = "{msg}";
        private final static String NEW_LINE = "{new-line}";
    }

    public static class FilePattern {
        private final static String[] filePath = System.getProperty("user.dir").split(System.lineSeparator().equals("\r\n") ? File.separator + File.separator : File.separator);
        private final static String PROJECT_NAME = filePath[filePath.length - 1];
        private final static String DATE = "{date}";
        private final static String DEFAULT = DATE + "-" + PROJECT_NAME + ".log";
    }


    private final static String CHARSET_UTF_8 = "UTF-8";
    private static LogFactory logFactory = null;
    private static Log instance = null;

    private static KoLoggerFactoryBean factoryBean = null;

    private final static String newLine = System.getProperty("line.separator");

    private Log() {
    }

    public static void customConfiguration(){
        customConfiguration(KoLoggerFactoryBean.builder().build());
    }

    /**
     * 직접 선언할 경우 쓰는 함수
     */
    public static void customConfiguration(KoLoggerFactoryBean factoryBean) {
        Objects.requireNonNull(factoryBean);
        optionalDefaultFactoryBean(factoryBean);
        Log.factoryBean = factoryBean;

        Class<?>[] annotatedClasses = LogAnnotationScanner.builder().annotation(Observable.class).build().getAnnotatedClasses();
        ObservableLogHandler.builder().target(annotatedClasses).build();
    }

    private static void optionalDefaultFactoryBean(KoLoggerFactoryBean factoryBean) {
        if (factoryBean.getLevel() == null) {
            factoryBean.setLevel(Level.ALL);
        }
        if (factoryBean.getFormatter() == null) {
            factoryBean.setFormatter(KoLoggerFactoryBean.Formatter.builder()
                    .placeHolder(MessagePattern.DEFAULT)
                    .datetime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"))
                    .build());

        }
        if (factoryBean.getFileRecorder() == null) {
            factoryBean.setFileRecorder(KoLoggerFactoryBean.FileRecorder.builder()
                    .logFileDirectory("logs/")
                    .dateFormat(new SimpleDateFormat("yyyy_MM_dd"))
                    .placeHolder(FilePattern.DATE + FilePattern.DEFAULT)
                    .build());
        }
    }

    private static Log getInstance() {
        if (Log.instance == null)
            Log.instance = new Log();
        if (Log.logFactory == null)
            Log.logFactory = new LogFactory();
        if (Log.factoryBean == null) {
            final KoLoggerFactoryBean bean = KoLoggerFactoryBean.builder().build();
            optionalDefaultFactoryBean(bean);
            Log.factoryBean = bean;
        }

        return instance;
    }

    public Log next() {
        logFactory.console(Log.newLine);
        logFactory.file(Log.newLine);
        return this;
    }

    public static Log fine(Object... msg) {
        return getInstanceAndWrite(Level.FINE, msg);
    }

    public static Log conf(Object... msg) {
        return getInstanceAndWrite(Level.CONFIG, msg);
    }

    public static Log info(Object... msg) {
        return getInstanceAndWrite(Level.INFO, msg);
    }

    public static Log warn(Object... msg) {
        Log instanceAndWrite = getInstanceAndWrite(Level.WARNING, msg);
        return Log.instance;
    }

    public Log andFine(Object... msg) {
        write(Level.FINE, msg);
        return this;
    }

    public Log andConf(Object... msg) {
        write(Level.CONFIG, msg);
        return this;
    }

    public Log andInfo(Object... msg) {
        write(Level.INFO, msg);
        return this;
    }

    public Log andWarn(Object... msg) {
        write(Level.WARNING, msg);
        return this;
    }

    private static Log getInstanceAndWrite(Level level, Object... msg) {
        Log.getInstance().write(level, msg);
        return Log.instance;
    }

    private void write(final Level level, final Object... messages) {
        if (messages == null || messages.length == 0) return;

        Level configLevel = Log.factoryBean.getLevel();
        if (configLevel.intValue() > level.intValue()) return;

        final String currentThreadName = Thread.currentThread().getName();
        KoLoggerFactoryBean.logConsoleExecutor.submit(new Runnable() {
            @Override
            public void run() {
                KoLoggerFactoryBean.Formatter formatter = factoryBean.getFormatter();
                final String now = formatter.getDatetime() != null ? formatter.getDatetime().format(new Date()) : "";
                final StringBuilder msgBuilder = new StringBuilder();

                for (Object message : messages)
                    msgBuilder.append(message);

                final String logMessage = formatter
                        .getPlaceHolder()
                        .replace(MessagePattern.DATETIME, now)
                        .replace(MessagePattern.THREAD, currentThreadName)
                        .replace(MessagePattern.LEVEL, level.getName().substring(0, 4))
                        .replace(MessagePattern.MESSAGE, msgBuilder.toString())
                        .replace(MessagePattern.NEW_LINE, Log.newLine);

                logFactory.console(logMessage);
                logFactory.file(logMessage);
            }
        });
    }

    private static class LogFactory {
        private synchronized void console(final String msg) {
            final byte[] msgStream = msg.getBytes(Charset.forName(CHARSET_UTF_8));
            final FileOutputStream fdOut = new FileOutputStream(FileDescriptor.out);

            try {
                fdOut.write(msgStream);
                fdOut.flush();

            } catch (IOException e) {
                e.printStackTrace(System.err);
            }
        }

        private synchronized void file(String msg) {
            final KoLoggerFactoryBean.FileRecorder fileRecorder = factoryBean.getFileRecorder();

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

                final File logFile = new File(logFileDirectory + File.separator + fileRecorder.getPlaceHolder().replace(FilePattern.DATE, ""));
                if (!logFile.exists()) {
                    boolean isSucceed = logFile.createNewFile();
                    if (!isSucceed) throw new IOException("Failed to create log file: " + logFile);

                } else if (logFile.isFile()) {
                    final String currentTimeFormatName = fileRecorder.getPlaceHolder().replace(FilePattern.DATE, fileRecorder.getDateFormat().format(new Date()));
                    final String existedTimeFormatName = fileRecorder.getPlaceHolder().replace(FilePattern.DATE, fileRecorder.getDateFormat().format(new Date(logFile.lastModified())));
                    if (!currentTimeFormatName.equals(existedTimeFormatName)) {
                        boolean isSucceed = logFile.renameTo(new File(existedTimeFormatName));
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
