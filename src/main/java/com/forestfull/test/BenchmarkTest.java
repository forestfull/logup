package com.forestfull.test;

import com.forestfull.logger.util.FileRecorder;
import com.forestfull.logger.util.KoLoggerFactoryBean;
import com.forestfull.logger.util.Log;
import org.openjdk.jmh.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

@State(Scope.Benchmark)
public class BenchmarkTest {

    private static final Logger log = LoggerFactory.getLogger(BenchmarkTest.class);

    static {
        Log.getInstance(KoLoggerFactoryBean.builder().fileRecorder(FileRecorder.getInstance()).build());
    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    @Warmup(iterations = 5)
    @Measurement(iterations = 5)
    @Fork(1)
    public void singleThreadLogging() {
        // 싱글스레드 로그 측정
        for (int i = 0; i < 1000; i++) {
            log.info("Logging in single thread: " + i);
        }
    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    @Warmup(iterations = 5)
    @Measurement(iterations = 5)
    @Fork(1)
    @Threads(4) // 멀티스레드 환경 설정
    public void multiThreadLogging() {
        // 멀티스레드 로그 측정
        for (int i = 0; i < 1000; i++) {
            log.info("Logging in multi thread: " + i);
        }
    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    @Warmup(iterations = 5)
    @Measurement(iterations = 5)
    @Fork(1)
    public void singleThreadLoggingWithKoLogger() {
        // 싱글스레드 로그 측정
        for (int i = 0; i < 1000; i++) {
            Log.info("KoLogging in single thread: " + i);
        }
    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    @Warmup(iterations = 5)
    @Measurement(iterations = 5)
    @Fork(1)
    @Threads(4) // 멀티스레드 환경 설정
    public void multiThreadLoggingWithKoLogger() {
        // 멀티스레드 로그 측정
        for (int i = 0; i < 1000; i++) {
            Log.info("KoLogging in multi thread: " + i);
        }
    }
}