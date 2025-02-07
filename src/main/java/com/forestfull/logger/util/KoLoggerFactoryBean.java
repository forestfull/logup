package com.forestfull.logger.util;

import com.forestfull.logger.Level;
import com.forestfull.logger.config.ConfigLoader;

import java.util.Properties;

/**
 * KoLooger's general Configuration (KoLooger 전역 환경 설정)
 * <p><b>This class's instance must be initialized only once</b>
 * <br>(이 클래스의 인스턴스는 한 번만 초기화하여야 합니다.)</p>
 *
 * <ul>
 * <li><b>OFF</b>
 * <li><b>ERROR</b>
 * <li><b>WARN</b>
 * <li><b>INFO</b>
 * <li><b>ALL</b>
 * </ul>
 *
 * @author Vigfoot
 * @version 1.0
 * @since JDK 1.6
 */

public class KoLoggerFactoryBean {
	protected static LogFormatter logFormatter;
	protected static FileRecorder fileRecorder;
	protected static Level level;

	static {
		configureProperties();
		if (KoLoggerFactoryBean.logFormatter == null)
			KoLoggerFactoryBean.builder().level(Level.ALL).start();
		if (KoLoggerFactoryBean.level == Level.ALL)
			loggingInitializeManual();
	}

	private static void loggingInitializeManual() {
		Log.LogFactory.console(Log.newLine + "=================================================================================================================================================================" + Log.newLine);
		Log.LogFactory.console("KoLogger Setting Example");
		Log.LogFactory.console(Log.newLine + "=================================================================================================================================================================" + Log.newLine);
		Log.LogFactory.console(Log.newLine + " # Priority.1 - application.properties" + Log.newLine);
		Log.LogFactory.console("kologger.level=INFO" + Log.newLine);
		Log.LogFactory.console("kologger.jdbc=true" + Log.newLine);
		Log.LogFactory.console("kologger.log-format.placeholder={datetime} [{thread}:{level}] - {msg}{new-line}" + Log.newLine);
		Log.LogFactory.console("kologger.log-format.date-time-format=yyyy-MM-dd HH:mm:ss" + Log.newLine);
		Log.LogFactory.console("kologger.file-recode.directory=log/" + Log.newLine);
		Log.LogFactory.console("kologger.file-recode.placeholder=YOUR_PROJECT_NAME{date}.log" + Log.newLine);
		Log.LogFactory.console("kologger.file-recode.date-format=yyyy-MM-dd" + Log.newLine + Log.newLine);
		Log.LogFactory.console("-----------------------------------------------------------------------------------------------------------------------------------------------------------------" + Log.newLine);
		Log.LogFactory.console(Log.newLine + " # Priority.2 - application.yml" + Log.newLine);
		Log.LogFactory.console("kologger:" + Log.newLine);
		Log.LogFactory.console("  level: INFO # ALL, INFO, WARN, ERROR, OFF" + Log.newLine);
		Log.LogFactory.console("  jdbc: true # true, false" + Log.newLine);
		Log.LogFactory.console("  log-format:" + Log.newLine);
		Log.LogFactory.console("    placeholder: \"{datetime} [{thread}:{level}] - {msg}{new-line}\"" + Log.newLine);
		Log.LogFactory.console("    date-time-format: yyyy-MM-dd HH:mm:ss" + Log.newLine);
		Log.LogFactory.console("  file-recode:" + Log.newLine);
		Log.LogFactory.console("    directory: log/" + Log.newLine);
		Log.LogFactory.console("    placeholder: YOUR_PROJECT_NAME{date}.log" + Log.newLine);
		Log.LogFactory.console("    date-format: yyyy-MM-dd" + Log.newLine + Log.newLine);
		Log.LogFactory.console("-----------------------------------------------------------------------------------------------------------------------------------------------------------------" + Log.newLine);
		Log.LogFactory.console(Log.newLine + " # Priority.3 - source code" + Log.newLine);
		Log.LogFactory.console("KoLoggerFactoryBean.builder()" + Log.newLine);
		Log.LogFactory.console("                    .level(Level.INFO)" + Log.newLine);
		Log.LogFactory.console("                    .jdbc(true)" + Log.newLine);
		Log.LogFactory.console("                    .logFormatter(LogFormatter.getInstance().placeholder(\"{datetime} [{thread}:{level}] - {msg}{new-line}\").datetime(\"yyyy-MM-dd HH:mm:ss\").build())" + Log.newLine);
		Log.LogFactory.console("                    .fileRecorder(FileRecorder.getInstance().placeholder(\"YOUR_PROJECT_NAME{date}.log\").logFileDirectory(\"logs/\").dateFormat(\"yyyy-MM-dd\"))" + Log.newLine);
		Log.LogFactory.console("                    .build();" + Log.newLine + Log.newLine);
		Log.LogFactory.console("=================================================================================================================================================================" + Log.newLine + Log.newLine);
	}

