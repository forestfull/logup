package com.forestfull.log;

import com.forestfull.log.logger.Log;
import com.forestfull.log.logger.ObservableAspect;
import com.forestfull.log.test.Constructor;

public class Main {

    public static void main(String[] args) {


        Log.customConfiguration();

        Constructor constructor = new Constructor();
        constructor.test("d");
        constructor.test("1");


        Log.conf("hi");
    }
}
