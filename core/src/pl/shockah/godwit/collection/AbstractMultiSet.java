package pl.shockah.godwit.collection;

public abstract class AbstractMultiSet<E> implements MultiSet<E> {
	@SuppressWarnings("unchecked")
	@Override
	public boolean equals(Object o) {
		if (!(o instanceof AbstractMultiSet<?>))
			return false;
		AbstractMultiSet<E> other = (AbstractMultiSet<E>)o;

		if (size() != other.size())
			return false;
		if (distinctSize() != other.distinctSize())
			return false;
		for (Entry<E> entry : entries()) {
			int count = other.count(entry.getElement());
			if (count != entry.getCount())
				return false;
		}

		return true;
	}
}