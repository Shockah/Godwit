package pl.shockah.godwit.entity;

import com.badlogic.gdx.graphics.g2d.Sprite;

import javax.annotation.Nonnull;

import pl.shockah.godwit.geom.IVec2;
import pl.shockah.godwit.gl.Gfx;
import pl.shockah.godwit.gl.GfxSprite;

public class GfxSpriteEntity extends Entity {
	@Nonnull
	public final GfxSprite sprite;

	public GfxSpriteEntity(@Nonnull Sprite sprite) {
		this(new GfxSprite(sprite));
	}

	public GfxSpriteEntity(@Nonnull GfxSprite sprite) {
		this.sprite = sprite;
	}

	@Override
	public void render(@Nonnull Gfx gfx, @Nonnull IVec2 v) {
		if (!getCameraGroup().getBoundingBox().collides(
				v.x() - sprite.offset.x * sprite.getScaleX() + sprite.getX(),
				v.y() - sprite.offset.y * sprite.getScaleY() + sprite.getY(),
				sprite.getScaledWidth(),
				sprite.getScaledHeight()
		))
			return;

		gfx.draw(sprite, v);
	}

	@Override
	public void updateFx() {
		super.updateFx();
		sprite.updateFx();
	}
}