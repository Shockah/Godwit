package pl.shockah.godwit.collection;

import lombok.EqualsAndHashCode;

@EqualsAndHashCode
public final class Box<T> {
	public T value;

	public Box() {
		this(null);
	}

	public Box(T value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return String.format("[Box: %s]", String.valueOf(value));
	}
}