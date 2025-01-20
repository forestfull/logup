package com.forestfull.factory;

import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;


class KorLoggerTest {

    @Test
    void test() throws IOException {
        KorLogger log = KorLogger.getInstance();
        log.test("Hello World").next();
    }
}
