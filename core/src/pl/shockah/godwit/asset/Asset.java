package pl.shockah.godwit.asset;

public abstract class Asset<T> {
	public abstract void load();

	public abstract void unload();

	public abstract void finishLoading();

	public abstract T get();
}