	private static void configureProperties() {
		final Properties properties = ConfigLoader.loadConfig();
		final String level = properties.getProperty("kologger.level");
		final String logFormatPlaceholder = properties.getProperty("kologger.log-format.placeholder");
		final String logFormatDateTimeFormat = properties.getProperty("kologger.log-format.date-time-format");
		final String fileDirectory = properties.getProperty("kologger.file-recode.directory");
		final String filePlaceholder = properties.getProperty("kologger.file-recode.placeholder");
		final String fileDateFormat = properties.getProperty("kologger.file-recode.date-format");

		final FileRecorder fileRecord = fileDirectory != null ? FileRecorder
				.getInstance()
				.logFileDirectory(fileDirectory)
				.placeholder(filePlaceholder)
				.dateFormat(fileDateFormat).build() : null;

		KoLoggerFactoryBean.builder()
						   .level(level == null ? Level.ALL :Level.valueOf(level.toUpperCase()))
						   .logFormatter(LogFormatter.getInstance()
													 .placeholder(logFormatPlaceholder)
													 .datetime(logFormatDateTimeFormat)
													 .build())
						   .fileRecorder(fileRecord)
						   .start();
	}

	KoLoggerFactoryBean(final LogFormatter logFormatter, final FileRecorder fileRecorder, final Level level, final Boolean jdbc) {
		KoLoggerFactoryBean.logFormatter = logFormatter;
		KoLoggerFactoryBean.fileRecorder = fileRecorder;
		KoLoggerFactoryBean.level = level;
	}

	public static KoLoggerFactoryBeanBuilder builder() {
		return new KoLoggerFactoryBeanBuilder();
	}

	public static class KoLoggerFactoryBeanBuilder {
		private LogFormatter logFormatter;
		private FileRecorder fileRecorder;
		private Level level;

		KoLoggerFactoryBeanBuilder() {
		}

		/**
		 * <p> ex) LogFormatter.getInstance().datetime(string of {@link java.text.SimpleDateFormat}).placeholder({@link LogFormatter.MessagePattern}).build()
		 * <p>
		 * @param logFormatter {@link LogFormatter}
		 */
		public KoLoggerFactoryBeanBuilder logFormatter(final LogFormatter logFormatter) {
			this.logFormatter = logFormatter; return this;
		}

		/**
		 * <p> ex) FileRecorder.getInstance().logFileDirectory("logs/").dateFormat(string of {@link java.text.SimpleDateFormat}).placeholder({@link FileRecorder.FilePattern}).build();
		 * <p>
		 * @param fileRecorder {@link FileRecorder}
		 */
		public KoLoggerFactoryBeanBuilder fileRecorder(final FileRecorder fileRecorder) {

			this.fileRecorder = fileRecorder; return this;
		}

		/**
		 *
		 * <ul>
		 * <li><b>OFF</b>
		 * <li><b>ERROR</b> (high)
		 * <li><b>WARN</b>
		 * <li><b>INFO</b> (row)
		 * <li><b>ALL</b>
		 * </ul>
		 * @param level {@link Level}
		 */
		public KoLoggerFactoryBeanBuilder level(final Level level) {
			this.level = level; return this;
		}

		public void start() {
			if (logFormatter == null)
				this.logFormatter = LogFormatter.getInstance().build();

			if (this.logFormatter.getPlaceholder() == null)
				this.logFormatter.setPlaceholder(LogFormatter.MessagePattern.DEFAULT);

			if (this.logFormatter.getDateTimeFormat() == null)
				this.logFormatter.setDateTimeFormat("yyyy-MM-dd HH:mm:ss");

			if (fileRecorder != null) {
				if (fileRecorder.getPlaceholder() == null)
					fileRecorder.setPlaceholder(FileRecorder.FilePattern.DEFAULT);

				if (fileRecorder.getLogFileDirectory() == null)
					fileRecorder.setLogFileDirectory("logs/");

				if (fileRecorder.getDateFormat() == null)
					fileRecorder.setDateFormat("yyyy-MM-dd");
			}

			KoLoggerFactoryBean.logFormatter = logFormatter;
			KoLoggerFactoryBean.fileRecorder = fileRecorder;
			KoLoggerFactoryBean.level = level;
		}
	}
}
