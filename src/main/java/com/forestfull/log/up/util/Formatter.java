package com.forestfull.log.up.util;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class Formatter {

    abstract String call(com.forestfull.log.up.Level level, Object... arg);

    static String[] splitWithDelimiter(String placeholder) {
        Pattern pattern = Pattern.compile("\\{\\w+}|\\{new-line}");
        Matcher matcher = pattern.matcher(placeholder);
        List<String> result = new ArrayList<>();

        int lastEnd = 0;
        while (matcher.find()) {
            if (lastEnd != matcher.start()) {
                result.add(placeholder.substring(lastEnd, matcher.start()));
            }
            result.add(matcher.group());
            lastEnd = matcher.end();
        }

        if (lastEnd != placeholder.length()) {
            result.add(placeholder.substring(lastEnd));
        }

        return result.toArray(new String[0]);

    }

    static Formatter[] replaceMatchPlaceholder(String[] placeholder) {
        Formatter[] formatters = new Formatter[placeholder.length];

        for (int i = 0; i < placeholder.length; i++) {
            switch (placeholder[i]) {
                case LogFormatter.MessagePattern.DATETIME:
                    formatters[i] = new DateFormatter();
                    break;

                case LogFormatter.MessagePattern.LEVEL:
                    formatters[i] = new Level();
                    break;

                case LogFormatter.MessagePattern.THREAD:
                    formatters[i] = new Thread();
                    break;

                case LogFormatter.MessagePattern.NEW_LINE:
                    formatters[i] = new LineSeparator();
                    break;

                case LogFormatter.MessagePattern.MESSAGE:
                    formatters[i] = new Message();
                    break;

                default:
                    formatters[i] = new Mime(placeholder[i]);
                    break;
            }
        }

        return formatters;
    }

    static class DateFormatter extends Formatter {

        @Override
        public String call(com.forestfull.log.up.Level level, Object... arg) {
            return LogUpFactoryBean.logFormatter.getDateTimeFormat().format(new Date());
        }
    }

    static class Level extends Formatter {

        @Override
        public String call(com.forestfull.log.up.Level level, Object... arg) {
            return level.getColorName();
        }
    }

    static class Thread extends Formatter {

        @Override
        public String call(com.forestfull.log.up.Level level, Object... arg) {
            StackTraceElement stackTrace = java.lang.Thread.currentThread().getStackTrace()[5];

            StringBuilder currentThreadName = new StringBuilder(java.lang.Thread.currentThread().getName()).append(' ');
            final String className = stackTrace.getClassName();
            final String methodName = stackTrace.getMethodName();

            if (className.split("\\.").length > 1) {
                String[] split = className.split("\\.");
                for (int i = 0; i < split.length - 1; i++) {
                    String pack = split[i];
                    currentThreadName.append(pack.charAt(0)).append('.');
                }
                currentThreadName.append(split[split.length - 1]).append('.');
            } else {
                currentThreadName.append(className).append('.');
            }

            currentThreadName.append(methodName)
                    .append('(').append(stackTrace.getFileName()).append(':').append(stackTrace.getLineNumber()).append(')');


            return String.valueOf(currentThreadName);
        }
    }

    static class LineSeparator extends Formatter {

        @Override
        String call(com.forestfull.log.up.Level level, Object... arg) {
            return System.lineSeparator();
        }
    }

    static class Message extends Formatter {

        @Override
        String call(com.forestfull.log.up.Level level, Object... orgs) {
            StringBuilder message = new StringBuilder();
            for (Object org : orgs)
                message.append(org);

            return level.getColor() + message + com.forestfull.log.up.Level.OFF.getColor();
        }
    }

    static class Mime extends Formatter {

        private final String mime;

        public Mime(String mime) {
            this.mime = mime;
        }

        @Override
        String call(com.forestfull.log.up.Level level, Object... arg) {
            return this.mime;
        }
    }
}