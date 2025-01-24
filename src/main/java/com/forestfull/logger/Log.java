package com.forestfull.logger;


import java.io.File;
import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;

/**
 * <ul>
 * <li><s>SEVERE</s> (deprecated)
 * <li><b>WARNING</b> (highest value)
 * <li><b>INFO</b>
 * <li><b>CONFIG</b>
 * <li><b>FINE</b> (lowest value)
 * <li><s>FINER</s> (deprecated)
 * <li><s>FINEST</s> (deprecated)
 * </ul>
 */
public class Log {

    public static class MessagePattern {
        public final static String DEFAULT = MessagePattern.DATETIME + " [" + MessagePattern.THREAD + ":" + MessagePattern.LEVEL + "] - " + MessagePattern.MESSAGE + MessagePattern.NEW_LINE;
        public final static String DATETIME = "{datetime}";
        public final static String THREAD = "{thread}";
        public final static String LEVEL = "{level}";
        public final static String MESSAGE = "{msg}";
        public final static String NEW_LINE = "{new-line}";
    }

    public static class FilePattern {
        public final static String DEFAULT = FilePattern.DATE + ".log";
        public final static String DATE = "{date}";
    }


    private final static String CHARSET_UTF_8 = "UTF-8";
    private static LogFactory logFactory = null;
    private static Log instance = null;

    private static KorLoggerFactoryBean factoryBean = null;

    private final static String newLine = System.getProperty("line.separator");

    private Log() {
    }

    /**
     * 직접 선언할 경우 쓰는 함수
     */
    public static void customConfiguration(KorLoggerFactoryBean factoryBean) {
        optionalDefaultFactoryBean(factoryBean);
        Log.factoryBean = factoryBean;

        /*TODO: 이 때 최초 초기화 일 것으로 기대하여 어노테이션 스캔도 살짝 넣어야 함*/
    }

    private static void optionalDefaultFactoryBean(KorLoggerFactoryBean factoryBean) {
        if (factoryBean.getFormatter() == null) {
            factoryBean.setFormatter(KorLoggerFactoryBean.Formatter.builder()
                    .placeHolder(MessagePattern.DEFAULT)
                    .datetime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"))
                    .level(Level.ALL)
                    .build());

        }
        if (factoryBean.getFileRecorder() == null) {
            factoryBean.setFileRecorder(KorLoggerFactoryBean.FileRecorder.builder()
                    .logFileDirectory("")
                    .dateFormat(new SimpleDateFormat("yyyy_MM_dd"))
                    .placeHolder(MessagePattern.DATETIME + ".log")
                    .build());
        }
    }

    public static Log getInstance() {
        if (Log.instance == null)
            Log.instance = new Log();
        if (Log.logFactory == null)
            Log.logFactory = new LogFactory();
        if (Log.factoryBean == null) {
            final KorLoggerFactoryBean bean = KorLoggerFactoryBean.builder().build();
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
        return getInstanceAndWrite(Level.WARNING, msg);
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

        Level configLevel = Log.factoryBean.getFormatter().getLevel();
        if (configLevel.intValue() > level.intValue()) return;

        final String currentThreadName = Thread.currentThread().getName();
        KorLoggerFactoryBean.logConsoleExecutor.submit(new Runnable() {
            @Override
            public void run() {
                KorLoggerFactoryBean.Formatter formatter = factoryBean.getFormatter();
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

            } catch (IOException e) {
                e.printStackTrace(System.err);

            } finally {
                try {
                    fdOut.flush();
                } catch (IOException ignore) {
                }
            }
        }

        private synchronized void file(String msg) {
            final KorLoggerFactoryBean.FileRecorder fileRecorder = factoryBean.getFileRecorder();

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
                    if (!logFile.isFile()) logFile.deleteOnExit();
                    boolean isSucceed = logFile.createNewFile();
                    if (!isSucceed) throw new IOException("Failed to create log directory: " + rootDirectory);
                }

                final FileOutputStream outputStream = new FileOutputStream(logFile, true);
                outputStream.write((msg.getBytes(Charset.forName(CHARSET_UTF_8))));
                outputStream.flush();

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
