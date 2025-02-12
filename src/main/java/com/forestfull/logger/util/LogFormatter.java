//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.forestfull.logger.util;

import java.text.SimpleDateFormat;

public class LogFormatter {
	private String placeholder;
	private SimpleDateFormat dateTimeFormat;

	public String toString() {
		return "LogFormatter{placeholder='" + this.placeholder + '\'' + ", dateTimeFormat=" + this.dateTimeFormat.toPattern() + '}';
	}

	protected void setDateTimeFormat(SimpleDateFormat dateTimeFormat) {
		this.dateTimeFormat = dateTimeFormat;
	}

	protected void setPlaceholder(String placeholder) {
		this.placeholder = placeholder;
	}

	protected static String getDefaultPlaceHolder() {
		return "{datetime} [{thread}:{level}] - {msg}{new-line}";
	}

	protected static SimpleDateFormat getDefaultDateTimeFormat() {
		return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	}

	private static String $default$placeholder() {
		return getDefaultPlaceHolder();
	}

	private static SimpleDateFormat $default$dateTimeFormat() {
		return getDefaultDateTimeFormat();
	}

	LogFormatter(String placeholder, SimpleDateFormat dateTimeFormat) {
		this.placeholder = placeholder;
		this.dateTimeFormat = dateTimeFormat;
	}

	public static LogFormatterBuilder builder() {
		return new LogFormatterBuilder();
	}

	public String getPlaceholder() {
		return this.placeholder;
	}

	public SimpleDateFormat getDateTimeFormat() {
		return this.dateTimeFormat;
	}

	public static class LogFormatterBuilder {
		private boolean placeholder$set;
		private String placeholder;
		private boolean dateTimeFormat$set;
		private SimpleDateFormat dateTimeFormat;

		LogFormatterBuilder() {
		}

		public LogFormatterBuilder placeholder(String placeholder) {
			this.placeholder = placeholder;
			this.placeholder$set = true;
			return this;
		}

		public LogFormatterBuilder dateTimeFormat(SimpleDateFormat dateTimeFormat) {
			this.dateTimeFormat = dateTimeFormat;
			this.dateTimeFormat$set = true;
			return this;
		}

		public LogFormatter build() {
			String placeholder = this.placeholder;
			if (!this.placeholder$set) {
				placeholder = LogFormatter.$default$placeholder();
			}

			SimpleDateFormat dateTimeFormat = this.dateTimeFormat;
			if (!this.dateTimeFormat$set) {
				dateTimeFormat = LogFormatter.$default$dateTimeFormat();
			}

			return new LogFormatter(placeholder, dateTimeFormat);
		}

		public String toString() {
			return "LogFormatter.LogFormatterBuilder(placeholder=" + this.placeholder + ", dateTimeFormat=" + this.dateTimeFormat + ")";
		}
	}

	public static class MessagePattern {
		public static final String DEFAULT = "{datetime} [{thread}:{level}] - {msg}{new-line}";
		public static final String DATETIME = "{datetime}";
		public static final String THREAD = "{thread}";
		public static final String LEVEL = "{level}";
		public static final String MESSAGE = "{msg}";
		public static final String NEW_LINE = "{new-line}";

		public MessagePattern() {
		}
	}
}
