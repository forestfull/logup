package com.forestfull;

import com.forestfull.logger.util.Log;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Test {

	public static void main(String[] args) {
		ExecutorService executorService = Executors.newFixedThreadPool(10);

		long startTime = System.nanoTime();

		for (int i = 0; i < 10; i++) {  // 10개의 스레드 실행
			executorService.submit(new Runnable() {
				@Override
				public void run() {
					for (int j = 0; j < 100; j++) {  // 스레드당 1000개 로그 기록
						Log.info("Thread logging test - ", Thread.currentThread().getName());
					}
				}
			});
		}

		executorService.shutdown();
		while (!executorService.isTerminated()) {

		}

		long endTime = System.nanoTime();
		System.out.println("멀티스레드 로그 기록 시간: " + (endTime - startTime) / 1000000.0 + " ms");
	}
}
