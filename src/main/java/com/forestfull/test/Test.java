package com.forestfull.test;

import com.forestfull.log.logger.annotation.Observable;
import com.forestfull.log.logger.util.Log;

@Observable
public class Test {


	public static void main(String[] args) {
		Test test1 = new Test();
		Test test = TestClass.getInstance();

		TestClass testClass = new TestClass();
		testClass.test(1, 23423, 2342);
	}

}
