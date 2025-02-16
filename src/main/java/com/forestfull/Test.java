package com.forestfull;

import com.forestfull.logger.lombok.Observable;
import com.forestfull.logger.util.Log;

import java.io.*;

public class Test {


    @Observable
    public Object test(String arg){
                Log.warn(arg);

        return "hi";
    }


    public static void main(String[] args) {
        new Test().test("Hello World");

        @Observable
        Object helloWorld = new Test().test("Hello World");


        Test.log("hi", "????");
    }


    private static final Writer writer = new PrintWriter(new FileWriter(FileDescriptor.out));

    public static void log(String level, String message) {
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        StackTraceElement element = stackTrace[2]; // 호출한 메서드의 스택 요소
        String className = element.getClassName();
        String methodName = element.getMethodName();
        int lineNumber = element.getLineNumber();
        String logMessage = String.format("%s %s.%s(%s:%d) %s - %s",
                java.time.LocalDateTime.now(),
                className,
                methodName,
                element.getFileName(),
                lineNumber,
                level,
                message);
        try {
            writer.write(logMessage + System.lineSeparator());
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}