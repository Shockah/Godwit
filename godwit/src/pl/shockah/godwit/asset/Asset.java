package pl.shockah.godwit.asset;

import com.badlogic.gdx.assets.AssetManager;

import javax.annotation.Nonnull;

import pl.shockah.godwit.Godwit;

public class Asset<T> {
	@Nonnull public final String fileName;
	@Nonnull public final Class<T> clazz;

	public Asset(@Nonnull String fileName, @Nonnull Class<T> clazz) {
		this.fileName = fileName;
		this.clazz = clazz;
	}

	protected static AssetManager getAssetManager() {
		return Godwit.getInstance().assetManager;
	}

	public void load() {
		getAssetManager().load(fileName, clazz);
	}

	public void unload() {
		getAssetManager().unload(fileName);
	}

	public void finishLoading() {
		getAssetManager().finishLoadingAsset(fileName);
	}

	public T get() {
		finishLoading();
		return getAssetManager().get(fileName, clazz);
	}
}