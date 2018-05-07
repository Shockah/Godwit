package pl.shockah.godwit.gl;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import lombok.experimental.Delegate;
import pl.shockah.godwit.Godwit;
import pl.shockah.godwit.fx.Animatable;
import pl.shockah.godwit.fx.Animatables;
import pl.shockah.godwit.geom.IVec2;
import pl.shockah.godwit.geom.MutableVec2;
import pl.shockah.godwit.geom.Vec2;

public class GfxSprite implements Renderable, Animatable<GfxSprite> {
	private interface DelegateExclusions {
		float getRotation();
		void setRotation(float rotation);
		void rotate(float degrees);
		void setFlip(boolean x, boolean y);
		boolean isFlipY();
		void setRegion(Texture texture);
		void setRegion(TextureRegion region);
		void setRegion(int x, int y, int width, int height);
		void setRegion(float u, float v, float u2, float v2);
		void setRegion(TextureRegion region, int x, int y, int width, int height);
	}

	@Delegate(excludes = DelegateExclusions.class)
	@Nonnull public final Sprite sprite;

	@Nonnull public MutableVec2 offset = new MutableVec2();

	@Nullable private Animatables.Properties<GfxSprite> animatableProperties;

	public GfxSprite(@Nonnull Sprite sprite) {
		this.sprite = sprite;
		fixFlip();
	}

	private void fixFlip() {
		if (Godwit.getInstance().yPointingDown)
			sprite.setFlip(sprite.isFlipX(), !sprite.isFlipY());
	}

	@Override
	public void render(@Nonnull Gfx gfx, @Nonnull IVec2 v) {
		if (Vec2.zero.equals(v) && Vec2.zero.equals(offset)) {
			gfx.prepareSprites();
			sprite.draw(gfx.getSpriteBatch());
		} else {
			float oldX = sprite.getX();
			float oldY = sprite.getY();

			sprite.translate(v.x() - offset.x, v.y() - offset.y);
			gfx.prepareSprites();
			sprite.draw(gfx.getSpriteBatch());

			sprite.setPosition(oldX, oldY);
		}
	}

	public final void center() {
		setOriginCenter();
		offset = getSize().multiply(0.5f).getMutableCopy();
	}

	public @Nonnull IVec2 getOrigin() {
		return new Vec2(sprite.getOriginX(), sprite.getOriginY());
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
		sprite.setFlip(x, Godwit.getInstance().yPointingDown != y);
	}

	public boolean isFlipY() {
		return Godwit.getInstance().yPointingDown != sprite.isFlipY();
	}

	public void setRegion(int x, int y, int width, int height) {
		sprite.setRegion(x, y, width, height);
		fixFlip();
	}

	public void setRegion(float u, float v, float u2, float v2) {
		sprite.setRegion(u, v, u2, v2);
		fixFlip();
	}

	public void setRegion(TextureRegion region) {
		sprite.setRegion(region);
		fixFlip();
	}

	public void setRegion(Texture region) {
		sprite.setRegion(region);
		fixFlip();
	}

	public void setRegion(TextureRegion region, int x, int y, int width, int height) {
		sprite.setRegion(region, x, y, width, height);
		fixFlip();
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
		return new Vec2(sprite.getWidth(), sprite.getHeight());
	}

	public void setSize(@Nonnull IVec2 size) {
		setSize(size.x(), size.y());
	}

	@Nonnull public IVec2 getRegionSize() {
		return new Vec2(sprite.getRegionWidth(), sprite.getRegionHeight());
	}

	public float getScaledWidth() {
		return getWidth() * getScaleX();
	}

	public float getScaledHeight() {
		return getHeight() * getScaleY();
	}

	@Nonnull public IVec2 getScaledSize() {
		return getSize().multiply(getScaleVector());
	}

	@Nonnull public IVec2 getScaleVector() {
		return new Vec2(sprite.getScaleX(), sprite.getScaleY());
	}

	public void setScaleVector(@Nonnull IVec2 scale) {
		setScale(scale.x(), scale.y());
	}

	@Nonnull public IVec2 getPosition() {
		return new Vec2(sprite.getX(), sprite.getY());
	}

	public void setPosition(@Nonnull IVec2 position) {
		setPosition(position.x(), position.y());
	}

	@SuppressWarnings("unchecked")
	@Override
	@Nonnull public Animatables.Properties<GfxSprite> getAnimatableProperties() {
		if (animatableProperties == null)
			animatableProperties = Animatables.getAnimatableProperties(this);
		return animatableProperties;
	}

	@Nonnull public Entity asEntity() {
		return new Entity(this);
	}

	public static class Entity extends pl.shockah.godwit.Entity {
		@Nonnull public final GfxSprite sprite;

		public Entity(@Nonnull Sprite sprite) {
			this(new GfxSprite(sprite));
		}

		public Entity(@Nonnull GfxSprite sprite) {
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
}