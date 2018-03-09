package pl.shockah.godwit.geom;

import com.badlogic.gdx.graphics.Color;

import javax.annotation.Nonnull;

import pl.shockah.godwit.geom.polygon.Polygonable;
import pl.shockah.godwit.gl.Gfx;

public abstract class Shape {
	@Nonnull public abstract Shape copy();

	@Nonnull public abstract Rectangle getBoundingBox();

	public final void translate(float x, float y) {
		translate(new Vec2(x, y));
	}

	public abstract void translate(@Nonnull IVec2 v);

	public abstract void mirror(boolean horizontally, boolean vertically);

	public abstract void scale(float scale);

	public final boolean collides(@Nonnull Shape shape) {
		return collides(shape, false);
	}

	protected boolean collides(@Nonnull Shape shape, boolean secondTry) {
		if (secondTry) {
			if (this instanceof Polygonable && shape instanceof Polygonable)
				return ((Polygonable)this).asPolygon().collides((Polygonable)shape);
			else
				throw new UnsupportedOperationException(String.format("%s --><-- %s collision isn't implemented.", getClass().getSimpleName(), shape.getClass().getSimpleName()));
		} else {
			return shape.collides(this, true);
		}
	}

	private static abstract class Entity<S extends Shape> extends pl.shockah.godwit.Entity {
		@Nonnull public final S shape;
		@Nonnull public Color color = Color.WHITE;

		public Entity(@Nonnull S shape) {
			this.shape = shape;
		}

		@Override
		public void render(@Nonnull Gfx gfx, @Nonnull IVec2 v) {
			gfx.setColor(color);
			drawShape(gfx, v);
			gfx.setColor(Color.WHITE);
		}

		protected abstract void drawShape(@Nonnull Gfx gfx, @Nonnull IVec2 v);

		@Nonnull public Entity<S> withColor(@Nonnull Color color) {
			this.color = color;
			return this;
		}
	}

	public interface Filled {
		void drawFilled(@Nonnull Gfx gfx, @Nonnull IVec2 v);

		default void drawFilled(@Nonnull Gfx gfx, float x, float y) {
			drawFilled(gfx, new Vec2(x, y));
		}

		default void drawFilled(@Nonnull Gfx gfx) {
			drawFilled(gfx, Vec2.zero);
		}

		boolean contains(@Nonnull IVec2 v);

		default boolean contains(float x, float y) {
			return contains(new Vec2(x, y));
		}

		@SuppressWarnings("unchecked")
		@Nonnull default Entity asFilledEntity() {
			return new Entity((Shape & Filled)this);
		}

		class Entity<S extends Shape & Filled> extends Shape.Entity<S> {
			public Entity(@Nonnull S shape) {
				super(shape);
			}

			@Override
			protected void drawShape(@Nonnull Gfx gfx, @Nonnull IVec2 v) {
				gfx.drawFilled(shape, v);
			}
		}
	}

	public interface Outline {
		void drawOutline(@Nonnull Gfx gfx, @Nonnull IVec2 v);

		default void drawOutline(@Nonnull Gfx gfx, float x, float y) {
			drawOutline(gfx, new Vec2(x, y));
		}

		default void drawOutline(@Nonnull Gfx gfx) {
			drawOutline(gfx, Vec2.zero);
		}

		@SuppressWarnings("unchecked")
		@Nonnull default Entity asOutlineEntity() {
			return new Entity((Shape & Outline)this);
		}

		class Entity<S extends Shape & Outline> extends Shape.Entity<S> {
			public Entity(@Nonnull S shape) {
				super(shape);
			}

			@Override
			protected void drawShape(@Nonnull Gfx gfx, @Nonnull IVec2 v) {
				gfx.drawOutline(shape, v);
			}
		}
	}
}