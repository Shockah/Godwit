package pl.shockah.godwit;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.files.FileHandle;

public class LocalOrInternalFileHandleResolver implements FileHandleResolver {
	@Override
	public FileHandle resolve(String fileName) {
		if (Gdx.files.isLocalStorageAvailable() && new FileHandle(String.format("%s/%s", Gdx.files.getLocalStoragePath(), fileName)).exists()) {
			return Gdx.files.local(fileName);
		} else {
			return Gdx.files.internal(fileName);
		}
	}
}
