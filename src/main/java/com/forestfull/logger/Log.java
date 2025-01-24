package com.forestfull.logger;


import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
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

    public static class Pattern {
        public final static String DATETIME = "{datetime}";
        public final static String THREAD = "{thread}";
        public final static String LEVEL = "{level}";
        public final static String MESSAGE = "{msg}";
        public final static String NEW_LINE = "{new-line}";
    }

    private final static String CHARSET_UTF_8 = "UTF-8";
    private static LogFactory logFactory = null;
    private static Log instance = null;

    protected static KorLoggerFactoryBean factoryBean = null;

    private final static String newLine = System.getProperty("line.separator");

    private Log() {
    }

    public static Log getInstance() {
        if (Log.instance == null)
            Log.instance = new Log();
        if (Log.logFactory == null)
            Log.logFactory = new LogFactory();
        if (Log.factoryBean == null)
            Log.factoryBean = new KorLoggerFactoryBean();

        return instance;
    }

    public Log next() {
        logFactory.console(Log.newLine);
        logFactory.file(Log.newLine);
        return this;
    }

    public static Log fine(String msg) {
        return getInstanceAndWrite(Level.FINE, msg);
    }

    public static Log conf(String msg) {
        return getInstanceAndWrite(Level.CONFIG, msg);
    }

    public static Log info(String msg) {
        return getInstanceAndWrite(Level.INFO, msg);
    }

    public static Log warn(String msg) {
        return getInstanceAndWrite(Level.WARNING, msg);
    }

    public Log andFine(String msg) {
        write(Level.FINE, msg);
        return this;
    }

    public Log andConf(String msg) {
        write(Level.CONFIG, msg);
        return this;
    }

    public Log andInfo(String msg) {
        write(Level.INFO, msg);
        return this;
    }

    public Log andWarn(String msg) {
        write(Level.WARNING, msg);
        return this;
    }

    private static Log getInstanceAndWrite(Level level, String msg) {
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
                final String now = formatter.getDatetime().format(new Date());
                final StringBuilder msgBuilder = new StringBuilder();

                for (Object message : messages)
                    msgBuilder.append(message);

                final String logMessage = formatter
                        .getPlaceHolder()
                        .replace(Pattern.DATETIME, now)
                        .replace(Pattern.THREAD, currentThreadName)
                        .replace(Pattern.LEVEL, level.getName().substring(0, 4))
                        .replace(Pattern.MESSAGE, msgBuilder.toString())
                        .replace(Pattern.NEW_LINE, Log.newLine);

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

        /* TODO: 파일 쓰기 */
        private synchronized void file(String msg) {

        }
    }
}
