package pl.shockah.godwit.collection;

import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.annotation.Nonnull;

import java8.util.Maps;
import java8.util.stream.Collectors;
import java8.util.stream.StreamSupport;
import lombok.EqualsAndHashCode;

public class HashMultiSet<E> extends AbstractMultiSet<E> {
	private int size = 0;
	@Nonnull private final Map<E, Count> counts = new HashMap<>();

	public HashMultiSet() {
	}

	public HashMultiSet(@Nonnull Collection<? extends E> collection) {
		this();
		for (E element : collection) {
			add(element);
		}
	}

	public HashMultiSet(@Nonnull MultiSet<? extends E> multiSet) {
		this();
		for (Entry<? extends E> entry : multiSet.entries()) {
			add(entry.getElement(), entry.getCount());
		}
	}

	@Override
	public void clear() {
		size = 0;
		counts.clear();
	}

	@Override
	public int size() {
		return size;
	}

	@Override
	public int distinctSize() {
		return counts.size();
	}

	@Override
	public int count(Object element) {
		return Maps.getOrDefault(counts, element, new Count()).value;
	}

	@Override
	public int add(E element) {
		size++;
		return ++Maps.computeIfAbsent(counts, element, key -> new Count()).value;
	}

	@Override
	public int add(E element, int occurences) {
		size += occurences;
		Count count = Maps.computeIfAbsent(counts, element, key -> new Count());
		count.value += occurences;
		return count.value;
	}

	@Override
	public int remove(E element) {
		Count count = Maps.getOrDefault(counts, element, new Count());
		if (count.value == 0)
			return 0;

		size--;
		if (count.value == 1) {
			counts.remove(element);
			return 0;
		}

		return --count.value;
	}

	@Override
	public int remove(E element, int occurences) {
		Count count = Maps.getOrDefault(counts, element, new Count());
		if (count.value == 0)
			return 0;

		if (count.value <= occurences) {
			size -= count.value;
			counts.remove(element);
			return 0;
		} else {
			size -= occurences;
			count.value -= occurences;
			return count.value;
		}
	}

	@Override
	@Nonnull public Set<E> distinct() {
		return new HashSet<>(counts.keySet());
	}

	@Override
	@Nonnull public Iterator<E> distinctIterator() {
		return new Iterator<E>() {
			final Iterator<Map.Entry<E, Count>> backingEntries = counts.entrySet().iterator();
			Map.Entry<E, Count> toRemove;

			@Override
			public boolean hasNext() {
				return backingEntries.hasNext();
			}

			@Override
			public E next() {
				final Map.Entry<E, Count> mapEntry = backingEntries.next();
				toRemove = mapEntry;
				return mapEntry.getKey();
			}

			@Override
			public void remove() {
				if (toRemove == null)
					throw new IllegalStateException("Already removed.");
				size -= toRemove.getValue().value;
				backingEntries.remove();
				toRemove = null;
			}
		};
	}

	@Override
	@Nonnull public Set<Entry<E>> entries() {
		return StreamSupport.stream(counts.entrySet())
				.map(entry -> new MultiSet.Entry<E>() {
					@Override
					public E getElement() {
						return entry.getKey();
					}

					@Override
					public int getCount() {
						return entry.getValue().value;
					}
				})
				.collect(Collectors.toSet());
	}

	@Override
	@Nonnull public Iterator<E> iterator() {
		return new Iterator<E>() {
			final Iterator<Map.Entry<E, Count>> entryIterator = counts.entrySet().iterator();
			Map.Entry<E, Count> currentEntry;
			int occurrencesLeft;
			boolean canRemove;

			@Override
			public boolean hasNext() {
				return occurrencesLeft > 0 || entryIterator.hasNext();
			}

			@Override
			public E next() {
				if (occurrencesLeft == 0) {
					currentEntry = entryIterator.next();
					occurrencesLeft = currentEntry.getValue().value;
				}
				occurrencesLeft--;
				canRemove = true;
				return currentEntry.getKey();
			}

			@Override
			public void remove() {
				if (!canRemove)
					throw new IllegalStateException("Already removed.");
				int frequency = currentEntry.getValue().value;
				if (frequency <= 0)
					throw new ConcurrentModificationException();

				Count count = currentEntry.getValue();
				if (count.value == 1)
					entryIterator.remove();
				else
					count.value--;
				size--;
				canRemove = false;
			}
		};
	}

	@EqualsAndHashCode
	private static class Count {
		private int value = 0;
	}
}