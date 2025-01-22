package com.forestfull.factory;


import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;


public class Log {

    private final static String CHARSET_UTF_8 = "UTF-8";
    private static LogFactory logFactory = null;
    private static Log instance = null;

    protected static KorLoggerFactoryBean factoryBean = null;

    private final static String newLine = System.getProperty("line.separator");

    private Log() {
    }

    public static Log getInstance() {
        if (Log.instance == null) Log.instance = new Log();
        if (Log.logFactory == null) Log.logFactory = new LogFactory();
        if (Log.factoryBean == null) Log.factoryBean = new KorLoggerFactoryBean();
        // 어노테이션 스타일도 고려필요

        return instance;
    }

    public Log trace(Object... messages) {
        if (Level.TRACE.compareTo(factoryBean.getFormatter().getLevel()) < 0) return this;

        return instance.write(messages).next();
    }

    public Log debug(Object... messages) {
        if (Level.DEBUG.compareTo(factoryBean.getFormatter().getLevel()) < 0) return this;

        return instance.write(messages).next();
    }

    public Log info(Object... messages) {
        if (Level.INFO.compareTo(factoryBean.getFormatter().getLevel()) < 0) return this;

        return instance.write(messages).next();
    }

    public Log warn(Object... messages) {
        if (Level.WARN.compareTo(factoryBean.getFormatter().getLevel()) < 0) return this;

        return instance.write(messages).next();
    }

    public Log error(Object... messages) {
        if (Level.ERROR.compareTo(factoryBean.getFormatter().getLevel()) < 0) return this;

        return instance.write(messages).next();
    }

    public Log fatal(Object... messages) {
        if (Level.FATAL.compareTo(factoryBean.getFormatter().getLevel()) < 0) return this;

        return instance.write(messages).next();
    }

    public Log next() {
        return instance.write(Log.newLine);
    }

    private Log write(Object... messages) {
        if (messages == null || messages.length == 0) return this;

        try {
            final StringBuilder msgBuilder = new StringBuilder();
            for (Object message : messages)
                msgBuilder.append(message);

            logFactory.console(msgBuilder.toString());

        } catch (IOException e) {
            e.printStackTrace(System.err);
        }

        return this;
    }

    private static class LogFactory {
        private void console(String msg) throws IOException {
            if (msg == null) return;

            final byte[] msgStream = msg.getBytes(Charset.forName(CHARSET_UTF_8));
            final FileOutputStream fdOut = new FileOutputStream(FileDescriptor.out);

            try {
                fdOut.write(msgStream);

            } catch (IOException e) {
                e.printStackTrace(System.err);

            } finally {
                fdOut.flush();
            }
        }

        /* TODO: 파일 쓰기 */
        private void file(String msg) throws IOException {
            if (msg == null) return;

        }
    }
}
