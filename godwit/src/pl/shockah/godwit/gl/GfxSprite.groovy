package pl.shockah.godwit.gl

import com.badlogic.gdx.graphics.g2d.Sprite
import groovy.transform.CompileStatic
import pl.shockah.godwit.geom.Vec2

import javax.annotation.Nonnull

@CompileStatic
class GfxSprite implements Renderable {
	@Nonnull @Delegate final Sprite sprite

	GfxSprite(@Nonnull Sprite sprite) {
		this.sprite = sprite
		sprite.setFlip(sprite.flipX, !sprite.flipY)
	}

	@Override
	void onRender(@Nonnull Gfx gfx, float x, float y) {
		float oldX = sprite.x
		float oldY = sprite.y

		sprite.translate(x, y)

		gfx.prepareSprites()
		sprite.draw(gfx.spriteBatch)

		sprite.x = oldX
		sprite.y = oldY
	}

	final void setOriginCenter() {
		setOrigin(sprite.width / 2f as float, sprite.height / 2f as float)
	}

	final void setOrigin(@Nonnull Vec2 origin) {
		setOrigin(origin.x, origin.y)
	}

	void setOrigin(float x, float y) {
		sprite.setOrigin(x, y)
		sprite.x = -x
		sprite.y = -y
	}

	@Nonnull Vec2 getOrigin() {
		return new Vec2(sprite.originX, sprite.originY)
	}

	float getRotation() {
		return -sprite.rotation
	}

	void setRotation(float rotation) {
		sprite.rotation = -rotation
	}

	void rotate(float degrees) {
		sprite.rotate(-degrees)
	}

	void flipX() {
		sprite.setFlip(!sprite.flipX, sprite.flipY)
	}

	void flipY() {
		sprite.setFlip(sprite.flipX, !sprite.flipY)
	}

	// by default it's the wrong way around
	boolean isFlipY() {
		return !sprite.flipY
	}
}