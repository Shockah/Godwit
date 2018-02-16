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

	void setFlip(boolean x, boolean y) {
		sprite.setFlip(x, !y)
	}

	// by default it's the wrong way around
	boolean isFlipY() {
		return !sprite.flipY
	}

	void setScaleX(float scaleX) {
		sprite.setScale(scaleX, sprite.scaleX)
	}

	void setScaleY(float scaleY) {
		sprite.setScale(sprite.scaleX, scaleY)
	}

	float getScale() {
		assert sprite.scaleX == sprite.scaleY
		return sprite.scaleX
	}

	@Nonnull Vec2 getSize() {
		return new Vec2(width, height)
	}

	void setSize(@Nonnull Vec2 size) {
		setSize(size.x, size.y)
	}

	@Nonnull Vec2 getScaleVector() {
		return new Vec2(scaleX, scaleY)
	}

	void setScaleVector(@Nonnull Vec2 scale) {
		setScale(scale.x, scale.y)
	}

	@Nonnull Entity asEntity() {
		return new Entity(this)
	}

	static class Entity extends pl.shockah.godwit.Entity {
		@Nonnull @Delegate(excludes = "asEntity") final GfxSprite sprite

		Entity(@Nonnull Sprite sprite) {
			this(new GfxSprite(sprite))
		}

		Entity(@Nonnull GfxSprite sprite) {
			this.sprite = sprite
		}

		@Override
		void onRender(@Nonnull Gfx gfx, float x, float y) {
			super.onRender(gfx, x, y)
			gfx.draw(sprite, x, y)
		}
	}
}