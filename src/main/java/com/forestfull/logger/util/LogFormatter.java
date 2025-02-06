package com.forestfull.logger.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class LogFormatter {

	private static LogFormatter logFormatter;
	private static String placeholder;
	private static SimpleDateFormat dateTimeFormat;

	public static class MessagePattern {
		public static final String DEFAULT = MessagePattern.DATETIME + " [" + MessagePattern.THREAD + ":" + MessagePattern.LEVEL + "] - " + MessagePattern.MESSAGE + MessagePattern.NEW_LINE;
		public static final String DATETIME = "{datetime}";
		public static final String THREAD = "{thread}";
		public static final String LEVEL = "{level}";
		public static final String MESSAGE = "{msg}";
		public static final String NEW_LINE = "{new-line}";
	}

	private LogFormatter() {}

	public static LogFormatter getInstance() {
		if (LogFormatter.logFormatter == null)
			LogFormatter.logFormatter =	new LogFormatter();

		return LogFormatter.logFormatter;
	}

	public static LogFormatter placeholder(String placeHolder) {
		LogFormatter.placeholder = placeHolder; return LogFormatter.logFormatter;
	}

	public static LogFormatter datetime(String datetime) {
		SimpleDateFormat format = new SimpleDateFormat(datetime);

		try {
			format.format(new Date());
		} catch (NullPointerException e) {
			format = null;
		} catch (IllegalArgumentException e) {
			format = null;
		} finally {
			LogFormatter.dateTimeFormat = format;
		}

		return LogFormatter.logFormatter;
	}

	protected static String getPlaceholder() {
		return placeholder;
	}

	protected static SimpleDateFormat getDateTimeFormat() {
		return dateTimeFormat;
	}
}
