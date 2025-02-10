package com.forestfull;

import com.forestfull.lombok.ObservableArguments;
import com.forestfull.logger.util.Log;

public class Test {

    @ObservableArguments
    void test(Object... args){
        Log.warn(args);
    }

    public static void main(String[] args) {

        new Test().test();

        Log.error("Hello World");
        Log.info("Hello World");
    }
}
