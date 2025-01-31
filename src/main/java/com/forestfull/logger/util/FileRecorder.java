package com.forestfull.logger.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class FileRecorder {
	private String placeholder;
	private SimpleDateFormat dateFormat;
	private String logFileDirectory;

	public static class FilePattern {
		public static final String[] filePath = System.getProperty("user.dir").split("\\\\"); //TODO 윈도우 구분 필요 나주엥
		public static final String PROJECT_NAME = filePath[filePath.length - 1];
		public static final String DATE = "{date}";
		public static final String DEFAULT = PROJECT_NAME + DATE + ".log";
	}

	private FileRecorder() {
	}

	public static FileRecorder getInstance() {
		return new FileRecorder();
	}

	public FileRecorder placeholder(String placeHolder) {
		this.placeholder = placeHolder; return this;
	}

	public FileRecorder dateFormat(String date) {
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

	public FileRecorder logFileDirectory(String logFileDirectory) {
		this.logFileDirectory = logFileDirectory;
		return this;
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
}
