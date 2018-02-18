package pl.shockah.godwit.gl;

import com.badlogic.gdx.graphics.g2d.Sprite;

import javax.annotation.Nonnull;

import lombok.experimental.Delegate;
import pl.shockah.godwit.fx.Animatable;
import pl.shockah.godwit.geom.IVec2;
import pl.shockah.godwit.geom.ImmutableVec2;
import pl.shockah.godwit.geom.Vec2;

public class GfxSprite implements Renderable, Animatable<GfxSprite> {
	private interface DelegateExclusions {
		float getRotation();
		void setRotation(float rotation);
		void rotate(float degrees);
		void setFlip(boolean x, boolean y);
		boolean isFlipY();
	}

	@Delegate(excludes = DelegateExclusions.class)
	@Nonnull public final Sprite sprite;

	@Nonnull public Vec2 offset = new Vec2();

	public GfxSprite(@Nonnull Sprite sprite) {
		this.sprite = sprite;
		sprite.setFlip(sprite.isFlipX(), !sprite.isFlipY());
	}

	@Override
	public void render(@Nonnull Gfx gfx, @Nonnull IVec2 v) {
		float oldX = sprite.getX();
		float oldY = sprite.getY();

		sprite.translate(v.x() - offset.x, v.y() - offset.y);
		gfx.prepareSprites();
		sprite.draw(gfx.getSpriteBatch());

		sprite.setPosition(oldX, oldY);
	}

	public final void center() {
		setOriginCenter();
		offset = (getSize() * 0.5f).getMutableCopy();
	}

	public @Nonnull IVec2 getOrigin() {
		return new ImmutableVec2(sprite.getOriginX(), sprite.getOriginY());
	}

	public final void setOrigin(@Nonnull IVec2 origin) {
		setOrigin(origin.x(), origin.y());
	}

	public float getRotation() {
		return -sprite.getRotation();
	}

	public void setRotation(float rotation) {
		sprite.setRotation(-rotation);
	}

	public void rotate(float degrees) {
		sprite.rotate(-degrees);
	}

	public void flipX() {
		sprite.setFlip(!sprite.isFlipX(), sprite.isFlipY());
	}

	public void flipY() {
		sprite.setFlip(sprite.isFlipX(), !sprite.isFlipY());
	}

	public void setFlip(boolean x, boolean y) {
		sprite.setFlip(x, !y);
	}

	// by default it's the wrong way around
	public boolean isFlipY() {
		return !sprite.isFlipY();
	}

	public void setScaleX(float scaleX) {
		sprite.setScale(scaleX, sprite.getScaleY());
	}

	public void setScaleY(float scaleY) {
		sprite.setScale(sprite.getScaleX(), scaleY);
	}

	public float getScale() {
		if (sprite.getScaleX() != sprite.getScaleY())
			throw new IllegalStateException("Scale X and Y are different.");
		return sprite.getScaleX();
	}

	@Nonnull public IVec2 getSize() {
		return new ImmutableVec2(sprite.getWidth(), sprite.getHeight());
	}

	public void setSize(@Nonnull IVec2 size) {
		setSize(size.x(), size.y());
	}

	@Nonnull public IVec2 getScaleVector() {
		return new ImmutableVec2(sprite.getScaleX(), sprite.getScaleY());
	}

	public void setScaleVector(@Nonnull IVec2 scale) {
		setScale(scale.x(), scale.y());
	}

	@Nonnull public IVec2 getPosition() {
		return new ImmutableVec2(sprite.getX(), sprite.getY());
	}

	public void setPosition(@Nonnull IVec2 position) {
		setPosition(position.x(), position.y());
	}

	@Nonnull public Entity asEntity() {
		return new Entity(this);
	}

	public static class Entity extends pl.shockah.godwit.Entity {
		// can't make it a @Delegate, as it produces a weird compile error
		// looks a bit like this issue: https://issues.apache.org/jira/browse/GROOVY-7118
		@Nonnull public final GfxSprite sprite;

		public Entity(@Nonnull Sprite sprite) {
			this(new GfxSprite(sprite));
		}

		public Entity(@Nonnull GfxSprite sprite) {
			this.sprite = sprite;
		}

		@Override
		public void onRender(@Nonnull Gfx gfx, @Nonnull IVec2 v) {
			super.onRender(gfx, v);
			gfx.draw(sprite, v);
		}

		@Override
		public void updateFx() {
			super.updateFx();
			sprite.updateFx();
		}
	}
}