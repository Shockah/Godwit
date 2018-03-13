package pl.shockah.godwit.asset;

import com.badlogic.gdx.graphics.g2d.BitmapFont;

import javax.annotation.Nonnull;

import lombok.experimental.Delegate;

public class FreeTypeFontAsset extends SingleAsset<BitmapFont> {
	public FreeTypeFontAsset(@Nonnull String fileName, @Nonnull FreeTypeFontLoader.FreeTypeFontParameter parameter) {
		super(fileName, BitmapFont.class, parameter);
	}

	@Delegate
	@Override
	public BitmapFont get() {
		return super.get();
	}
}