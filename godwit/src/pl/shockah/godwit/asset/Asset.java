package pl.shockah.godwit.asset;

import com.badlogic.gdx.assets.AssetLoaderParameters;
import com.badlogic.gdx.assets.AssetManager;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import pl.shockah.godwit.Godwit;

public class Asset<T> {
	@Nonnull public final String fileName;
	@Nonnull public final Class<T> clazz;
	@Nullable public final AssetLoaderParameters<T> parameters;

	public Asset(@Nonnull String fileName, @Nonnull Class<T> clazz) {
		this(fileName, clazz, null);
	}

	public Asset(@Nonnull String fileName, @Nonnull Class<T> clazz, @Nullable AssetLoaderParameters<T> parameters) {
		this.fileName = fileName;
		this.clazz = clazz;
		this.parameters = parameters;
	}

	protected static AssetManager getAssetManager() {
		return Godwit.getInstance().getAssetManager();
	}

	public void load() {
		getAssetManager().load(fileName, clazz, parameters);
	}

	public void unload() {
		getAssetManager().unload(fileName);
	}

	public void finishLoading() {
		AssetManager manager = getAssetManager();
		if (manager.isLoaded(fileName, clazz))
			return;
		manager.finishLoadingAsset(fileName);
	}

	public T get() {
		finishLoading();
		return getAssetManager().get(fileName, clazz);
	}
}