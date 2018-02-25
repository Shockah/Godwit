package pl.shockah.godwit.asset;

import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public abstract class GodwitAssetLoader<T, Params extends GodwitAssetParameters<T>, Async extends GodwitAssetLoader.AsyncResult<T, Params>> {
	@Nonnull public final GodwitAssetManager assetManager;

	public GodwitAssetLoader(@Nonnull GodwitAssetManager assetManager) {
		this.assetManager = assetManager;
	}

	@Nullable public List<GodwitAssetParameters<?>> getDependencies() {
		return null;
	}

	@Nonnull public abstract Async loadAsynchronous(@Nonnull Params parameters);

	@Nonnull public abstract T loadSynchronous(@Nonnull Async asyncResult);

	public static class AsyncResult<T, Params extends GodwitAssetParameters<T>> {
		@Nonnull public final GodwitAsset<T> asset;
		@Nonnull public final Params parameters;

		public AsyncResult(@Nonnull GodwitAsset<T> asset, @Nonnull Params parameters) {
			this.asset = asset;
			this.parameters = parameters;
		}
	}
}