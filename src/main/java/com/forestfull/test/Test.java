package com.forestfull.test;

import com.forestfull.log.anotation.Observable;
import com.forestfull.log.logger.util.Log;

public class Test {

	@Observable
	public void test(){
		Log.error("hi");
	}


	public static void main(String[] args) {
		new Test().test();
	}

}
