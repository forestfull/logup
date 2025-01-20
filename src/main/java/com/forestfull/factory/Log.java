package com.forestfull.factory;


import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;


public class Log {


    private final static String CHARSET_UTF_8 = "UTF-8";
    private static Log instance = null;
    private static LogFactory logFactory = null;

    private final static String newLine = System.getProperty("line.separator");

    private Log() {
    }

    public static Log getInstance() {
        if (instance == null) {
            instance = new Log();
            logFactory = new LogFactory();
        }

        return instance;
    }

    public Log next() {
        try {
            logFactory.console(Log.newLine);
        } catch (IOException e) {
            e.printStackTrace(System.err);
        }

        return this;
    }

    public Log trace(String msg) {
        try {
            logFactory.console(msg);
            next();
        } catch (IOException e) {
            e.printStackTrace(System.err);
        }

        return this;
    }

    public Log debug(String msg) {
        try {
            logFactory.console(msg);
            next();
        } catch (IOException e) {
            e.printStackTrace(System.err);
        }

        return this;
    }

    public Log info(String msg) {
        try {
            logFactory.console(msg);
            next();
        } catch (IOException e) {
            e.printStackTrace(System.err);
        }

        return this;
    }

    public Log warn(String msg) {
        try {
            logFactory.console(msg);
            next();
        } catch (IOException e) {
            e.printStackTrace(System.err);
        }

        return this;
    }

    public Log error(String msg) {
        try {
            logFactory.console(msg);
            next();
        } catch (IOException e) {
            e.printStackTrace(System.err);
        }

        return this;
    }

    public Log fatal(String msg) {
        try {
            logFactory.console(msg);
            next();
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