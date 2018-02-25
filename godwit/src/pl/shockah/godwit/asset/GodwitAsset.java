package pl.shockah.godwit.asset;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class GodwitAsset<T> {
	@Nonnull public final GodwitAssetManager manager;
	@Nonnull public final GodwitAssetParameters<T> parameters;
	@Nullable T asset;

	public GodwitAsset(@Nonnull GodwitAssetManager manager, @Nonnull GodwitAssetParameters<T> parameters) {
		this.manager = manager;
		this.parameters = parameters;
	}

	public boolean isLoaded() {
		return asset != null;
	}

	public boolean isQueued() {
		return manager.isQueued(parameters);
	}

	@Nonnull public T get() {
		if (asset == null)
			throw new NullPointerException();
		return asset;
	}
}