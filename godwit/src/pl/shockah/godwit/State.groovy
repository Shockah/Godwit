package pl.shockah.godwit

import groovy.transform.CompileStatic
import groovy.transform.PackageScope
import pl.shockah.godwit.asset.Asset

import javax.annotation.Nonnull

@CompileStatic
class State extends EntityGroup<Entity> {
	@Nonnull protected final List<Asset<?>> retainedAssets = []

	@PackageScope final void create() {
		if (created || destroyed)
			return
		created = true
		onCreate()
	}

	@Override
	void onDestroy() {
		super.onDestroy()
		for (Asset<?> asset : retainedAssets) {
			asset.unload()
		}
		retainedAssets.clear()
	}

	void loadAsset(Asset<?> asset) {
		asset.load()
		retainedAssets.add(asset)
	}
}