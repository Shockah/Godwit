package pl.shockah.godwit.asset;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.utils.TimeUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import java8.util.stream.StreamSupport;

public class AssetManager {
	@Nonnull public FileHandleResolver resolver = new InternalFileHandleResolver();

	@Nonnull private final Map<Class<?>, AssetLoader<?, ?, ?>> loaders = new HashMap<>();
	@Nonnull private final Map<GodwitAssetParameters<?>, ?> loaded = new HashMap<>();
	@Nonnull private final List<GodwitAssetParameters<?>> queuedAsync = new LinkedList<>();
	@Nonnull private final List<AssetLoader.AsyncResult<?, ?>> queuedSync = new LinkedList<>();
	@Nonnull private final ExecutorService executor;

	public AssetManager() {
		executor = Executors.newFixedThreadPool(1, runnable -> {
			Thread thread = new Thread(runnable);
			thread.setDaemon(true);
			return thread;
		});
	}

	public synchronized <T, L extends AssetLoader<T, ?, ?>> void registerLoader(Class<T> assetClass, L loader) {
		loaders.put(assetClass, loader);
	}

	@SuppressWarnings("unchecked")
	@Nullable public synchronized <T> T get(@Nonnull GodwitAssetParameters<?> parameters) {
		return (T)loaded.get(parameters);
	}

	public synchronized boolean isLoaded(@Nonnull Asset<?> asset) {
		return StreamSupport.stream(loaded.keySet()).anyMatch(parameters -> parameters == asset.parameters);
	}

	public synchronized boolean isLoaded(@Nonnull GodwitAssetParameters<?> parameters) {
		return loaded.keySet().contains(parameters);
	}

	public synchronized boolean isQueued(@Nonnull Asset<?> asset) {
		return isQueued(asset.parameters);
	}

	public synchronized boolean isQueued(@Nonnull GodwitAssetParameters<?> parameters) {
		if (queuedAsync.contains(parameters))
			return true;
		synchronized (queuedSync) {
			return StreamSupport.stream(queuedSync).anyMatch(async -> async.parameters == parameters);
		}
	}

	@Nonnull public synchronized List<GodwitAssetParameters<?>> finishUpdate() {
		List<GodwitAssetParameters<?>> result = new ArrayList<>();
		while (!queuedAsync.isEmpty() || !queuedSync.isEmpty()) {
			GodwitAssetParameters<?> loaded = updateOnce();
			if (loaded != null)
				result.add(loaded);
		}
		return result;
	}

	@Nonnull public synchronized List<GodwitAssetParameters<?>> update() {
		return update(Gdx.graphics.getDeltaTime() * 0.75f);
	}

	@Nonnull public synchronized List<GodwitAssetParameters<?>> update(float duration) {
		List<GodwitAssetParameters<?>> result = new ArrayList<>();
		long ms = TimeUtils.millis();
		while (TimeUtils.timeSinceMillis(ms) / 1000f < duration) {
			GodwitAssetParameters<?> loaded = updateOnce();
			if (loaded != null)
				result.add(loaded);
		}
		return result;
	}

	@Nullable public synchronized GodwitAssetParameters<?> updateOnce() {
		if (queuedSync.isEmpty())
			return null;

		AssetLoader.AsyncResult<?, ?> asyncResult = queuedSync[0];
		AssetLoader<?, ?, ?> loader = loaders.get(asyncResult.parameters.clazz);
		if (loader == null)
			throw new NullPointerException(String.format("No registered loader for class %s.", asyncResult.parameters.clazz.getName()));

		List<GodwitAssetParameters<?>> dependencies = loader.getDependencies();
		boolean ready = true;
		if (dependencies != null && !dependencies.isEmpty()) {
			for (GodwitAssetParameters<?> parameters : dependencies) {
				if (!isLoaded(parameters)) {
					ready = false;
					if (!isQueued(parameters)) {
						queuedAsync.add(0, parameters);
						loadAsync(parameters);
					}
				}
			}
		}

		if (ready)
			return loadSync(asyncResult);
		else
			return updateOnce();
	}

	public <T> void load(@Nonnull GodwitAssetParameters<T> parameters) {
		loadAsync(parameters);
	}

	private <T> void loadAsync(@Nonnull GodwitAssetParameters<T> parameters) {
		executor.submit(() -> loadAsyncTask(parameters));
	}

	private <T> void loadAsyncNow(@Nonnull GodwitAssetParameters<T> parameters) {
		executor.submit(() -> {
			AssetLoader.AsyncResult<T, GodwitAssetParameters<T>> asyncResult = loadAsyncTask(parameters);
			Gdx.app.postRunnable(() -> loadSync(asyncResult));
		});
	}

	@SuppressWarnings("unchecked")
	private synchronized <T> AssetLoader.AsyncResult<T, GodwitAssetParameters<T>> loadAsyncTask(@Nonnull GodwitAssetParameters<T> parameters) {
		AssetLoader<?, ?, ?> loader = loaders.get(parameters.clazz);
		if (loader == null)
			throw new NullPointerException(String.format("No registered loader for class %s.", parameters.clazz.getName()));

		AssetLoader.AsyncResult<T, GodwitAssetParameters<T>> asyncResult = ((AssetLoader<T, GodwitAssetParameters<T>, ?>) loader).loadAsynchronous(parameters);
		queuedSync.add(asyncResult);
		queuedAsync.remove(parameters);
		return asyncResult;
	}

	@SuppressWarnings("unchecked")
	private synchronized <T> GodwitAssetParameters<T> loadSync(@Nonnull AssetLoader.AsyncResult<T, ?> asyncResult) {
		AssetLoader<?, ?, ?> loader = loaders.get(asyncResult.parameters.clazz);
		if (loader == null)
			throw new NullPointerException(String.format("No registered loader for class %s.", asyncResult.parameters.clazz.getName()));

		T asset = ((AssetLoader<T, ?, AssetLoader.AsyncResult<T, ?>>) loader).loadSynchronous(asyncResult);
		queuedSync.remove(asyncResult);
		((Map)loaded).put(asyncResult.parameters, asset);
		return asyncResult.parameters;
	}
}