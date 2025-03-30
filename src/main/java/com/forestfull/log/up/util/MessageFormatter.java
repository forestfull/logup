package com.forestfull.log.up.util;

import com.forestfull.log.up.formatter.LogFormatter;
import oshi.SystemInfo;
import oshi.hardware.CentralProcessor;

import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class MessageFormatter {

    abstract String call(final com.forestfull.log.up.Level level, final Object... args);

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

    static MessageFormatter[] replaceMatchPlaceholder(String[] placeholder) {
        MessageFormatter[] messageFormatters = new MessageFormatter[placeholder.length];

        for (int i = 0; i < placeholder.length; i++) {
            switch (placeholder[i]) {
                case LogFormatter.MessagePattern.DATETIME:
                    messageFormatters[i] = new DateMessageFormatter();
                    break;

                case LogFormatter.MessagePattern.CPU_TICK:
                    messageFormatters[i] = new CPUTick();
                    break;

                case LogFormatter.MessagePattern.LEVEL:
                    messageFormatters[i] = new Level();
                    break;

                case LogFormatter.MessagePattern.THREAD:
                    messageFormatters[i] = new Thread();
                    break;

                case LogFormatter.MessagePattern.MESSAGE:
                    messageFormatters[i] = new Message();
                    break;

                default:
                    messageFormatters[i] = new Mime(placeholder[i]);
                    break;
            }
        }

        return messageFormatters;
    }

    static class DateMessageFormatter extends MessageFormatter {

        @Override
        public String call(final com.forestfull.log.up.Level level, final Object... args) {
            return LogUpFactoryBean.logUpProperties.getLogFormat().getDateTimeFormat().format(System.currentTimeMillis());
        }
    }

    static class CPUTick extends MessageFormatter {
        private static final CentralProcessor processor = new SystemInfo().getHardware().getProcessor();
        private static final long MINIMUM_MILLISECONDS = 1000L;

        private static long prevTimeMilliSeconds = System.currentTimeMillis();
        private static long[] prevTicks = processor.getSystemCpuLoadTicks();
        private static String cpu_usage = "/----------/";

        @Override
        synchronized String call(com.forestfull.log.up.Level level, Object... args) {
            if (System.currentTimeMillis() - prevTimeMilliSeconds < MINIMUM_MILLISECONDS) return cpu_usage;

            long totalCpu = 0;
            long[] ticks = processor.getSystemCpuLoadTicks();
            prevTimeMilliSeconds = System.currentTimeMillis();

            for (int i = 0; i < ticks.length; i++) {
                totalCpu += ticks[i] - prevTicks[i];
            }

            int idleIndex = CentralProcessor.TickType.IDLE.getIndex();
            int cpuUsage = (int) Math.ceil((totalCpu - (ticks[idleIndex] - prevTicks[idleIndex])) / (double) totalCpu * 10);
            cpu_usage = "/";

            for (int i = 0; i < 10; i++)
                cpu_usage += cpuUsage > i ? '*' : '-';

            cpu_usage += '/';
            prevTicks = ticks;

            return cpu_usage;
        }
    }

    static class Level extends MessageFormatter {

        @Override
        public String call(final com.forestfull.log.up.Level level, final Object... args) {
            if (level == com.forestfull.log.up.Level.TEST)
                return level.name();

            return level.getColor() + level.name() + com.forestfull.log.up.Level.COLOR.RESET;
        }
    }

    static class Thread extends MessageFormatter {
        private static final String PID = ManagementFactory.getRuntimeMXBean().getName().split("@")[0];

        @Override
        public synchronized String call(final com.forestfull.log.up.Level level, final Object... args) {
            if (level == com.forestfull.log.up.Level.TEST)
                return "[PID:" + PID + "] " + java.lang.Thread.currentThread().getName();

            return com.forestfull.log.up.Level.COLOR.PURPLE +
                    "[PID:" + PID + "] " +
                    com.forestfull.log.up.Level.COLOR.RESET +
                    com.forestfull.log.up.Level.COLOR.CYAN +
                    java.lang.Thread.currentThread().getName() +
                    com.forestfull.log.up.Level.COLOR.RESET;
        }
    }

    static class Message extends MessageFormatter {

        @Override
        String call(final com.forestfull.log.up.Level level, final Object... args) {
            StringBuilder message = new StringBuilder();
            for (Object arg : args) message.append(arg);

            if (level == com.forestfull.log.up.Level.TEST)
                return message.toString();

            return level.getColor() + message + com.forestfull.log.up.Level.OFF.getColor();
        }
    }

    static class Mime extends MessageFormatter {

        private final String mime;

        public Mime(String mime) {
            this.mime = mime;
        }

        @Override
        synchronized String call(final com.forestfull.log.up.Level level, final Object... args) {
            return this.mime;
        }
    }
}