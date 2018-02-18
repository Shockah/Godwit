package pl.shockah.godwit.util;

import com.badlogic.gdx.utils.JsonValue;

import javax.annotation.Nonnull;

import lombok.experimental.Delegate;

public class JSON {
	private interface DelegateExclusions {
		JsonValue get(int index);
		JsonValue get(String name);
	}

	@Delegate(excludes = DelegateExclusions.class)
	@Nonnull public final JsonValue wrapped;

	public JSON(@Nonnull JsonValue wrapped) {
		this.wrapped = wrapped;
	}

	public JSON get(int index) {
		JsonValue value = wrapped.get(index);
		return value != null ? new JSON(value) : null;
	}

	public JSON get(String name) {
		JsonValue value = wrapped.get(name);
		return value != null ? new JSON(value) : null;
	}
}