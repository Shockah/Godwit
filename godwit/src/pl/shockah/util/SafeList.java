package pl.shockah.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.annotation.Nonnull;

public class SafeList<T> {
	@Nonnull private final List<T> wrapped;
	@Nonnull private final List<T> unmodifiableList;
	@Nonnull private final List<T> waitingToAdd = new ArrayList<>();
	@Nonnull private final List<T> waitingToRemove = new ArrayList<>();

	public SafeList(@Nonnull List<T> wrapped) {
		this.wrapped = wrapped;
		unmodifiableList = Collections.unmodifiableList(wrapped);
	}

	@Nonnull public List<T> get() {
		return unmodifiableList;
	}

	public void add(T element) {
		waitingToAdd.add(element);
	}

	public void remove(T element) {
		waitingToRemove.add(element);
	}

	public void update() {
		if (!waitingToAdd.isEmpty()) {
			wrapped.addAll(waitingToAdd);
			waitingToAdd.clear();
		}
		if (!waitingToRemove.isEmpty()) {
			wrapped.removeAll(waitingToRemove);
			waitingToRemove.clear();
		}
	}
}