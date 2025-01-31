package com.forestfull.log.logger.util;


import com.forestfull.log.logger.annotation.Observable;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;

import java.lang.annotation.Annotation;

@Aspect
public class ObservableLoggingAspect {

	@Before("@annotation(com.forestfull.log.logger.annotation.Observable) && execution(* *(..)) && !within(com.forestfull.log.logger.util.ObservableLoggingAspect)")
	public void methodArgumentsLogging(JoinPoint joinPoint) {
		Log log = Log.getInstance();

		final Object[] args = joinPoint.getArgs();
		final StringBuilder argumentString = new StringBuilder();
		final String className = joinPoint.getSignature().getDeclaringType().getName();
		final String methodName = joinPoint.getSignature().getName();

		argumentString.append(className).append(".").append(methodName).append("(");

		if (args.length > 0) {
			for (int i = 0; i < args.length; i++) {
				argumentString.append(args[i]);
				if (i != args.length - 1)
					argumentString.append(", ");
			}

		} else {
			argumentString.append("parameter is undefined");

		}

		argumentString.append(")");

		switch (Log.factoryBean.getLevel()) {
			case ALL:
			case INFO:
				log.andInfo(argumentString.toString()); break;
			case WARN:
				log.andWarn(argumentString.toString()); break;
			case ERROR:
				log.andError(argumentString.toString()); break;
		}
	}
}
