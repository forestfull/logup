package ddgsds;

import com.forestfull.logger.reflection.Observable;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

public class Test {

    @Observable
    void tesdddt() {

    }

    void tesdddtss() {

    }

    public static void main(String[] args) throws IOException {
        Package mainMethodPackage = getMainMethodPackage();
        List<Class<?>> classesForPackage = getClassesForPackage(mainMethodPackage.getName());
        for (Class<?> aClass : classesForPackage) {
            for (Method method : aClass.getDeclaredMethods()) {
                final Observable declaredAnnotation = method.getDeclaredAnnotation(Observable.class);
                if (declaredAnnotation == null) continue;
                System.out.println(declaredAnnotation);

            }
        }
    }

    private static List<Class<?>> getClassesForPackage(String packageName) throws IOException {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        String path = packageName.replace('.', '/');
        Enumeration<URL> resources = classLoader.getResources(path);
        List<File> dirs = new ArrayList<>();
        while (resources.hasMoreElements()) {
            URL resource = resources.nextElement();
            dirs.add(new File(resource.getFile()));
        }
        List<Class<?>> classes = new ArrayList<>();
        for (File directory : dirs) {
            List<Class<?>> classList = findClasses(directory, packageName);
            classes.addAll(classList);
        }
        return classes;
    }

    private static List<Class<?>> findClasses(File directory, String packageName) {
        List<Class<?>> classes = new ArrayList<>();
        if (!directory.exists()) {
            return classes;
        }
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    classes.addAll(findClasses(file, packageName + "." + file.getName()));
                } else if (file.getName().endsWith(".class")) {
                    try {
                        classes.add(Class.forName(packageName + '.' + file.getName().substring(0, file.getName().length() - 6)));
                    } catch (ClassNotFoundException ignored) {

                    }
                }
            }
        }
        return classes;
    }

    private static Package getMainMethodPackage() {
        final String MAIN_METHOD_NAME = "main";
        for (StackTraceElement stackTraceElement : Thread.currentThread().getStackTrace()) {
            if (!MAIN_METHOD_NAME.equals(stackTraceElement.getMethodName())) continue;

            try {
                Class<?> clazz = Class.forName(stackTraceElement.getClassName());
                clazz.getMethod(MAIN_METHOD_NAME, String[].class);
                return clazz.getPackage();
            } catch (NoSuchMethodException | ClassNotFoundException ignored) {
            }
        }
        return null;
    }
}
