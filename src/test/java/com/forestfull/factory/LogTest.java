package com.forestfull.factory;

import com.forestfull.logger.KoLoggerFactoryBean;
import com.forestfull.logger.Log;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.logging.Level;


class LogTest {

    @Test
    void test() throws IOException {

        Log.customConfiguration(KoLoggerFactoryBean.builder()
                .formatter(KoLoggerFactoryBean.Formatter.builder()
                        .level(Level.CONFIG)
                        .datetime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"))
                        .placeHolder(Log.MessagePattern.DEFAULT)
                        .build())
                .fileRecorder(KoLoggerFactoryBean.FileRecorder.builder()
                        .logFileDirectory("logs")
                        .dateFormat(new SimpleDateFormat("yyyy-MM-dd"))
                        .placeHolder(Log.FilePattern.DEFAULT)
                        .build())
                .build());

        for (int i = 0; i < 1000; i++) {
            Log.fine("Hello World")
                    .andConf(")write(")
                    .andInfo("this Info")
                    .andWarn("this High Level");
        }
        ;
    }
}