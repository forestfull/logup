package com.forestfull.logger.util;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.text.SimpleDateFormat;
import java.util.Date;

@Getter
@Builder
public class FileRecorder {
	@Builder.Default
	private String placeholder = FilePattern.PROJECT_NAME + "{date}.log";
	@Builder.Default
	private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	@Builder.Default
	private String logFileDirectory = "logs/";

	public static class FilePattern {
		public static final String[] filePath = System.getProperty("user.dir").split("[/|\\\\]");
		public static final String PROJECT_NAME = filePath[filePath.length - 1];
		public static final String DATE = "{date}";
		public static final String DEFAULT = PROJECT_NAME + DATE + ".log";
	}

	protected void setPlaceholder(String placeholder) {
		this.placeholder = placeholder;
	}

	protected void setDateFormat(SimpleDateFormat dateFormat) {
		this.dateFormat = dateFormat;
	}

	protected void setLogFileDirectory(String logFileDirectory) {
		this.logFileDirectory = logFileDirectory;
	}
}