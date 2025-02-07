package com.forestfull.logger.util;

import com.forestfull.logger.annotation.ObservableArguments;
import javassist.*;
import org.reflections.Reflections;
import org.reflections.scanners.MethodAnnotationsScanner;

import java.lang.reflect.Method;
import java.util.Set;

public class LogAnnotationAssist {

	public static void init() throws Exception {
		final ClassPool pool = ClassPool.getDefault();
		Reflections reflections = new Reflections("com.forestfull", new MethodAnnotationsScanner());

		Set<Method> methodsAnnotatedWith = reflections.getMethodsAnnotatedWith(ObservableArguments.class);

		for (Method method : methodsAnnotatedWith) {
			String className = method.getDeclaringClass().getName();
			String methodName = method.getName();

			System.out.println("메서드 변환 시도: " + className + "." + methodName);

			// 1. 클래스 로드
			CtClass ctClass = pool.get(className);

			// 2. 이미 변환된 클래스인지 확인
			if (ctClass.isFrozen()) {
				ctClass.defrost();
			}

			CtMethod ctMethod = ctClass.getDeclaredMethod(methodName);

			// 3. 메서드 앞에 로그 추가
			StringBuilder logCode = new StringBuilder();
			logCode.append("{ System.out.println(\"[LOG] 호출된 메서드: " + methodName + "\");");

			CtClass[] paramTypes = ctMethod.getParameterTypes();
			for (int i = 0; i < paramTypes.length; i++) {
				logCode.append("System.out.println(\"[LOG] 파라미터 " + i + ": \" + $" + (i + 1) + ");");
			}
			logCode.append(" }");

			// 4. 기존 메서드 앞에 코드 삽입
			ctMethod.insertBefore(logCode.toString());

			// 🔥 `toClass()` 호출 없음 -> 변환된 코드가 런타임에 반영되지만 기존 JVM 클래스를 변경하지 않음!
			System.out.println("✅ 코드 삽입 완료: " + className + "." + methodName);
		}

		Log.LogFactory.console("@ObservableArguments 적용 완료");
	}

}
