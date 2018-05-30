package pl.shockah.godwit.asset;

import com.badlogic.gdx.assets.loaders.TextureLoader;
import com.badlogic.gdx.graphics.Texture;

import javax.annotation.Nonnull;

import lombok.experimental.Delegate;
import lombok.experimental.UtilityClass;

public class TextureAsset extends SingleAsset<Texture> {
	@UtilityClass
	public static final class Defaults {
		@Nonnull
		public static Texture.TextureFilter filter = Texture.TextureFilter.Nearest;
	}

	public TextureAsset(@Nonnull String fileName) {
		this(fileName, Defaults.filter);
	}

	public TextureAsset(@Nonnull String fileName, @Nonnull Texture.TextureFilter filter) {
		super(fileName, Texture.class, new TextureLoader.TextureParameter() {{
			magFilter = filter;
			minFilter = filter;
		}});
	}

	@Nonnull
	@Delegate
	@Override
	public Texture get() {
		return super.get();
	}
}