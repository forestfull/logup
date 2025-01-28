package com.forestfull.log;

import com.forestfull.log.logger.Log;
import com.forestfull.log.logger.ObservableAspect;
import com.forestfull.log.test.Constructor;

import java.io.File;
import java.util.List;

public class Main {


    static List<Object> inFile(File file) {

        return null;
    }


    public static void main(String[] args) throws ClassNotFoundException {

        Log.customConfiguration();

        ObservableAspect.setPackageName("com.forestfull");

        Constructor build = Constructor.builder().t1test("123").test("324").build();
        build.test("a");

//        String property = System.getProperty("user.dir");
//        File[] files = new File(property).listFiles();
//        for (File file : files) {
//            if (file.exists()) {
//                inFile(file);
//            }
//
//        }


//        Log.customConfiguration();
//
//        Constructor constructor = new Constructor();
//        Log.info("Constructor: ", constructor);
//
//        LocalVariable localVariable = new LocalVariable();
//        Log.info("LocalVariable: ", localVariable);
//
//        Log.info("-----Method-----");
//        Method method = new Method();
//        method.someMethod("someMethod");


    }
}
