package pl.shockah.godwit.asset;

import java.util.Collections;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class Asset<T> {
	@Nonnull public final AssetManager manager;
	@Nonnull public final GodwitAssetParameters<T> parameters;
	@Nonnull public final List<GodwitAssetParameters<T>> dependencies;
	@Nullable T asset;

	public Asset(@Nonnull AssetManager manager, @Nonnull GodwitAssetParameters<T> parameters, @Nonnull List<GodwitAssetParameters<T>> dependencies) {
		this.manager = manager;
		this.parameters = parameters;
		this.dependencies = Collections.unmodifiableList(dependencies);
	}

	public boolean isLoaded() {
		return manager.isLoaded(parameters);
	}

	public boolean isQueued() {
		return manager.isQueued(parameters);
	}

	@Nonnull public T get() {
		if (asset == null)
			asset = manager.get(parameters);
		if (asset == null)
			throw new NullPointerException();
		return asset;
	}
}