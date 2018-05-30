package pl.shockah.godwit;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.files.FileHandle;

import javax.annotation.Nullable;

public class LocalOrInternalFileHandleResolver implements FileHandleResolver {
	@Nullable
	public final String assetDirectory;

	public LocalOrInternalFileHandleResolver() {
		this(null);
	}

	public LocalOrInternalFileHandleResolver(@Nullable String assetDirectory) {
		this.assetDirectory = assetDirectory;
	}

	private String getLocalFileName(String fileName) {
		return assetDirectory != null ? String.format("%s/%s", assetDirectory, fileName) : fileName;
	}

	@Override
	public FileHandle resolve(String fileName) {
		if (Gdx.files.isLocalStorageAvailable() && new FileHandle(String.format("%s/%s", Gdx.files.getLocalStoragePath(), getLocalFileName(fileName))).exists()) {
			return Gdx.files.local(getLocalFileName(fileName));
		} else {
			return Gdx.files.internal(fileName);
		}
	}
}
