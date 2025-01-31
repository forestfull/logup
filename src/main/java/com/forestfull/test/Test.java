package com.forestfull.test;

import com.forestfull.log.logger.annotation.ObservableArguments;
import com.forestfull.log.logger.annotation.ObservableReturnValue;

public class Test {

	public static Test getInstance() {
		return new Test();
	}


	@ObservableArguments
	@ObservableReturnValue
	public String test(Object s, Object s3, Object s22){
		return "testShit";
	}

	@ObservableArguments
	@ObservableReturnValue
	public void test1(Object s){

	}

	public static void main(String[] args) {
		Test test = new Test();

		test.test(1, 23423, 2342);
		test.test1(1);

	}

}
