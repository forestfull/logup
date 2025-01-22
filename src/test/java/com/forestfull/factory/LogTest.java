package com.forestfull.factory;

import org.junit.jupiter.api.Test;

import java.io.IOException;


class LogTest {

    @Test
    void test() throws IOException {
        StringBuilder test1 = new StringBuilder();
        extracted(test1); Log.getInstance().trace(test1);

        StringBuilder test2 = new StringBuilder();
        Log.getInstance().info(test2); extracted(test2);

        StringBuilder test3 = new StringBuilder();
        extracted(test3); Log.getInstance().warn(test3);
    }

    private static void extracted(StringBuilder test1) {
        for (int i = 0; i < 10000; i++) {
            test1.append(i).append(',');
        }
    }
}
