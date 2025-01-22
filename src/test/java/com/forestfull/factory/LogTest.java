package com.forestfull.factory;

import org.junit.jupiter.api.Test;

import java.io.IOException;


class LogTest {

    @Test
    void test() throws IOException {
        Log.getInstance().trace("hi").info("hello").warn("warn");

    }
}
