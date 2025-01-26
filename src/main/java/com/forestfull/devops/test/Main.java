package com.forestfull.devops.test;

import com.forestfull.devops.logger.Log;
import com.forestfull.devops.logger.LogAnnotationScanner;

import java.io.File;
import java.lang.annotation.Annotation;
import java.net.URL;
import java.util.List;

public class Main {


    static List<Object> inFile(File file) {

        return null;
    }


    public static void main(String[] args) throws ClassNotFoundException {

Log.customConfiguration();

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
