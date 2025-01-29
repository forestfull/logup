package com.forestfull.log.logger;

import com.forestfull.log.config.Level;
import lombok.*;

import java.text.SimpleDateFormat;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * KoLooger's general Configuration (KoLooger 전역 환경 설정)
 * <p><b>This class's instance must be initialized only once</b>
 * <br>(이 클래스의 인스턴스는 한 번만 초기화하여야 합니다.)</p>
 *
 * <ul>
 * <li><b>OFF</b>
 * <li><s>ERROR</s>
 * <li><b>WARN</b>
 * <li><b>INFO</b>
 * <li><b>ALL</b>
 * </ul>
 *
 * @author Vigfoot
 * @version 1.0
 * @since JDK 1.6
 */
@Setter
@Getter
@Builder
public class KoLoggerFactoryBean {

    protected final static ExecutorService logConsoleExecutor = Executors.newCachedThreadPool();
    private Formatter formatter;
    private FileRecorder fileRecorder;
    private Level level;

    @Getter
    @Builder
    public static class FileRecorder {
        private String placeHolder;
        private SimpleDateFormat dateFormat;
        private String logFileDirectory;
    }

    @Getter
    @Builder
    public static class Formatter {
        private String placeHolder;
        private SimpleDateFormat datetime;
    }
}