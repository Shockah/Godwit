package pl.shockah.godwit.geom;

import javax.annotation.Nonnull;

import pl.shockah.godwit.gl.Gfx;

public interface Shape {
	@Nonnull
	Shape copy();

	@Nonnull
	Rectangle getBoundingBox();

	void translate(float x, float y);

	void translate(@Nonnull IVec2 v);

	void mirror(boolean horizontally, boolean vertically);

	void scale(float scale);

	boolean collides(@Nonnull Shape shape);

	boolean collides(@Nonnull Shape shape, boolean secondTry);

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
	}

	interface Outline extends Shape {
		void drawOutline(@Nonnull Gfx gfx, @Nonnull IVec2 v);

		default void drawOutline(@Nonnull Gfx gfx, float x, float y) {
			drawOutline(gfx, new Vec2(x, y));
		}

		default void drawOutline(@Nonnull Gfx gfx) {
			drawOutline(gfx, Vec2.zero);
		}
	}
}