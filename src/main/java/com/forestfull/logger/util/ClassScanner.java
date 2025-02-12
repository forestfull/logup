package com.forestfull.logger.util;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

public class ClassScanner {

    /**
     * 클래스패스 내 모든 클래스를 자동으로 검색하여 반환
     * @return 찾은 클래스 목록
     */
    public static List<Class<?>> findAllClasses() {
        List<Class<?>> classList = new ArrayList<Class<?>>();

        try {
            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            Enumeration<URL> urls = classLoader.getResources("");

            while (urls.hasMoreElements()) {
                URL url = urls.nextElement();
                File rootDir = new File(url.getPath());

                if (rootDir.exists() && rootDir.isDirectory()) {
                    scanDirectory(rootDir, "", classList);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Error scanning classes", e);
        }

        return classList;
    }

    /**
     * 디렉토리 내 모든 클래스를 재귀적으로 검색
     */
    private static void scanDirectory(File directory, String packageName, List<Class<?>> classList) {
        File[] files = directory.listFiles();

        if (files == null) return;

        for (File file : files) {
            if (file.isDirectory()) {
                // 하위 디렉토리 탐색 (패키지 구조 유지)
                scanDirectory(file, packageName + file.getName() + ".", classList);
            } else if (file.getName().endsWith(".class")) {
                try {
                    String className = packageName + file.getName().replace(".class", "");
                    Class<?> clazz = Class.forName(className);
                    classList.add(clazz);
                } catch (ClassNotFoundException ignored) {
                    // 찾을 수 없는 클래스는 무시
                }
            }
        }
    }
}
