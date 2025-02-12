package com.forestfull;

import com.forestfull.logger.util.Log;
import com.forestfull.logger.Observable;

public class Test {

    @Observable
    public void test(String arg){
        Log.warn(arg);
    }


    public static void main(String[] args) {
        new Test().test("Hello World");

        Log.error("Hello World");
        Log.info("Hello World");
    }
}