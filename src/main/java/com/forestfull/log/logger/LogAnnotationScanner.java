package com.forestfull.log.logger;

import lombok.Builder;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.*;

@Builder
public class LogAnnotationScanner {


    protected Set<Class<?>> getAnnotatedTarget() {


        return null;
    }

    protected Class<?> getCallerClass() {
        StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
        try {
            return Class.forName(stackTraceElements[5].getClassName());
        } catch (ClassNotFoundException e) {
            return null;
        }
    }

    /**
     * 패키지에 해당하는 클래스 셋 반환
     *
     * @param packageName
     * @return
     * @throws IOException
     * @throws ClassNotFoundException
     */
    private Set<Class<?>> getClasses(String packageName) throws IOException, ClassNotFoundException {
        final ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        final String path = packageName.replace('.', '/');
        final Enumeration<URL> resources = classLoader.getResources(path);

        final List<File> dirs = new ArrayList<File>();
        while (resources.hasMoreElements()) {
            URL resource = resources.nextElement();
            dirs.add(new File(resource.getFile()));
        }

        final Set<Class<?>> classes = new HashSet<Class<?>>();
        for (File directory : dirs)
            classes.addAll(findClasses(directory, packageName));

        return classes;
    }

    /**
     * 재귀 호출 구조로 하위 클래스 스캔
     *
     * @param directory
     * @param packageName
     * @return
     * @throws ClassNotFoundException
     */
    private Set<Class<?>> findClasses(File directory, String packageName) throws ClassNotFoundException {
        final Set<Class<?>> classes = new HashSet<Class<?>>();
        if (!directory.exists()) return classes;

        final File[] files = directory.listFiles();
        if (files == null) return classes;

        for (File file : files) {
            if (file.isDirectory()) {
                classes.addAll(findClasses(file, packageName + "." + file.getName()));
            } else if (file.getName().endsWith(".class")) {
                classes.add(Class.forName(packageName + '.' + file.getName().substring(0, file.getName().length() - 6)));
            }
        }
        return classes;
    }
}