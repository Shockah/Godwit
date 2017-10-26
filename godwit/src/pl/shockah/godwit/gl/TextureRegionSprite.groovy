package pl.shockah.godwit.gl

import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import groovy.transform.CompileStatic

import javax.annotation.Nonnull

@CompileStatic
class TextureRegionSprite extends Sprite {
	@Nonnull final TextureRegion region

	TextureRegionSprite(@Nonnull TextureRegion region) {
		this.region = region
	}

	@Override
	protected void internalDraw(@Nonnull SpriteBatch sb, float x, float y) {
		sb.draw(region, x, y)
	}
}