package pl.shockah.godwit.geom;

import com.badlogic.gdx.graphics.Color;

import javax.annotation.Nonnull;

import pl.shockah.godwit.gl.Gfx;

public interface Shape {
	@Nonnull Shape copy();

	@Nonnull Rectangle getBoundingBox();

	void translate(float x, float y);

	void translate(@Nonnull IVec2 v);

	void mirror(boolean horizontally, boolean vertically);

	void scale(float scale);

	boolean collides(@Nonnull Shape shape);

	boolean collides(@Nonnull Shape shape, boolean secondTry);

	abstract class Entity<S extends Shape> extends pl.shockah.godwit.Entity {
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

	interface Filled extends Shape {
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
			return new Entity(this);
		}

		class Entity<S extends Filled> extends Shape.Entity<S> {
			public Entity(@Nonnull S shape) {
				super(shape);
			}

			@Override
			protected void drawShape(@Nonnull Gfx gfx, @Nonnull IVec2 v) {
				gfx.drawFilled(shape, v);
			}
		}
	}

	interface Outline extends Shape {
		void drawOutline(@Nonnull Gfx gfx, @Nonnull IVec2 v);

		default void drawOutline(@Nonnull Gfx gfx, float x, float y) {
			drawOutline(gfx, new Vec2(x, y));
		}

		default void drawOutline(@Nonnull Gfx gfx) {
			drawOutline(gfx, Vec2.zero);
		}

		@SuppressWarnings("unchecked")
		@Nonnull default Entity asOutlineEntity() {
			return new Entity(this);
		}

		class Entity<S extends Outline> extends Shape.Entity<S> {
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