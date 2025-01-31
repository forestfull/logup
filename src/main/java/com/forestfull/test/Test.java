package com.forestfull.test;

import com.forestfull.log.logger.util.FileRecorder;
import com.forestfull.log.logger.Level;
import com.forestfull.log.logger.util.LogFormatter;
import com.forestfull.log.logger.annotation.ObservableArguments;
import com.forestfull.log.logger.annotation.ObservableReturnValue;
import com.forestfull.log.logger.util.KoLoggerFactoryBean;
import com.forestfull.log.logger.util.Log;

public class Test {

	public static Test getInstance() {
		return new Test();
	}


	@ObservableArguments(level = Level.WARN)
	@ObservableReturnValue(level = Level.ALL)
	public String test(Object s, Object s3, Object s22) {
		return "testShit";
	}

	@ObservableArguments(level = Level.ERROR)
	@ObservableReturnValue(level = Level.OFF)
	public void test1(Object s) {

	}

	public static void main(String[] args) {
		Log.getInstance(KoLoggerFactoryBean.builder()
										   .level(Level.WARN)
										   .logFormatter(LogFormatter.getInstance())
										   .fileRecorder(FileRecorder.getInstance())
										   .build());

//		Log.info("test")
//				.andError("test error")
//				.andWarn("test warn");

		Test test = new Test();

		test.test(1, 23423, 2342); test.test1(1);
	}

}
