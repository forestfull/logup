package com.forestfull.factory;


import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Date;


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

    public Log trace(final Object... messages) {
        if (Level.TRACE.compareTo(factoryBean.getFormatter().getLevel()) < 0)
            return this;

        instance.write(Level.TRACE, messages);
        return this;
    }

    public Log debug(final Object... messages) {
        if (Level.DEBUG.compareTo(factoryBean.getFormatter().getLevel()) < 0)
            return this;

        instance.write(Level.DEBUG, messages);
        return this;
    }

    public Log info(final Object... messages) {
        if (Level.INFO.compareTo(factoryBean.getFormatter().getLevel()) < 0)
            return this;

        instance.write(Level.INFO, messages);
        return this;
    }

    public Log warn(final Object... messages) {
        if (Level.WARN.compareTo(factoryBean.getFormatter().getLevel()) < 0)
            return this;

        instance.write(Level.WARN, messages);
        return this;
    }

    public Log error(final Object... messages) {
        if (Level.ERROR.compareTo(factoryBean.getFormatter().getLevel()) < 0)
            return this;

        instance.write(Level.ERROR, messages);
        return this;
    }

    public Log fatal(final Object... messages) {
        if (Level.FATAL.compareTo(factoryBean.getFormatter().getLevel()) < 0)
            return this;

        instance.write(Level.FATAL, messages);
        return this;
    }

    public Log next() {
        logFactory.console(Log.newLine);
        logFactory.file(Log.newLine);
        return this;
    }

    private void write(final Level level, final Object... messages) {
        if (messages == null || messages.length == 0)
            return;

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
                        .replace(Pattern.THREAD, Thread.currentThread().getName())
                        .replace(Pattern.LEVEL, level.name())
                        .replace(Pattern.MESSAGE, msgBuilder.toString())
                        .replace(Pattern.NEW_LINE, Log.newLine);

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
        private void file(String msg) {

        }
    }
}
