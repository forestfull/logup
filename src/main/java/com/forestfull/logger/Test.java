package com.forestfull.logger;

import com.forestfull.logger.annotation.ObservableArguments;
import com.forestfull.logger.util.LogAnnotationAssist;

import java.util.Arrays;

public class Test {
    @ObservableArguments
    public void test(String message, int number) {
        System.out.println("Original Method Execution: " + message + ", " + number);
    }

    public static void main(String[] args) throws Exception {
        // Javassist를 사용해 메서드 변환
        LogAnnotationAssist.modifyMethods(Test.class);

        // 변경된 메서드 실행
        new Test().test("Hello", 42);
    }
}
