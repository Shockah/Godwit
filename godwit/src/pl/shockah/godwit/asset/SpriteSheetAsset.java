package pl.shockah.godwit.asset;

import javax.annotation.Nonnull;

import lombok.experimental.Delegate;
import pl.shockah.godwit.gl.SpriteSheet;

public class SpriteSheetAsset extends Asset<SpriteSheet> {
	public SpriteSheetAsset(@Nonnull String fileName, @Nonnull Class<SpriteSheet> clazz) {
		super(fileName, clazz);
	}

	@Delegate
	@Override
	public SpriteSheet get() {
		return super.get();
	}
}
