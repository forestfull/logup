package com.forestfull.factory;

import com.forestfull.logger.KorLoggerFactoryBean;
import com.forestfull.logger.Log;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.logging.Level;


class LogTest {

    @Test
    void test() throws IOException {

        Log.customConfiguration(KorLoggerFactoryBean.builder()
                .formatter(KorLoggerFactoryBean.Formatter.builder()
                        .level(Level.WARNING)
                        .placeHolder(Log.Pattern.MESSAGE + Log.Pattern.NEW_LINE)
                        .build())
                .fileRecorder(KorLoggerFactoryBean.FileRecorder.builder()
                        .logFileDirectory("logs")
                        .dateFormat(new SimpleDateFormat("yyyy-MM-dd"))
                        .placeHolder(Log.Pattern.DATETIME + "test-log.log")
                        .build())
                .build());

        Log.fine("Hello World")
                .andConf(")write(")
                .andInfo("this Info")
                .andWarn("this High Level")
        ;
    }
}