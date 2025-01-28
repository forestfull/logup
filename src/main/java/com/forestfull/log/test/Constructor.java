package com.forestfull.log.test;

import com.forestfull.log.config.Observable;
import lombok.Builder;

@Observable
public class Constructor {

    private String test;
    private String t1test;


    public void test(String a){
        System.out.println(a);
    }
}
