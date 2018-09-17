package pl.shockah.godwit.entity;

import com.badlogic.gdx.assets.AssetLoaderParameters;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import pl.shockah.godwit.asset.SingleAsset;
import pl.shockah.godwit.geom.IVec2;
import pl.shockah.godwit.gl.Gfx;
import pl.shockah.godwit.gl.GfxFont;

public class GfxFontEntity extends Entity {
	@Nonnull
	public final GfxFont font;

	public GfxFontEntity(@Nonnull SingleAsset<BitmapFont> asset) {
		this(new GfxFont(asset));
	}

	public GfxFontEntity(@Nonnull BitmapFont font) {
		this(new GfxFont(font));
	}

	public GfxFontEntity(@Nonnull BitmapFont font, @Nullable AssetLoaderParameters<BitmapFont> parameters) {
		this(new GfxFont(font, parameters));
	}

	public GfxFontEntity(@Nonnull GfxFont font) {
		this.font = font;
	}

	@Override
	public void render(@Nonnull Gfx gfx, @Nonnull IVec2 v) {
		font.render(gfx, v, getCameraGroup().getBoundingBox());
	}
}