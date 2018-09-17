package pl.shockah.godwit.entity;

import com.badlogic.gdx.graphics.Color;

import javax.annotation.Nonnull;

import pl.shockah.godwit.geom.IVec2;
import pl.shockah.godwit.geom.Shape;
import pl.shockah.godwit.gl.Gfx;

public abstract class ShapeEntity<S extends Shape> extends Entity {
	@Nonnull
	public final S shape;

	@Nonnull
	public Color color = Color.WHITE;

	public ShapeEntity(@Nonnull S shape) {
		this.shape = shape;
	}

	@Override
	public void render(@Nonnull Gfx gfx, @Nonnull IVec2 v) {
		gfx.setColor(color);
		drawShape(gfx, v);
		gfx.setColor(Color.WHITE);
	}

	protected abstract void drawShape(@Nonnull Gfx gfx, @Nonnull IVec2 v);

	@Nonnull
	public ShapeEntity<S> withColor(@Nonnull Color color) {
		this.color = color;
		return this;
	}

	public static class Filled<S extends Shape.Filled> extends ShapeEntity<S> {
		public Filled(@Nonnull S shape) {
			super(shape);
		}

		@Override
		protected void drawShape(@Nonnull Gfx gfx, @Nonnull IVec2 v) {
			gfx.drawFilled(shape, v);
		}
	}

	public static class Outline<S extends Shape.Outline> extends ShapeEntity<S> {
		public Outline(@Nonnull S shape) {
			super(shape);
		}

		@Override
		protected void drawShape(@Nonnull Gfx gfx, @Nonnull IVec2 v) {
			gfx.drawOutline(shape, v);
		}
	}
}