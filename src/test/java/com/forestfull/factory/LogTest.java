package com.forestfull.factory;

import com.forestfull.logger.Log;
import org.junit.jupiter.api.Test;

import java.io.IOException;


class LogTest {

    @Test
    void test() throws IOException {


        Log.fine("Hello World")
                .andConfig(")write(")
                .andInfo("this Info")
                .andWarn("this High Level")
        ;
    }
}