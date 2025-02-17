package ddgsds;

import com.forestfull.logger.reflection.Observable;

import java.lang.reflect.Method;

public class Test {

    @Observable
    void tesdddt(){

    }

    public static void main(String[] args) {
        getMainMethodPackage();
    }

    private static Package getMainMethodPackage() {
        final String MAIN_METHOD_NAME = "main";
        for (StackTraceElement stackTraceElement : Thread.currentThread().getStackTrace()) {
            if (!MAIN_METHOD_NAME.equals(stackTraceElement.getMethodName())) continue;

            try {
                Class<?> clazz = Class.forName(stackTraceElement.getClassName());
                Method mainMethod = clazz.getMethod(MAIN_METHOD_NAME, String[].class);
                return clazz.getPackage();
            } catch (NoSuchMethodException | ClassNotFoundException ignored) {
            }
        }
        return null;
    }
}
