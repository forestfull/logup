package com.forestfull.devops.test;

import com.forestfull.devops.config.Observable;

public class LocalVariable {


    public Object someMethod(String arg) {

        @Observable Object o = arg + System.currentTimeMillis();


        return null;
    }
}
