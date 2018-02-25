package pl.shockah.godwit.asset;

import javax.annotation.Nonnull;

public abstract class GodwitSynchronousAssetLoader extends GodwitAssetLoader {
	public GodwitSynchronousAssetLoader(@Nonnull GodwitAssetManager assetManager) {
		super(assetManager);
	}
}