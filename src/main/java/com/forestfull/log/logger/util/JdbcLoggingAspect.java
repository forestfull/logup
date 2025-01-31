package com.forestfull.log.logger.util;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;

@Aspect
public class JdbcLoggingAspect {

	@Before("call(* java.sql.Statement.execute*(String)) && args(sql)")
	public void logStatementExecution(JoinPoint joinPoint, String sql) {
		sqlQueryLogging(joinPoint, sql);
	}

	@Before("call(java.sql.PreparedStatement java.sql.Connection.prepareStatement(String)) && args(sql)")
	public void logPreparedStatement(JoinPoint joinPoint, String sql) {
		sqlQueryLogging(joinPoint, sql);
	}

	private void sqlQueryLogging(JoinPoint joinPoint, String sql) {
		Log log = Log.getInstance();

		final StringBuilder argumentString = new StringBuilder();
		final String className = joinPoint.getSignature().getDeclaringType().getName();
		final String methodName = joinPoint.getSignature().getName();
		argumentString.append(className).append(".").append(methodName)
					  .append(Log.newLine)
					  .append(sql);

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
