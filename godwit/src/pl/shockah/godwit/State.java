package pl.shockah.godwit;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

import pl.shockah.godwit.asset.Asset;

public class State extends EntityGroup<Entity> {
	@Nonnull protected final List<Asset<?>> retainedAssets = new ArrayList<>();

	final void create() {
		if (isCreated() || isDestroyed())
			return;
		setCreated();
		onCreate();
	}

	@Override
	protected void onDestroy(@Nonnull Entity entity) {
		super.onDestroy(entity);
		for (Asset<?> asset : retainedAssets) {
			asset.unload();
		}
		retainedAssets.clear();
	}

	public void loadAsset(Asset<?> asset) {
		asset.load();
		retainedAssets.add(asset);
	}
}