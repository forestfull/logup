package com.forestfull.logger.util;

import lombok.Builder;
import lombok.Getter;

import java.text.SimpleDateFormat;

@Getter
@Builder
public class LogFormatter {

	@Builder.Default
	private String placeholder = getDefaultPlaceHolder();

	@Builder.Default
	private SimpleDateFormat dateTimeFormat = getDefaultDateTimeFormat();

	public static class MessagePattern {
		public static final String DEFAULT = MessagePattern.DATETIME + " [" + MessagePattern.THREAD + ":" + MessagePattern.LEVEL + "] - " + MessagePattern.MESSAGE + MessagePattern.NEW_LINE;
		public static final String DATETIME = "{datetime}";
		public static final String THREAD = "{thread}";
		public static final String LEVEL = "{level}";
		public static final String MESSAGE = "{msg}";
		public static final String NEW_LINE = "{new-line}";
	}

	@Override
	public String toString() {
		return "LogFormatter{" +
				"placeholder='" + placeholder + '\'' +
				", dateTimeFormat=" + dateTimeFormat.toPattern() +
				'}';
	}

	protected void setDateTimeFormat(SimpleDateFormat dateTimeFormat) {
		this.dateTimeFormat = dateTimeFormat;
	}

	protected void setPlaceholder(String placeholder) {
		this.placeholder = placeholder;
	}

	protected static String getDefaultPlaceHolder() {
		return MessagePattern.DEFAULT;
	}

	protected static SimpleDateFormat getDefaultDateTimeFormat() {
		return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	}
}