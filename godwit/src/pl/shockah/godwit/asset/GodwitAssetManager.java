package pl.shockah.godwit.asset;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.annotation.Nonnull;

import java8.util.stream.StreamSupport;

public class GodwitAssetManager {
	@Nonnull public FileHandleResolver resolver = new InternalFileHandleResolver();

	@Nonnull private final Map<Class<?>, GodwitAssetLoader<?, ?, ?>> loaders = new HashMap<>();
	@Nonnull private final List<GodwitAsset<?>> loaded = new ArrayList<>();
	@Nonnull private final List<GodwitAssetParameters<?>> queuedAsync = new LinkedList<>();
	@Nonnull private final List<GodwitAssetLoader.AsyncResult<?, ?>> queuedSync = new LinkedList<>();
	@Nonnull private final ExecutorService executor;

	public GodwitAssetManager() {
		executor = Executors.newFixedThreadPool(1, runnable -> {
			Thread thread = new Thread(runnable);
			thread.setDaemon(true);
			return thread;
		});
	}

	public synchronized <T, L extends GodwitAssetLoader<T, ?, ?>> void registerLoader(Class<T> assetClass, L loader) {
		loaders.put(assetClass, loader);
	}

	public synchronized boolean isLoaded(@Nonnull GodwitAsset<?> asset) {
		return loaded.contains(asset);
	}

	public synchronized boolean isQueued(@Nonnull GodwitAssetParameters<?> parameters) {
		if (queuedAsync.contains(parameters))
			return true;
		synchronized (queuedSync) {
			return StreamSupport.stream(queuedSync).anyMatch(async -> async.parameters == parameters);
		}
	}

	private void loadAsync(@Nonnull GodwitAsset<?> asset) {
		executor.submit(() -> loadAsyncTask(asset));
	}

	private <T> void loadAsyncNow(@Nonnull GodwitAsset<T> asset) {
		executor.submit(() -> {
			GodwitAssetLoader.AsyncResult<T, GodwitAssetParameters<T>> asyncResult = loadAsyncTask(asset);
			Gdx.app.postRunnable(() -> loadSync(asyncResult));
		});
	}

	@SuppressWarnings("unchecked")
	private synchronized <T> GodwitAssetLoader.AsyncResult<T, GodwitAssetParameters<T>> loadAsyncTask(@Nonnull GodwitAsset<T> asset) {
		GodwitAssetLoader<?, ?, ?> loader = loaders.get(asset.parameters.clazz);
		if (loader == null)
			throw new NullPointerException(String.format("No registered loader for class %s.", asset.parameters.clazz.getName()));

		GodwitAssetLoader.AsyncResult<T, GodwitAssetParameters<T>> asyncResult = ((GodwitAssetLoader<T, GodwitAssetParameters<T>, ?>) loader).loadAsynchronous(asset.parameters);
		queuedSync.add(asyncResult);
		queuedAsync.remove(asset.parameters);
		return asyncResult;
	}

	@SuppressWarnings("unchecked")
	private synchronized <T> GodwitAsset<T> loadSync(@Nonnull GodwitAssetLoader.AsyncResult<T, ?> asyncResult) {
		GodwitAssetLoader<?, ?, ?> loader = loaders.get(asyncResult.parameters.clazz);
		if (loader == null)
			throw new NullPointerException(String.format("No registered loader for class %s.", asyncResult.parameters.clazz.getName()));

		asyncResult.asset.asset = ((GodwitAssetLoader<T, ?, GodwitAssetLoader.AsyncResult<T, ?>>) loader).loadSynchronous(asyncResult);
		queuedSync.remove(asyncResult);
		loaded.add(asyncResult.asset);
		return asyncResult.asset;
	}
}