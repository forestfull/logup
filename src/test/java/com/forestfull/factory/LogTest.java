package com.forestfull.factory;

import org.junit.jupiter.api.Test;

import java.io.IOException;


class LogTest {

    @Test
    void test() throws IOException {
        Log log = Log.getInstance();
        log.trace("Hello World").debug("Hello World").info("Hello World").warn("Hello World").error("Hello World").fatal("Hello World").next();
    }
}
