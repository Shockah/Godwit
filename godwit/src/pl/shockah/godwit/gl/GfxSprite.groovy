package pl.shockah.godwit.gl

import com.badlogic.gdx.graphics.g2d.Sprite
import groovy.transform.CompileStatic
import pl.shockah.godwit.geom.IVec2
import pl.shockah.godwit.geom.ImmutableVec2
import pl.shockah.godwit.geom.Vec2

import javax.annotation.Nonnull

@CompileStatic
class GfxSprite implements Renderable {
	@Nonnull @Delegate final Sprite sprite
	@Nonnull Vec2 offset = new Vec2()

	GfxSprite(@Nonnull Sprite sprite) {
		this.sprite = sprite
		sprite.setFlip(sprite.flipX, !sprite.flipY)
	}

	@Override
	void onRender(@Nonnull Gfx gfx, float x, float y) {
		float oldX = sprite.x
		float oldY = sprite.y

		sprite.translate(x - offset.x as float, y - offset.y as float)
		gfx.prepareSprites()
		sprite.draw(gfx.spriteBatch)

		sprite.x = oldX
		sprite.y = oldY
	}

	final void center() {
		setOriginCenter()
		offset = (size * 0.5f).mutableCopy
	}

	@Nonnull IVec2 getOrigin() {
		return new ImmutableVec2(sprite.originX, sprite.originY)
	}

	final void setOrigin(@Nonnull IVec2 origin) {
		setOrigin(origin.x, origin.y)
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

	@Nonnull IVec2 getSize() {
		return new ImmutableVec2(width, height)
	}

	void setSize(@Nonnull IVec2 size) {
		setSize(size.x, size.y)
	}

	@Nonnull IVec2 getScaleVector() {
		return new ImmutableVec2(scaleX, scaleY)
	}

	void setScaleVector(@Nonnull IVec2 scale) {
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