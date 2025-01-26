package com.forestfull.devops.test;

import com.forestfull.devops.config.Observable;

public class Method {


    @Observable
    public Object someMethod() {

        @Observable Object o = someMethod();


        return null;
    }
}
