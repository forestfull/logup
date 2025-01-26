package com.forestfull.devops.test;

import com.forestfull.devops.logger.Log;

public class Main {
    public static void main(String[] args) {
        Log.customConfiguration();

        Constructor constructor = new Constructor();
        Log.info("Constructor", constructor);

        LocalVariable localVariable = new LocalVariable();
        Log.info("LocalVariable", localVariable);

        Log.info("Method");
        Method method = new Method();
        method.someMethod("someMethod");




    }
}
