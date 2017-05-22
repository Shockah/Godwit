package pl.shockah.godwit.gl

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import groovy.transform.CompileStatic

@CompileStatic
class TextureSprite extends Sprite {
	final Texture texture

	TextureSprite(Texture texture) {
		this.texture = texture
	}

	@Override
	protected void internalDraw(SpriteBatch sb, float x, float y) {
		sb.draw(texture, x, y)
	}
}