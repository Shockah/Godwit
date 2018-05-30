package pl.shockah.godwit.asset;

import javax.annotation.Nonnull;

public abstract class Asset<T> {
	public abstract void load();

	public abstract void unload();

	public abstract void finishLoading();

	@Nonnull
	public abstract T get();
}