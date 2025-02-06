package com.forestfull.logger.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class LogFormatter {

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

	private LogFormatter(String placeholder, SimpleDateFormat dateTimeFormat) {
		LogFormatter.placeholder = placeholder;
		LogFormatter.dateTimeFormat = dateTimeFormat;
	}

	public static LogFormatterBuilder getInstance() {
		return new LogFormatterBuilder();
	}

	public static class LogFormatterBuilder {
		private String placeholder;
		private SimpleDateFormat dateTimeFormat;

		public LogFormatterBuilder placeholder(String placeHolder) {
			this.placeholder = placeHolder; return this;
		}

		public LogFormatterBuilder datetime(String datetime) {
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

		public LogFormatter build() {
			return new LogFormatter(this.placeholder, this.dateTimeFormat);
		}

	}

	protected String getPlaceholder() {
		return placeholder;
	}

	protected SimpleDateFormat getDateTimeFormat() {
		return dateTimeFormat;
	}

	protected void setPlaceholder(String placeholder) {
		LogFormatter.placeholder = placeholder;
	}

	protected void setDateTimeFormat(String dateTimeFormat) {
		LogFormatter.dateTimeFormat = new SimpleDateFormat(dateTimeFormat);
	}
}
