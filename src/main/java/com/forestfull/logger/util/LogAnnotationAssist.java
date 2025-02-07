package com.forestfull.logger.util;

import com.forestfull.logger.annotation.ObservableArguments;
import javassist.*;

import java.lang.reflect.Method;

public class LogAnnotationAssist {
	public static void modifyMethods(Class<?> targetClass) {
		try {
			ClassPool pool = new ClassPool(true);
			pool.insertClassPath(new LoaderClassPath(targetClass.getClassLoader())); // 클래스 로더 추가

			CtClass ctClass = pool.get(targetClass.getName());

			if (ctClass.isFrozen()) {
				System.out.println("[Javassist] Skipping modification: " + targetClass.getName() + " (Already modified)");
				return;
			}

			System.out.println("[Javassist] Attempting to transform class: " + ctClass.getName());
			System.out.println("[Javassist] ClassLoader: " + targetClass.getClassLoader());
			System.out.println("[Javassist] ProtectionDomain: " + targetClass.getProtectionDomain());

			for (CtMethod method : ctClass.getDeclaredMethods()) {
				if (hasObservableArgumentsAnnotation(targetClass, method.getName())) {
					System.out.println("[Javassist] Modifying method: " + method.getName());
					addLoggingToMethod(method, method.getName());
				}
			}

			// 기존 클래스를 제거하고 다시 변환
			ctClass.detach();

			// 바이트코드 생성 후 새로운 클래스로 강제 로드
			byte[] byteCode = ctClass.toBytecode();
			Class<?> modifiedClass = new MyClassLoader(targetClass.getClassLoader()).defineClass(targetClass.getName(), byteCode);
			System.out.println("[Javassist] Successfully modified class: " + modifiedClass.getName());

		} catch (CannotCompileException e) {
			System.err.println("[ERROR] Compilation error in modifyMethods: " + e.getReason());
			e.printStackTrace();
		} catch (Exception e) {
			System.err.println("[ERROR] Unexpected exception in modifyMethods:");
			e.printStackTrace();
		}
	}

	private static boolean hasObservableArgumentsAnnotation(Class<?> clazz, String methodName) {
		try {
			for (Method method : clazz.getDeclaredMethods()) {
				if (method.getName().equals(methodName) &&
						method.isAnnotationPresent(ObservableArguments.class)) {
					return true;
				}
			}
		} catch (Exception e) {
			System.err.println("[ERROR] Exception while checking annotation:");
			e.printStackTrace();
		}
		return false;
	}

	private static void addLoggingToMethod(CtMethod method, String methodName) throws CannotCompileException {
		String loggingCode = ""
				+ "System.out.println(\"[Javassist] Method executed: " + methodName + " instance: \" + System.identityHashCode(this));"
				+ "System.out.print(\"[Javassist] Arguments: \");"
				+ "for (int i = 0; i < $args.length; i++) {"
				+ "    System.out.print($args[i] + \" \");"
				+ "}"
				+ "System.out.println();";

		method.insertBefore("{" + loggingCode + "}");
	}
}

// 커스텀 클래스 로더
class MyClassLoader extends ClassLoader {
	public MyClassLoader(ClassLoader parent) {
		super(parent);
	}

	public Class<?> defineClass(String name, byte[] byteCode) {
		return defineClass(name, byteCode, 0, byteCode.length);
	}
}