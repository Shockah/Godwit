package pl.shockah.godwit;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public final class GodwitLogger {
	@Nonnull
	public final String tag;

	@Nullable
	public SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss.SSS", Locale.US);

	@Nonnull
	public Level level = Level.Info;

	public GodwitLogger(@Nonnull Class<?> clazz) {
		this(clazz.getSimpleName());
	}

	public GodwitLogger(@Nonnull String tag) {
		this.tag = tag;
	}

	@Nonnull
	public GodwitLogger setFormatter(@Nullable SimpleDateFormat formatter) {
		this.formatter = formatter;
		return this;
	}

	public boolean willLog(@Nonnull Level level) {
		return this.level.gdxLevel >= level.gdxLevel;
	}

	public boolean willLogError() {
		return willLog(Level.Error);
	}

	public boolean willLogInfo() {
		return willLog(Level.Info);
	}

	public boolean willLogDebug() {
		return willLog(Level.Debug);
	}

	public void log(@Nonnull Level level, @Nullable Object message) {
		if (!willLog(level))
			return;
		if (formatter != null)
			message = String.format("%s %s", formatter.format(new Date()), message);

		switch (level) {
			case Error:
				Gdx.app.error(tag, String.valueOf(message));
				break;
			case Info:
				Gdx.app.log(tag, String.valueOf(message));
				break;
			case Debug:
				Gdx.app.debug(tag, String.valueOf(message));
				break;
		}
	}

	public void log(@Nonnull Level level, @Nonnull Throwable throwable) {
		if (!willLog(level))
			return;

		if (throwable instanceof Error)
			log(level, "Error", throwable);
		else if (throwable instanceof RuntimeException)
			log(level, "Unchecked exception", throwable);
		else
			log(level, "Checked exception", throwable);
	}

	public void log(@Nonnull Level level, @Nullable Object message, @Nonnull Throwable throwable) {
		if (!willLog(level))
			return;
		if (formatter != null)
			message = String.format("%s %s", formatter.format(new Date()), message);

		switch (level) {
			case Error:
				Gdx.app.error(tag, String.valueOf(message), throwable);
				break;
			case Info:
				Gdx.app.log(tag, String.valueOf(message), throwable);
				break;
			case Debug:
				Gdx.app.debug(tag, String.valueOf(message), throwable);
				break;
		}
	}

	public void log(@Nonnull Level level, @Nonnull String format, @Nonnull Object... args) {
		if (willLog(level))
			log(level, String.format(format, args));
	}

	public void error(@Nullable Object message) {
		log(Level.Error, message);
	}

	public void error(@Nonnull Throwable throwable) {
		log(Level.Error, throwable);
	}

	public void error(@Nullable Object message, @Nonnull Throwable throwable) {
		log(Level.Error, message, throwable);
	}

	public void error(@Nonnull String format, @Nonnull Object... args) {
		log(Level.Error, format, args);
	}

	public void info(@Nullable Object message) {
		log(Level.Info, message);
	}

	public void info(@Nonnull Throwable throwable) {
		log(Level.Info, throwable);
	}

	public void info(@Nullable Object message, @Nonnull Throwable throwable) {
		log(Level.Info, message, throwable);
	}

	public void info(@Nonnull String format, @Nonnull Object... args) {
		log(Level.Info, format, args);
	}

	public void debug(@Nullable Object message) {
		log(Level.Debug, message);
	}

	public void debug(@Nonnull Throwable throwable) {
		log(Level.Debug, throwable);
	}

	public void debug(@Nullable Object message, @Nonnull Throwable throwable) {
		log(Level.Debug, message, throwable);
	}

	public void debug(@Nonnull String format, @Nonnull Object... args) {
		log(Level.Debug, format, args);
	}

	public void log(@Nonnull Level level, @Nonnull LogMessageCallback callback) {
		if (willLog(level))
			callback.log(message -> log(level, message));
	}

	public void log(@Nonnull Level level, @Nonnull LogMessageThrowableCallback callback) {
		if (willLog(level))
			callback.log((message, throwable) -> log(level, message, throwable));
	}

	public void log(@Nonnull Level level, @Nonnull LogThrowableCallback callback) {
		if (willLog(level))
			callback.log(throwable -> log(level, throwable));
	}

	public void log(@Nonnull Level level, @Nonnull LogFormatCallback callback) {
		if (willLog(level))
			callback.log((format, args) -> log(level, format, args));
	}

	public void error(@Nonnull LogMessageCallback callback) {
		log(Level.Error, callback);
	}

	public void error(@Nonnull LogMessageThrowableCallback callback) {
		log(Level.Error, callback);
	}

	public void error(@Nonnull LogThrowableCallback callback) {
		log(Level.Error, callback);
	}

	public void error(@Nonnull LogFormatCallback callback) {
		log(Level.Error, callback);
	}

	public void info(@Nonnull LogMessageCallback callback) {
		log(Level.Info, callback);
	}

	public void info(@Nonnull LogMessageThrowableCallback callback) {
		log(Level.Info, callback);
	}

	public void info(@Nonnull LogThrowableCallback callback) {
		log(Level.Info, callback);
	}

	public void info(@Nonnull LogFormatCallback callback) {
		log(Level.Info, callback);
	}

	public void debug(@Nonnull LogMessageCallback callback) {
		log(Level.Debug, callback);
	}

	public void debug(@Nonnull LogMessageThrowableCallback callback) {
		log(Level.Debug, callback);
	}

	public void debug(@Nonnull LogThrowableCallback callback) {
		log(Level.Debug, callback);
	}

	public void debug(@Nonnull LogFormatCallback callback) {
		log(Level.Debug, callback);
	}

	public interface MessageLogger {
		void log(@Nullable Object message);
	}

	public interface MessageThrowableLogger {
		void log(@Nullable Object message, @Nonnull Throwable throwable);
	}

	public interface ThrowableLogger {
		void log(@Nonnull Throwable throwable);
	}

	public interface FormatLogger {
		void log(@Nonnull String format, @Nonnull Object... args);
	}

	public interface LogMessageCallback {
		void log(@Nonnull MessageLogger logger);
	}

	public interface LogMessageThrowableCallback {
		void log(@Nonnull MessageThrowableLogger logger);
	}

	public interface LogThrowableCallback {
		void log(@Nonnull ThrowableLogger logger);
	}

	public interface LogFormatCallback {
		void log(@Nonnull FormatLogger logger);
	}

	public enum Level {
		None(Application.LOG_NONE), Error(Application.LOG_ERROR), Info(Application.LOG_INFO), Debug(Application.LOG_DEBUG);

		public final int gdxLevel;

		Level(int gdxLevel) {
			this.gdxLevel = gdxLevel;
		}
	}
}