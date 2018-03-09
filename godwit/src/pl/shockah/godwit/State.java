package pl.shockah.godwit;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

import pl.shockah.godwit.asset.Asset;

public class State extends RenderGroupEntity {
	@Nonnull private final List<Asset<?>> retainedAssets = new ArrayList<>();

	public void loadAsset(Asset<?> asset) {
		asset.load();
		retainedAssets.add(asset);
	}

	@Override
	public void onAddedToHierarchy(@Nonnull RenderGroupEntity renderGroup) {
		throw new IllegalStateException();
	}

	@Override
	public void onRemovedFromHierarchy(@Nonnull RenderGroupEntity renderGroup) {
		throw new IllegalStateException();
	}

	@Override
	public void onAddedToParent() {
		super.onAddedToParent();
		for (Entity entity : children.get()) {
			callAddedToHierarchy(entity, this);
		}
	}

	@Override
	public void onRemovedFromParent() {
		super.onRemovedFromParent();
		for (Entity entity : children.get()) {
			callRemovedFromHierarchy(entity, this);
		}
		for (Asset<?> asset : retainedAssets) {
			asset.unload();
		}
		retainedAssets.clear();
	}
}