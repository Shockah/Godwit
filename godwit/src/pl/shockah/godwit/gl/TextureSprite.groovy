package pl.shockah.godwit.gl

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import groovy.transform.CompileStatic

import javax.annotation.Nonnull

@CompileStatic
class TextureSprite extends Sprite {
	@Nonnull final Texture texture

	TextureSprite(@Nonnull Texture texture) {
		this.texture = texture
	}

	@Override
	protected void internalDraw(@Nonnull SpriteBatch sb, float x, float y) {
		sb.draw(texture, x, y)
	}
}