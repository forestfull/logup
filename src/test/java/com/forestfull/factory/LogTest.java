package com.forestfull.factory;

import com.forestfull.logger.KorLoggerFactoryBean;
import com.forestfull.logger.Log;
import org.junit.jupiter.api.Test;

import java.io.IOException;


class LogTest {

    @Test
    void test() throws IOException {

        Log.customConfiguration(KorLoggerFactoryBean.builder().build());

        Log.fine("Hello World")
                .andConf(")write(")
                .andInfo("this Info")
                .andWarn("this High Level")
        ;
    }
}