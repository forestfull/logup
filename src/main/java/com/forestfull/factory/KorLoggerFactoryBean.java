package com.forestfull.factory;

import lombok.*;

import java.text.SimpleDateFormat;

/**
 * korLogger's Configuration for spring project
 * @default-value
 * <div>
 *      <h3>log file path</h3>
 *      <p>logFileDirectory - "classpath:logs/"</p>
 * </div>
 * <hr>
 * <div>
 *      <h3>{@link Formatter}</h3>
 *      <p>placeHolder - "{datetime}{thread}:{level} - {msg}{new-line}"</p>
 *      <p>datetime - new {@link SimpleDateFormat}("yyyy-MM-dd HH:mm:ss")</p>
 *      <p>{@link Level} - {@link Level}.INFO</p>
 * </div>
 * @version JDK 1.6
 * @author Hyeonseok Ko
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class KorLoggerFactoryBean {

    private Formatter formatter = Formatter.builder()
            .placeHolder("{datetime}{thread}:{level} - {msg}{new-line}")
            .datetime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"))
            .level(Level.INFO)
            .build();

    private String logFileDirectory = "classpath:logs/";

    @Builder
    public static class Formatter {
        private String placeHolder;
        private SimpleDateFormat datetime;
        private Level level;
    }

    public static KorLoggerStarterBuilder builder() {
        return new KorLoggerStarterBuilder();
    }

    public static class KorLoggerStarterBuilder {
        private Formatter formatter;
        private String logFileDirectory;

        KorLoggerStarterBuilder() {
        }

        public KorLoggerStarterBuilder formatter(Formatter formatter) {
            this.formatter = formatter;
            return this;
        }

        public KorLoggerStarterBuilder logFileDirectory(String logFileDirectory) {
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
