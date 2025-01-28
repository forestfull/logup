package com.forestfull.log.test;

import com.forestfull.log.config.Observable;

public class Method {

    @Observable
    void someMethod(Object someParameter) {
        System.out.println(someParameter);
    }

}
