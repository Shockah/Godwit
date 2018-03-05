package pl.shockah.godwit.asset;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public abstract class ComplexAsset<T> extends Asset<T> {
	@Nonnull protected final Set<Asset<?>> dependencies = new HashSet<>();
	private int referenceCount = 0;
	@Nullable private T asset = null;

	public ComplexAsset(Asset<?>... assets) {
		dependencies.addAll(Arrays.asList(assets));
	}

	@Override
	public void load() {
		for (Asset<?> asset : dependencies) {
			asset.load();
		}
		referenceCount++;
	}

	@Override
	public void unload() {
		for (Asset<?> asset : dependencies) {
			asset.unload();
		}
		if (--referenceCount == 0)
			asset = null;
	}

	@Override
	public void finishLoading() {
		for (Asset<?> asset : dependencies) {
			asset.finishLoading();
		}
	}

	@Override
	public T get() {
		if (asset == null)
			asset = create();
		return asset;
	}

	public abstract T create();
}