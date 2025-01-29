package com.forestfull.log.test;

import com.forestfull.log.config.Observable;

@Observable
public class Constructor {

    private String test;
    private String t1test;

    public void test(String a) {
        System.out.println(a);
    }

    @Observable
    public void testMethod(String param1, int param2) {
        System.out.println(param1);;
    }
}
