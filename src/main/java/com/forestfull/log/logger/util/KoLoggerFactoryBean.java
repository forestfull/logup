package com.forestfull.log.logger.util;

import com.forestfull.log.logger.FileRecorder;
import com.forestfull.log.logger.Level;
import com.forestfull.log.logger.LogFormatter;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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

	protected static final ExecutorService logConsoleExecutor = Executors.newSingleThreadExecutor();
	private final LogFormatter logFormatter;
	private final FileRecorder fileRecorder;
	private final Level level;

	KoLoggerFactoryBean(final LogFormatter logFormatter, final FileRecorder fileRecorder, final Level level) {
		this.logFormatter = logFormatter;
		this.fileRecorder = fileRecorder;
		this.level = level;
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

		public KoLoggerFactoryBeanBuilder logFormatter(final LogFormatter logFormatter) {
			this.logFormatter = logFormatter;
			return this;
		}

		public KoLoggerFactoryBeanBuilder fileRecorder(final FileRecorder fileRecorder) {
			this.fileRecorder = fileRecorder;
			return this;
		}

		public KoLoggerFactoryBeanBuilder level(final Level level) {
			this.level = level;
			return this;
		}

		public KoLoggerFactoryBean build() {
			this.level = level == null ? Level.ALL : level;

			if (logFormatter != null) {
				if (logFormatter.getPlaceholder() == null)
					logFormatter.placeholder(LogFormatter.MessagePattern.DEFAULT);

				if (logFormatter.getDateTimeFormat() == null)
					logFormatter.datetime("yyyy-MM-dd HH:mm:ss");
			}

			if (fileRecorder != null) {
				if (fileRecorder.getPlaceholder() == null)
					fileRecorder.placeholder(FileRecorder.FilePattern.DEFAULT);

				if (fileRecorder.getLogFileDirectory() == null)
					fileRecorder.logFileDirectory("logs/");

				if (fileRecorder.getDateFormat() == null)
					fileRecorder.dateFormat("yyyy-MM-dd");
			}

 			return new KoLoggerFactoryBean(this.logFormatter, this.fileRecorder, this.level);
		}

		public String toString() {
			String var10000 = String.valueOf(this.logFormatter);
			return "KoLoggerFactoryBean.KoLoggerFactoryBeanBuilder(logFormatter=" + var10000 + ", fileRecorder=" + String.valueOf(this.fileRecorder) + ", level=" + String.valueOf(this.level) + ")";
		}
	}

	public LogFormatter getLogFormatter() {
		return logFormatter;
	}

	public FileRecorder getFileRecorder() {
		return fileRecorder;
	}

	public Level getLevel() {
		return level;
	}
}
