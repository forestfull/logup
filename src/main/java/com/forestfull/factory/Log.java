package com.forestfull.factory;


import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;


public class Log {


    private final static String CHARSET_UTF_8 = "UTF-8";
    private static LogFactory logFactory = null;
    private static Log instance = null;

    private final static String newLine = System.getProperty("line.separator");

    private Log() {
    }

    public static Log getInstance() {
        if (instance == null) instance = new Log();
        if (logFactory == null) logFactory = new LogFactory();

        return instance;
    }

    public Log trace(Object... messages) {
        return instance.write(messages).next();
    }

    public Log debug(Object... messages) {
        return instance.write(messages).next();
    }

    public Log info(Object... messages) {
        return instance.write(messages).next();
    }

    public Log warn(Object... messages) {
        return instance.write(messages).next();
    }

    public Log error(Object... messages) {
        return instance.write(messages).next();
    }

    public Log fatal(Object... messages) {
        return instance.write(messages).next();
    }

    public Log next() {
        return instance.write(Log.newLine);
    }

    private Log write(Object... messages) {
        if (messages == null || messages.length == 0) return this;

        try {
            for (Object message : messages) logFactory.console(String.valueOf(message));

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