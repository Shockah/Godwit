package pl.shockah.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.annotation.Nonnull;

import pl.shockah.func.Action1;

public class SafeList<T> {
	@Nonnull private final List<T> wrapped;
	@Nonnull private final List<T> unmodifiableList;
	@Nonnull private final List<T> waitingToAdd = new ArrayList<>();
	@Nonnull private final List<T> waitingToRemove = new ArrayList<>();
	private int busyCounter = 0;

	public SafeList(@Nonnull List<T> wrapped) {
		this.wrapped = wrapped;
		unmodifiableList = Collections.unmodifiableList(wrapped);
	}

	public void add(T element) {
		if (busyCounter == 0)
			wrapped.add(element);
		else
			waitingToAdd.add(element);
	}

	public void remove(T element) {
		if (busyCounter == 0)
			wrapped.remove(element);
		else
			waitingToRemove.add(element);
	}

	public void use(Action1<List<T>> func) {
		busyCounter++;
		func.call(unmodifiableList);
		if (--busyCounter == 0) {
			wrapped.addAll(waitingToAdd);
			wrapped.removeAll(waitingToRemove);

			waitingToAdd.clear();
			waitingToRemove.clear();
		}
	}

	public List<T> getSafeCopy() {
		return new ArrayList<>(wrapped);
	}

	public void iterate(Action1<T> func) {
		use(list -> {
			for (T element : list) {
				func.call(element);
			}
		});
	}
}