package pl.shockah.godwit.gl

import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import groovy.transform.CompileStatic

@CompileStatic
class TextureRegionSprite extends Sprite {
	final TextureRegion region

	TextureRegionSprite(TextureRegion region) {
		this.region = region
	}

	@Override
	protected void internalDraw(SpriteBatch sb, float x, float y) {
		sb.draw(region, x, y)
	}
}