package pl.shockah.godwit.asset;

import javax.annotation.Nonnull;

public abstract class SynchronousAssetLoader extends AssetLoader {
	public SynchronousAssetLoader(@Nonnull AssetManager assetManager) {
		super(assetManager);
	}
}