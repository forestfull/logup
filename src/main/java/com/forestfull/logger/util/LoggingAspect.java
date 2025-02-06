package com.forestfull.logger.util;

import com.forestfull.logger.Level;
import com.forestfull.logger.annotation.ObservableArguments;
import com.forestfull.logger.annotation.ObservableReturnValue;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.reflect.Method;

public class LoggingAspect {

	@Aspect
	public static class Observable {
		@Before("@annotation(com.forestfull.logger.annotation.ObservableArguments) && execution(* *(..)) && !within(com.forestfull.logger.util.LoggingAspect$Observable)")
		public void methodArguments(JoinPoint joinPoint) {
			final MethodSignature signature = (MethodSignature) joinPoint.getSignature();
			final Method method = signature.getMethod();
			final Level annotationLevel = method.getAnnotation(ObservableArguments.class).level();

			if (annotationLevel == null) return;
			if (annotationLevel == Level.OFF) return;
			if (annotationLevel != Level.ALL && annotationLevel.compareTo(KoLoggerFactoryBean.level) < 0) return;

			final Object[] args = joinPoint.getArgs();
			final StringBuilder argumentString = new StringBuilder();
			final String className = joinPoint
					.getSignature().getDeclaringType()
					.getName();
			final String methodName = joinPoint
					.getSignature().getName();

			argumentString.append(className).append(".")
						  .append(methodName).append("(");

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

			Log.LogFactory.console(annotationLevel.name() + argumentString);
			Log.LogFactory.file(annotationLevel.name() + argumentString);
		}

		@AfterReturning(value = "@annotation(com.forestfull.logger.annotation.ObservableReturnValue) && execution(* *(..)) && !within(com.forestfull.logger.util.LoggingAspect$Observable)", returning = "returnValue")
		public void methodReturns(JoinPoint joinPoint, Object returnValue) {
			final MethodSignature signature = (MethodSignature) joinPoint.getSignature();
			final Method method = signature.getMethod();
			final Level annotationLevel = method.getAnnotation(ObservableReturnValue.class).level();

			if (annotationLevel == null) return;
			if (annotationLevel == Level.OFF) return;
			if (annotationLevel != Level.ALL && annotationLevel.compareTo(KoLoggerFactoryBean.level) < 0) return;

			final StringBuilder argumentString = new StringBuilder();
			final String className = joinPoint
					.getSignature().getDeclaringType()
					.getName();
			final String methodName = joinPoint
					.getSignature().getName();
			argumentString.append(className).append(".")
						  .append(methodName)
						  .append(" -> {")
						  .append(returnValue).append("}");

			Log.LogFactory.console(annotationLevel.name() + argumentString);
			Log.LogFactory.file(annotationLevel.name() + argumentString);
		}
	}

	@Aspect
	public static class Jdbc {

		@Before("call(* java.sql.Statement.execute*(String)) && args(sql)")
		public void statement(JoinPoint joinPoint, String sql) {
			sqlQueryLogging(joinPoint, sql);
		}

		@Before("call(java.sql.PreparedStatement java.sql.Connection.prepareStatement(String)) && args(sql)")
		public void preparedStatement(JoinPoint joinPoint, String sql) {
			sqlQueryLogging(joinPoint, sql);
		}

		private void sqlQueryLogging(JoinPoint joinPoint, String sql) {
			if (Boolean.FALSE.equals(KoLoggerFactoryBean.jdbc)) return;

			final StringBuilder argumentString = new StringBuilder();
			final String className = joinPoint
					.getSignature().getDeclaringType()
					.getName();
			final String methodName = joinPoint
					.getSignature().getName();
			argumentString.append(className).append(".")
						  .append(methodName)
						  .append(Log.newLine).append(sql);

			Log.LogFactory.console(KoLoggerFactoryBean.level.name() + argumentString);
			Log.LogFactory.file(KoLoggerFactoryBean.level.name() + argumentString);
		}
	}
}
