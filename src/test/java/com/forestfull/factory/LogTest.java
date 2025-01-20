package com.forestfull.factory;

import org.junit.jupiter.api.Test;

import java.io.IOException;


class LogTest {

    @Test
    void test() throws IOException {
        Log.getInstance().warn("아 재밋당", "sfasd", 12).error("error???");
    }
}
