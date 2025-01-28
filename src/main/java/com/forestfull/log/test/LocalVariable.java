package com.forestfull.log.test;

import com.forestfull.log.config.Observable;

public class LocalVariable {


    public Object someMethod(String arg) {

        @Observable Object o = arg + System.currentTimeMillis();


        return null;
    }
}
