package pl.shockah.godwit.collection;

import java8.util.Objects;

public final class UnorderedPair<T> {
	public final T first;
	public final T second;

	public UnorderedPair(T first, T second) {
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

	public int size() {
		return (first != null ? 1 : 0) + (second != null ? 1 : 0);
	}

	public boolean isEmpty() {
		return first == null && second == null;
	}

	public boolean isFull() {
		return first != null && second != null;
	}

	public boolean contains(Object o) {
		return Objects.equals(first, o) || Objects.equals(second, o);
	}
}