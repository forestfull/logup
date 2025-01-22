package com.forestfull.factory;

import lombok.*;

import java.text.SimpleDateFormat;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * korLogger's general Configuration (korLogger 전역 환경 설정)
 * <p><b>This class's instance must be initialized only once</b>
 * <br>(이 클래스의 인스턴스는 한 번만 초기화하여야 합니다.)</p>
 *
 * @default-value
 * <div>
 *      <h3>log file path</h3>
 *      <p>logFileDirectory - "classpath:logs/"</p>
 * </div>
 * <hr>
 * <div>
 *      <h3>{@link Formatter}</h3>
 *      <p>placeHolder({@link com.forestfull.factory.Log.Pattern}) - "{datetime} [{thread}]:{level} - {msg}{new-line}"</p>
 *      <p>datetime - new {@link SimpleDateFormat}("yyyy-MM-dd HH:mm:ss")</p>
 *      <p>{@link Level} - {@link Level}.TRACE</p>
 * </div>
 * @version JDK 1.6
 * @author Hyeonseok Ko
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class KorLoggerFactoryBean {

    protected final static ExecutorService logConsoleExecutor = Executors.newSingleThreadExecutor();

    private Formatter formatter = Formatter.builder()
            .placeHolder("{datetime} [{thread}]:{level} - {msg}{new-line}")
            .datetime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"))
            .level(Level.TRACE)
            .build();

    private String logFileDirectory = "classpath:logs/";

    @Getter
    @Builder
    public static class Formatter {
        private String placeHolder;
        private SimpleDateFormat datetime;
        private Level level;
    }

    public static KorLoggerFactoryBeanBuilder builder() {
        return new KorLoggerFactoryBeanBuilder();
    }

    public static class KorLoggerFactoryBeanBuilder {
        private Formatter formatter;
        private String logFileDirectory;

        KorLoggerFactoryBeanBuilder() {
        }

        public KorLoggerFactoryBeanBuilder formatter(Formatter formatter) {
            this.formatter = formatter;
            return this;
        }

        public KorLoggerFactoryBeanBuilder logFileDirectory(String logFileDirectory) {
            this.logFileDirectory = logFileDirectory;
            return this;
        }

        public Log build() {
            /*TODO 여기다가 이니셜라이징*/
            Log.factoryBean = new KorLoggerFactoryBean(this.formatter, this.logFileDirectory);
            return Log.getInstance();
        }
    }
}
