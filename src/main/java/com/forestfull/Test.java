package com.forestfull;

import com.forestfull.logger.Level;
import com.forestfull.logger.util.FileRecorder;
import com.forestfull.logger.util.KoLoggerFactoryBean;
import com.forestfull.logger.util.Log;
import com.forestfull.logger.util.LogFormatter;

public class Test {
    public static void main(String[] args) {

        KoLoggerFactoryBean.builder()
                .level(Level.ERROR)
                .logFormatter(LogFormatter.builder().build())
                .fileRecorder(FileRecorder.builder().logFileDirectory("logs/").build())
                .start();

        Log.warn("Hello World");

        Log.error("Hello World");


        KoLoggerFactoryBean.builder()
                .level(Level.INFO)
                .logFormatter(LogFormatter.builder().build())
                .fileRecorder(FileRecorder.builder().logFileDirectory("logs/").build())
                .start();

        Log.info("Hello World");
    }
}
