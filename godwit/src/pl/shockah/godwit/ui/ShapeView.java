package pl.shockah.godwit.ui;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import pl.shockah.godwit.geom.IVec2;
import pl.shockah.godwit.geom.ImmutableVec2;
import pl.shockah.godwit.geom.Shape;
import pl.shockah.godwit.gl.Gfx;

public abstract class ShapeView<T extends Shape> extends View {
	@Nullable public T shape;

	public ShapeView(@Nullable T shape) {
		this.shape = shape;
	}

	@Override
	@Nonnull public IVec2 getIntrinsicSize(@Nonnull IVec2 availableSize) {
		return shape != null ? shape.getBoundingBox().size : ImmutableVec2.zero;
	}

	public static class Filled<T extends Shape & Shape.Filled> extends ShapeView<T> {
		public Filled(@Nullable T shape) {
			super(shape);
		}

		@Override
		public void render(@Nonnull Gfx gfx, @Nonnull IVec2 v) {
			super.render(gfx, v);
			if (shape != null)
				shape.drawFilled(gfx, -shape.getBoundingBox().position + v);
		}
	}

	public static class Outline<T extends Shape & Shape.Outline> extends ShapeView<T> {
		public Outline(@Nullable T shape) {
			super(shape);
		}

		@Override
		public void render(@Nonnull Gfx gfx, @Nonnull IVec2 v) {
			super.render(gfx, v);
			if (shape != null)
				shape.drawOutline(gfx, -shape.getBoundingBox().position + v);
		}
	}
}