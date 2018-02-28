package pl.shockah.godwit.asset;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetLoaderParameters;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.SynchronousAssetLoader;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;

import pl.shockah.jay.JSONObject;
import pl.shockah.jay.JSONParser;

public class JSONObjectLoader extends SynchronousAssetLoader<JSONObject, JSONObjectLoader.JSONObjectParameter> {
	public JSONObjectLoader(FileHandleResolver resolver) {
		super(resolver);
	}

	@Override
	public Array<AssetDescriptor> getDependencies(String fileName, FileHandle file, JSONObjectParameter parameter) {
		return null;
	}

	@Override
	public JSONObject load(AssetManager assetManager, String fileName, FileHandle file, JSONObjectParameter parameter) {
		return new JSONParser().parseObject(file.readString("UTF-8"));
	}

	public static class JSONObjectParameter extends AssetLoaderParameters<JSONObject> {
	}
}