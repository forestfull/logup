package com.forestfull.log.test;

import com.forestfull.log.config.Observable;
import com.forestfull.log.logger.Log;

@Observable
public class Constructor {

    private String test;
    private String t1test;

    public void test(String a) {
        Log.error(a);
    }

    @Observable
    public void testMethod(String param1, int param2) {
        Log.warn(param1);
        ;
    }
}
