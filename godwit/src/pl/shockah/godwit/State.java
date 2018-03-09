package pl.shockah.godwit;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

import pl.shockah.godwit.asset.Asset;

public class State extends RenderGroup {
	@Nonnull private final List<Asset<?>> retainedAssets = new ArrayList<>();

	@Override
	public void onRemovedFromHierarchy() {
		super.onRemovedFromHierarchy();
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