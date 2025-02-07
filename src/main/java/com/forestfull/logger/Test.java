package com.forestfull.logger;

import com.forestfull.logger.annotation.ObservableArguments;
import com.forestfull.logger.util.LogAnnotationAssist;

import java.util.Arrays;

public class Test {

    @ObservableArguments
    void test(String msg, Object... args) {
        System.out.println(msg);
        System.out.println(Arrays.toString(args));
    }

    public static void main(String[] args) throws Exception {
        LogAnnotationAssist.init();


        Test test = new Test();
        test.test("hi", "hel", "lo" , " ","world");

    }
}
