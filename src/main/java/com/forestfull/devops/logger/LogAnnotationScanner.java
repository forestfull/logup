package com.forestfull.devops.logger;

import lombok.Builder;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.*;

@Builder
public class LogAnnotationScanner {

    static Class<?> getCallerClass() {
        StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
        try {
            return Class.forName(stackTraceElements[5].getClassName());
        } catch (ClassNotFoundException e) {
            Log.warn(e.getMessage());
            e.printStackTrace(System.out);
            return null;
        }
    }
    public static Set<Class<?>> findClassesInPackage(String packageName) throws ClassNotFoundException, IOException {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        String path = packageName.replace('.', '/');
        Enumeration<URL> resources = classLoader.getResources(path);
        Set<Class<?>> classes = new HashSet<>();

        while (resources.hasMoreElements()) {
            URL resource = resources.nextElement();
            if (resource.getProtocol().equals("file")) {
                // 파일 시스템에서 클래스 파일 찾기
                findClassesInDirectory(new File(resource.getFile()), packageName, classes);
            }
        }

        return classes;
    }

    private static void findClassesInDirectory(File directory, String packageName, Set<Class<?>> classes) throws ClassNotFoundException {
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    findClassesInDirectory(file, packageName + "." + file.getName(), classes);
                } else if (file.getName().endsWith(".class")) {
                    String className = packageName + '.' + file.getName().substring(0, file.getName().length() - 6);
                    classes.add(Class.forName(className));
                }
            }
        }
    }

    Set<Class<?>> getAnnotatedClasses() {
        Class<?> callerClass = getCallerClass();
        Set<Class<?>> allSubclasses = null;
        try {
            allSubclasses = findClassesInPackage("com.forestfull.devops");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        System.out.println(allSubclasses.toString());

        return allSubclasses;
    }
}