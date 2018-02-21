package pl.shockah.godwit.asset;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetLoaderParameters;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.utils.Array;

import java.lang.reflect.Field;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import lombok.Getter;
import pl.shockah.godwit.Godwit;

public class Asset<T> {
	@Getter(lazy = true)
	private static final Field loadQueueField = getLoadQueueFieldLazy();

	@Nonnull public final String fileName;
	@Nonnull public final Class<T> clazz;
	@Nullable public final AssetLoaderParameters<T> parameters;
	@Nonnull public final AssetDescriptor<T> descriptor;

	public Asset(@Nonnull String fileName, @Nonnull Class<T> clazz) {
		this(fileName, clazz, null);
	}

	public Asset(@Nonnull String fileName, @Nonnull Class<T> clazz, @Nullable AssetLoaderParameters<T> parameters) {
		this.fileName = fileName;
		this.clazz = clazz;
		this.parameters = parameters;
		descriptor = new AssetDescriptor<T>(fileName, clazz, parameters);
	}

	private static Field getLoadQueueFieldLazy() {
		try {
			Field field = AssetManager.class.getDeclaredField("loadQueue");
			field.setAccessible(true);
			return field;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	protected static AssetManager getAssetManager() {
		return Godwit.getInstance().getAssetManager();
	}

	@SuppressWarnings("unchecked")
	private static Array<AssetDescriptor> getLoadQueue() {
		try {
			return (Array<AssetDescriptor>)getLoadQueueField().get(getAssetManager());
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public void load() {
		getAssetManager().load(descriptor);
	}

	public void unload() {
		getAssetManager().unload(fileName);
	}

	public void finishLoading() {
		AssetManager manager = getAssetManager();
		if (manager.isLoaded(fileName, clazz))
			return;
		for (AssetDescriptor<?> descriptor : getLoadQueue()) {
			if (descriptor.type == this.descriptor.type && descriptor.fileName.equals(this.descriptor.fileName)) {
				manager.finishLoadingAsset(fileName);
				return;
			}
		}
		throw new IllegalStateException(String.format("Asset %s is not queued for loading.", descriptor));
	}

	public T get() {
		finishLoading();
		return getAssetManager().get(descriptor);
	}
}