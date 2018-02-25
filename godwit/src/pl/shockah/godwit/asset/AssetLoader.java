package pl.shockah.godwit.asset;

import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public abstract class AssetLoader<T, Params extends GodwitAssetParameters<T>, Async extends AssetLoader.AsyncResult<T, Params>> {
	@Nonnull public final AssetManager assetManager;

	public AssetLoader(@Nonnull AssetManager assetManager) {
		this.assetManager = assetManager;
	}

	@Nullable public List<GodwitAssetParameters<?>> getDependencies() {
		return null;
	}

	@Nonnull public abstract Async loadAsynchronous(@Nonnull Params parameters);

	@Nonnull public abstract T loadSynchronous(@Nonnull Async asyncResult);

	public static class AsyncResult<T, Params extends GodwitAssetParameters<T>> {
		@Nonnull public final Params parameters;

		public AsyncResult(@Nonnull Params parameters) {
			this.parameters = parameters;
		}
	}
}