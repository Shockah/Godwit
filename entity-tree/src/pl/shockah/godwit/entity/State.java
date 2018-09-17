package pl.shockah.godwit.entity;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

import pl.shockah.godwit.asset.Asset;

public class State extends RenderGroup {
	@Nonnull
	public final CameraGroup game = new CameraGroup();

	@Nonnull
	public final CameraGroup ui = new CameraGroup();

	@Nonnull
	private final List<Asset<?>> retainedAssets = new ArrayList<>();

	public State() {
		addChild(game);
		addChild(ui);
	}

	@Override
	public void addChild(@Nonnull Entity entity) {
		if (entity != game && entity != ui)
			throw new UnsupportedOperationException("Entities shouldn't be added to States directly. Use State#game or State#ui instead.");
		else
			super.addChild(entity);
	}

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