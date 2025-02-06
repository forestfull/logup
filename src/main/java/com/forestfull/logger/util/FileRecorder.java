package com.forestfull.logger.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class FileRecorder {
	private static String placeholder;
	private static SimpleDateFormat dateFormat;
	private static String logFileDirectory;

	public static class FilePattern {
		public static final String[] filePath = System.getProperty("user.dir").split("\\\\"); //TODO 윈도우 구분 필요 나주엥
		public static final String PROJECT_NAME = filePath[filePath.length - 1];
		public static final String DATE = "{date}";
		public static final String DEFAULT = PROJECT_NAME + DATE + ".log";
	}

	private FileRecorder(String placeholder, SimpleDateFormat dateFormat, String logFileDirectory) {
		FileRecorder.placeholder = placeholder;
		FileRecorder.dateFormat = dateFormat;
		FileRecorder.logFileDirectory = logFileDirectory;
	}

	public static FileRecorderBuilder getInstance() {
		return new FileRecorderBuilder();
	}

	public static class FileRecorderBuilder {
		private String placeholder;
		private SimpleDateFormat dateFormat;
		private String logFileDirectory;

		public FileRecorderBuilder placeholder(String placeHolder) {
			this.placeholder = placeHolder; return this;
		}

		public FileRecorderBuilder dateFormat(String date) {
			SimpleDateFormat format = new SimpleDateFormat(date);

			try {
				format.format(new Date());
			} catch (NullPointerException e) {
				format = null;
			} catch (IllegalArgumentException e) {
				format = null;
			} finally {
				this.dateFormat = format;
			}

			return this;
		}

		public FileRecorderBuilder logFileDirectory(String logFileDirectory) {
			this.logFileDirectory = logFileDirectory;
			return this;
		}

		public FileRecorder build() {
			return new FileRecorder(placeholder, dateFormat, logFileDirectory);
		}
	}

	protected String getPlaceholder() {
		return placeholder;
	}

	protected SimpleDateFormat getDateFormat() {
		return dateFormat;
	}

	protected String getLogFileDirectory() {
		return logFileDirectory;
	}

	protected void setPlaceholder(String placeholder) {
		FileRecorder.placeholder = placeholder;
	}

	protected void setDateFormat(String dateFormat) {
		FileRecorder.dateFormat = new SimpleDateFormat(dateFormat);
	}

	protected void setLogFileDirectory(String logFileDirectory) {
		FileRecorder.logFileDirectory = logFileDirectory;
	}
}
