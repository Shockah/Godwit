package pl.shockah.godwit.collection;

import java.util.Iterator;
import java.util.Set;

import javax.annotation.Nonnull;

public interface MultiSet<E> extends Iterable<E> {
	void clear();

	int size();

	int distinctSize();

	int count(Object element);

	int add(E element);

	int remove(E element);

	@Nonnull Set<E> distinct();

	@Nonnull Iterator<E> distinctIterator();

	@Nonnull Set<Entry<E>> entries();

	default boolean contains(Object element) {
		return count(element) != 0;
	}

	default int add(E element, int occurences) {
		int count = 0;
		for (int i = 0; i < occurences; i++) {
			count = add(element);
		}
		return count;
	}

	default int remove(E element, int occurences) {
		int count = 0;
		for (int i = 0; i < occurences; i++) {
			count = remove(element);
		}
		return count;
	}

	default boolean isEmpty() {
		return size() == 0;
	}

	interface Entry<E> {
		E getElement();
		int getCount();
	}
}