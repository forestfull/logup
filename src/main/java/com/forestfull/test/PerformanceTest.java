package com.forestfull.test;

import com.forestfull.logger.util.KoLoggerFactoryBean;
import com.forestfull.logger.util.Log;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PerformanceTest {

    public static void main(String[] args) {
        Log.getInstance();
        // 싱글 스레드 로그 성능 테스트
        long singleThreadStartWithKoLogger = System.currentTimeMillis();
        for (int i = 0; i < 1000; i++) {
            Log.info("KoLogging in single thread: ", i);
        }
        long singleThreadEndWithKoLogger = System.currentTimeMillis();
        System.out.println("Single thread Kologging time: " + (singleThreadEndWithKoLogger - singleThreadStartWithKoLogger) + " ms");

        // 멀티 스레드 로그 성능 테스트
        long multiThreadStartWithKoLogger = System.currentTimeMillis();
        Thread[] threadsWithKoLogger = new Thread[4];
        for (int t = 0; t < 4; t++) {
            threadsWithKoLogger[t] = new Thread(new Runnable() {
                @Override
                public void run() {
                    for (int i = 0; i < 1000; i++) {
                        Log.info("KoLogging in multi thread: ", i);
                    }
                }
            });
            threadsWithKoLogger[t].start();
        }
        for (int t = 0; t < 4; t++) {
            try {
                threadsWithKoLogger[t].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        long multiThreadEndWithKoLogger = System.currentTimeMillis();
        System.out.println("Multi thread Kologging time: " + (multiThreadEndWithKoLogger - multiThreadStartWithKoLogger) + " ms");
    }
}
