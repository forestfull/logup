package com.forestfull.log.logger;

import java.text.SimpleDateFormat;
import java.util.Date;

public class LogFormatter {

	private String placeholder;
	private SimpleDateFormat dateTimeFormat;

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
		return new LogFormatter();
	}

	public LogFormatter placeholder(String placeHolder) {
		this.placeholder = placeHolder; return this;
	}

	public LogFormatter datetime(String datetime) {
		SimpleDateFormat format = new SimpleDateFormat(datetime);

		try {
			format.format(new Date());
		} catch (NullPointerException e) {
			format = null;
		} catch (IllegalArgumentException e) {
			format = null;
		} finally {
			this.dateTimeFormat = format;
		}

		return this;
	}

	public String getPlaceholder() {
		return placeholder;
	}

	public SimpleDateFormat getDateTimeFormat() {
		return dateTimeFormat;
	}
}
