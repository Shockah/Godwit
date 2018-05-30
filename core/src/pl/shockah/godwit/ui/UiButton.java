package pl.shockah.godwit.ui;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import pl.shockah.godwit.geom.IVec2;
import pl.shockah.godwit.geom.Shape;
import pl.shockah.godwit.gesture.GestureRecognizer;
import pl.shockah.godwit.gesture.TapGestureRecognizer;
import pl.shockah.godwit.gl.Gfx;

public abstract class UiButton<S extends Shape.Filled> extends UiControl<S> {
	@Nullable
	public final Listener listener;

	protected boolean isPressed = false;

	public UiButton(@Nullable Listener listener) {
		this.listener = listener;

		TapGestureRecognizer tapGesture = new TapGestureRecognizer(this, recognizer -> {
			if (listener != null)
				listener.onButtonPressed(this);
		});
		tapGesture.stateListeners.add((recognizer, state) -> {
			isPressed = state == GestureRecognizer.State.Began;
		});
		gestureRecognizers.add(tapGesture);
	}

	public abstract void updateGestureShape();

	public static abstract class Rectangle extends UiButton<pl.shockah.godwit.geom.Rectangle> {
		@Nonnull
		public Padding padding = new Padding();

		public Rectangle(@Nullable Listener listener) {
			super(listener);
		}

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
		public final pl.shockah.godwit.gl.NinePatch normal;

		@Nonnull
		public final pl.shockah.godwit.gl.NinePatch pressed;

		public NinePatch(@Nonnull pl.shockah.godwit.gl.NinePatch normal, @Nonnull pl.shockah.godwit.gl.NinePatch pressed, @Nonnull Listener listener) {
			super(listener);
			this.normal = normal;
			this.pressed = pressed;
		}

		@Override
		public void render(@Nonnull Gfx gfx, @Nonnull IVec2 v) {
			super.render(gfx, v);

			pl.shockah.godwit.gl.NinePatch ninePatch = isPressed ? pressed : normal;
			ninePatch.rectangle = getBounds();
			ninePatch.render(gfx, v);
		}

		@Nonnull
		@Override
		public NinePatch setPadding(@Nonnull Padding padding) {
			super.setPadding(padding);
			return this;
		}
	}

	public static abstract class Circle extends UiButton<pl.shockah.godwit.geom.Circle> {
		public float padding = 0f;

		public Circle(@Nullable Listener listener) {
			super(listener);
		}

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

	public interface Listener {
		void onButtonPressed(@Nonnull UiButton<? extends Shape.Filled> button);
	}
}