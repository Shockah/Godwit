package pl.shockah.godwit.collection;

import java.lang.reflect.Array;

import javax.annotation.Nonnull;

public class Array2D<T> {
	public final int width;
	public final int height;
	public final int length;

	@Nonnull protected final T[] array;

	@SuppressWarnings("unchecked")
	public Array2D(@Nonnull Class<? extends T> clazz, int width, int height) {
		this.width = width;
		this.height = height;
		length = width * height;
		array = (T[])Array.newInstance(clazz, length);
	}

	protected final int getIndex(int x, int y) {
		if (x < 0 || x >= width)
			throw new ArrayIndexOutOfBoundsException();
		if (y < 0 || y >= height)
			throw new ArrayIndexOutOfBoundsException();
		return y * width + x;
	}

	public T get(int x, int y) {
		return array[getIndex(x, y)];
	}
}