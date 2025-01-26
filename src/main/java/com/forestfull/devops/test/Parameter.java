package com.forestfull.devops.test;

import com.forestfull.devops.config.Observable;

public class Parameter {

    void someMethod(@Observable Object someParameter) {
        System.out.println(someParameter);
    }

}
