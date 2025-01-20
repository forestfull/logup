package com.forestfull.factory;


import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;


public class KorLogger {


    private final static String CHARSET_UTF_8 = "UTF-8";
    private final static String newLine = System.getProperty("line.separator");
    private static KorLogger instance = null;

    private KorLogger() {
    }

    public static KorLogger getInstance() {
        if (instance == null)
            instance = new KorLogger();

        return instance;
    }

    public KorLogger next() {
        try {
            return write(KorLogger.newLine);

        } catch (IOException e) {
            e.printStackTrace(System.err);

        }

        return this;
    }

    public KorLogger test(String msg) {
        try {
            write(msg);
        } catch (IOException e) {
            e.printStackTrace(System.err);
        }
        return this;
    }

    private KorLogger write(String msg) throws IOException {
        if (msg == null) return this;

        final byte[] msgStream = msg.getBytes(Charset.forName(CHARSET_UTF_8));
        final FileOutputStream fdOut = new FileOutputStream(FileDescriptor.out);

        try {
            fdOut.write(msgStream);

        } catch (IOException e) {
            e.printStackTrace(System.err);

        } finally {
            fdOut.flush();
        }

        return this;
    }
}