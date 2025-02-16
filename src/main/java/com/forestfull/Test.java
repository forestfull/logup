package com.forestfull;

import com.forestfull.logger.lombok.Observable;
import com.forestfull.logger.util.Log;

import java.io.*;

public class Test {


    @Observable
    public Object test(String arg) {
        Log.warn(arg);

        return "hi";
    }


    public static void main(String[] args) {
        new Test().test("Hello World");

        @Observable
        Object helloWorld = new Test().test("Hello World");

    }
}