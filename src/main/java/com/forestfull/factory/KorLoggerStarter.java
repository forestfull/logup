package com.forestfull.factory;

import lombok.*;

import java.text.SimpleDateFormat;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class KorLoggerStarter {

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
            KorLoggerStarter starter = new KorLoggerStarter(this.formatter, this.logFileDirectory);

            return Log.getInstance();
        }
    }
}
