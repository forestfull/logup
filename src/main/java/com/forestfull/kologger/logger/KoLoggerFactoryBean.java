package com.forestfull.kologger.logger;

import lombok.*;

import java.text.SimpleDateFormat;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;

/**
 * KoLooger's general Configuration (KoLooger 전역 환경 설정)
 * <p><b>This class's instance must be initialized only once</b>
 * <br>(이 클래스의 인스턴스는 한 번만 초기화하여야 합니다.)</p>
 *
 * @author Hyeonseok Ko
 * @version JDK 1.6
 * <div>
 * <h3>log file path</h3>
 * <p>logFileDirectory - "classpath:logs/"</p>
 * </div>
 * <hr>
 * <div>
 *      <h3>{@link Formatter}</h3>
 *      <p>placeHolder({@link Log.MessagePattern}) - "{datetime} [{thread}:{level}] - {msg}{new-line}"</p>
 *      <p>datetime - new {@link SimpleDateFormat}("yyyy-MM-dd HH:mm:ss")</p>
 *      <p>{@link Level} - {@link Level}.ALL</p>
 * </div>
 * <hr>
 * <ul>
 * <li><b>OFF</b>
 * <li><s>SEVERE</s> (deprecated)
 * <li><b>WARNING</b> (highest value)
 * <li><b>INFO</b>
 * <li><b>CONFIG</b>
 * <li><b>FINE</b> (lowest value)
 * <li><s>FINER</s> (deprecated)
 * <li><s>FINEST</s> (deprecated)
 * <li><b>ALL</b>
 * </ul>
 */
@Setter
@Getter
@Builder
public class KoLoggerFactoryBean {

    protected final static ExecutorService logConsoleExecutor = Executors.newCachedThreadPool();
    private Formatter formatter;
    private FileRecorder fileRecorder;

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
        private Level level;
    }
}