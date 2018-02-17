package pl.shockah.godwit.asset

import com.badlogic.gdx.assets.AssetManager
import groovy.transform.CompileStatic
import pl.shockah.godwit.Godwit

import javax.annotation.Nonnull

@CompileStatic
class Asset<T> {
	@Nonnull final String fileName
	@Nonnull final Class<T> clazz

	Asset(String fileName, Class<T> clazz) {
		this.fileName = fileName
		this.clazz = clazz
	}

	protected static AssetManager getAssetManager() {
		return Godwit.instance.assetManager
	}

	void load() {
		assetManager.load(fileName, clazz)
	}

	void unload() {
		assetManager.unload(fileName)
	}

	void finishLoading() {
		assetManager.finishLoadingAsset(fileName)
	}

	T get() {
		finishLoading()
		return assetManager.get(fileName, clazz)
	}
}