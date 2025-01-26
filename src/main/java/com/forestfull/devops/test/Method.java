package com.forestfull.devops.test;

import com.forestfull.devops.config.Observable;

public class Method {

    @Observable
    void someMethod(Object someParameter) {
        System.out.println(someParameter);
    }

}
