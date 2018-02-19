package pl.shockah.godwit.geom;

import javax.annotation.Nonnull;

import pl.shockah.godwit.gl.Gfx;

public abstract class Shape {
	@Nonnull public abstract Shape copy();

	@Nonnull public abstract Rectangle getBoundingBox();

	public final void translate(float x, float y) {
		translate(new ImmutableVec2(x, y));
	}

	public abstract void translate(@Nonnull IVec2 v);

	public final boolean collides(@Nonnull Shape shape) {
		return collides(shape, false);
	}

	protected boolean collides(@Nonnull Shape shape, boolean secondTry) {
		if (secondTry)
			throw new UnsupportedOperationException();
		else
			return shape.collides(this, true);
	}

	public interface Filled {
		void drawFilled(@Nonnull Gfx gfx, @Nonnull IVec2 v);

		default void drawFilled(@Nonnull Gfx gfx, float x, float y) {
			drawFilled(gfx, new ImmutableVec2(x, y));
		}

		default void drawFilled(@Nonnull Gfx gfx) {
			drawFilled(gfx, new ImmutableVec2());
		}

		boolean contains(@Nonnull IVec2 v);

		default boolean contains(float x, float y) {
			return contains(new ImmutableVec2(x, y));
		}

		// TODO: reimplement
		/*@Nonnull Entity asFilledEntity() {
			return new Entity(this)
		}

		static class Entity extends pl.shockah.oldgodwit.Entity {
			@Nonnull @Delegate(interfaces = false, excludes = "asFilledEntity") final Filled shape

			Entity(@Nonnull Filled shape) {
				this.shape = shape
			}

			@Override
			void onRender(@Nonnull Gfx gfx, float x, float y) {
				super.onRender(gfx, x, y)
				gfx.drawFilled(shape, x, y)
			}
		}*/
	}

	public interface Outline {
		void drawOutline(@Nonnull Gfx gfx, @Nonnull IVec2 v);

		default void drawOutline(@Nonnull Gfx gfx, float x, float y) {
			drawOutline(gfx, new ImmutableVec2(x, y));
		}

		default void drawOutline(@Nonnull Gfx gfx) {
			drawOutline(gfx, new ImmutableVec2());
		}

		// TODO: reimplement
		/*@Nonnull Entity asOutlineEntity() {
			return new Entity(this)
		}

		static class Entity extends pl.shockah.oldgodwit.Entity {
			@Nonnull @Delegate(interfaces = false, excludes = "asOutlineEntity") final Outline shape

			Entity(@Nonnull Outline shape) {
				this.shape = shape
			}

			@Override
			void onRender(@Nonnull Gfx gfx, float x, float y) {
				super.onRender(gfx, x, y)
				gfx.drawOutline(shape, x, y)
			}
		}*/
	}
}