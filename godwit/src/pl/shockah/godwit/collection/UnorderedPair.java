package pl.shockah.godwit.collection;

import javax.annotation.Nullable;

import java8.util.Objects;

public final class UnorderedPair<T> {
	@Nullable public final T first;
	@Nullable public final T second;

	public UnorderedPair(@Nullable T first, @Nullable T second) {
		this.first = first;
		this.second = second;
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof UnorderedPair<?>))
			return false;
		UnorderedPair<T> other = (UnorderedPair<T>)obj;
		return (Objects.equals(first, other.first) && Objects.equals(second, other.second))
				|| (Objects.equals(first, other.second) && Objects.equals(second, other.first));
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(first) ^ Objects.hashCode(second);
	}
}