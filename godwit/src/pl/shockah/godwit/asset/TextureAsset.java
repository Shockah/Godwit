package pl.shockah.godwit.asset;

import com.badlogic.gdx.assets.loaders.TextureLoader;
import com.badlogic.gdx.graphics.Texture;

import javax.annotation.Nonnull;

import lombok.experimental.Delegate;
import lombok.experimental.UtilityClass;

public class TextureAsset extends Asset<Texture> {
	@UtilityClass
	public static final class Defaults {
		@Nonnull public static Texture.TextureFilter filter = Texture.TextureFilter.Nearest;
	}

	public TextureAsset(@Nonnull String fileName, @Nonnull Class<Texture> clazz) {
		this(fileName, clazz, Defaults.filter);
	}

	public TextureAsset(@Nonnull String fileName, @Nonnull Class<Texture> clazz, @Nonnull Texture.TextureFilter filter) {
		super(fileName, clazz, new TextureLoader.TextureParameter() {{
			magFilter = filter;
			minFilter = filter;
		}});
	}

	@Delegate
	@Override
	public Texture get() {
		return super.get();
	}
}