package com.forestfull.factory;

import lombok.*;

import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;

/**
 * korLogger's general Configuration (korLogger 전역 환경 설정)
 * <p><b>This class's instance must be initialized only once</b>
 * <br>(이 클래스의 인스턴스는 한 번만 초기화하여야 합니다.)</p>
 *
 * @author Hyeonseok Ko
 * @version JDK 1.6
 * @default-value <div>
 * <h3>log file path</h3>
 * <p>logFileDirectory - "classpath:logs/"</p>
 * </div>
 * <hr>
 * <div>
 *      <h3>{@link Formatter}</h3>
 *      <p>placeHolder({@link com.forestfull.factory.Log.Pattern}) - "{datetime} [{thread}:{level}] - {msg}{new-line}"</p>
 *      <p>datetime - new {@link SimpleDateFormat}("yyyy-MM-dd HH:mm:ss")</p>
 *      <p>{@link Level} - {@link Level}.ALL</p>
 * </div>
 * <hr>
 * <ul>
 * <li>OFF
 * <li>SEVERE (highest value)
 * <li>WARNING
 * <li>INFO
 * <li>CONFIG
 * <li>FINE
 * <li>FINER
 * <li>FINEST  (lowest value)
 * <li>ALL
 * </ul>
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class KorLoggerFactoryBean {

    protected final static ExecutorService logConsoleExecutor = Executors.newCachedThreadPool();

    private Formatter formatter = Formatter.builder()
            .placeHolder("{datetime} [{thread}:{level}] - {msg}{new-line}")
            .datetime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"))
            .level(Level.ALL)
            .build();

    private FileRecorder fileRecorder = FileRecorder
            .builder().logFileDirectory("classpath:logs/")
            .nameFormat("")
            .build();

    @Getter
    @Builder
    public static class FileRecorder {
        private String nameFormat;
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
