package pl.shockah.godwit.ui;

import javax.annotation.Nonnull;

import pl.shockah.godwit.geom.IVec2;
import pl.shockah.godwit.geom.Shape;
import pl.shockah.godwit.gl.Gfx;

public abstract class UiButton<T extends Shape.Filled> extends UiControl<T> {
	public abstract void updateGestureShape();

	public static abstract class Rectangle extends UiButton<pl.shockah.godwit.geom.Rectangle> {
		@Nonnull
		public Padding padding = new Padding();

		@Nonnull
		public Rectangle setPadding(@Nonnull Padding padding) {
			this.padding = padding;
			return this;
		}

		@Override
		public void updateGestureShape() {
			pl.shockah.godwit.geom.Rectangle bounds = getBounds();
			gestureShape = new pl.shockah.godwit.geom.Rectangle(
					bounds.position.x - padding.left.getPixels(),
					bounds.position.y - padding.top.getPixels(),
					bounds.size.x + padding.left.getPixels() + padding.right.getPixels(),
					bounds.size.y + padding.top.getPixels() + padding.bottom.getPixels()
			);
		}
	}

	public static class NinePatch extends Rectangle {
		@Nonnull
		public final pl.shockah.godwit.gl.NinePatch ninePatch;

		public NinePatch(@Nonnull pl.shockah.godwit.gl.NinePatch ninePatch) {
			this.ninePatch = ninePatch;
		}

		@Override
		public void render(@Nonnull Gfx gfx, @Nonnull IVec2 v) {
			super.render(gfx, v);
			ninePatch.rectangle = getBounds();
			ninePatch.render(gfx, v);
		}
	}

	public static abstract class Circle extends UiButton<pl.shockah.godwit.geom.Circle> {
		public float padding = 0f;

		@Nonnull
		public Circle setPadding(float padding) {
			this.padding = padding;
			return this;
		}

		@Override
		public void updateGestureShape() {
			pl.shockah.godwit.geom.Rectangle bounds = getBounds();
			gestureShape = new pl.shockah.godwit.geom.Circle(
					bounds.getCenter(),
					Math.max(bounds.size.x, bounds.size.y) + padding * 2f
			);
		}
	}
}