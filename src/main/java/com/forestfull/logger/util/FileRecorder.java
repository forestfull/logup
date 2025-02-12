//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.forestfull.logger.util;

import java.text.SimpleDateFormat;

public class FileRecorder {
	private String placeholder;
	private SimpleDateFormat dateFormat;
	private String logFileDirectory;

	protected void setPlaceholder(String placeholder) {
		this.placeholder = placeholder;
	}

	protected void setDateFormat(SimpleDateFormat dateFormat) {
		this.dateFormat = dateFormat;
	}

	protected void setLogFileDirectory(String logFileDirectory) {
		this.logFileDirectory = logFileDirectory;
	}

	protected static String getDefaultPlaceHolder() {
		return FileRecorder.FilePattern.PROJECT_NAME + "{date}.log";
	}

	protected static SimpleDateFormat getDefaultDateFormat() {
		return new SimpleDateFormat("yyyy-MM-dd");
	}

	private static String $default$placeholder() {
		return getDefaultPlaceHolder();
	}

	private static SimpleDateFormat $default$dateFormat() {
		return getDefaultDateFormat();
	}

	private static String $default$logFileDirectory() {
		return "logs/";
	}

	FileRecorder(String placeholder, SimpleDateFormat dateFormat, String logFileDirectory) {
		this.placeholder = placeholder;
		this.dateFormat = dateFormat;
		this.logFileDirectory = logFileDirectory;
	}

	public static FileRecorderBuilder builder() {
		return new FileRecorderBuilder();
	}

	public String getPlaceholder() {
		return this.placeholder;
	}

	public SimpleDateFormat getDateFormat() {
		return this.dateFormat;
	}

	public String getLogFileDirectory() {
		return this.logFileDirectory;
	}

	public static class FileRecorderBuilder {
		private boolean placeholder$set;
		private String placeholder;
		private boolean dateFormat$set;
		private SimpleDateFormat dateFormat;
		private boolean logFileDirectory$set;
		private String logFileDirectory;

		FileRecorderBuilder() {
		}

		public FileRecorderBuilder placeholder(String placeholder) {
			this.placeholder = placeholder;
			this.placeholder$set = true;
			return this;
		}

		public FileRecorderBuilder dateFormat(SimpleDateFormat dateFormat) {
			this.dateFormat = dateFormat;
			this.dateFormat$set = true;
			return this;
		}

		public FileRecorderBuilder logFileDirectory(String logFileDirectory) {
			this.logFileDirectory = logFileDirectory;
			this.logFileDirectory$set = true;
			return this;
		}

		public FileRecorder build() {
			String placeholder = this.placeholder;
			if (!this.placeholder$set) {
				placeholder = FileRecorder.$default$placeholder();
			}

			SimpleDateFormat dateFormat = this.dateFormat;
			if (!this.dateFormat$set) {
				dateFormat = FileRecorder.$default$dateFormat();
			}

			String logFileDirectory = this.logFileDirectory;
			if (!this.logFileDirectory$set) {
				logFileDirectory = FileRecorder.$default$logFileDirectory();
			}

			return new FileRecorder(placeholder, dateFormat, logFileDirectory);
		}

		public String toString() {
			return "FileRecorder.FileRecorderBuilder(placeholder=" + this.placeholder + ", dateFormat=" + this.dateFormat + ", logFileDirectory=" + this.logFileDirectory + ")";
		}
	}

	public static class FilePattern {
		public static final String[] filePath = System.getProperty("user.dir").split("[/|\\\\]");
		public static final String PROJECT_NAME;
		public static final String DATE = "{date}";
		public static final String DEFAULT;

		public FilePattern() {
		}

		static {
			PROJECT_NAME = filePath[filePath.length - 1];
			DEFAULT = PROJECT_NAME + "{date}" + ".log";
		}
	}
}
