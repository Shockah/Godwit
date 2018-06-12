package pl.shockah.godwit.ui;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import lombok.Getter;
import pl.shockah.godwit.geom.IVec2;
import pl.shockah.godwit.geom.Shape;
import pl.shockah.godwit.gesture.GestureRecognizer;
import pl.shockah.godwit.gesture.TapGestureRecognizer;
import pl.shockah.godwit.gl.Gfx;

public abstract class UiToggle<S extends Shape.Filled, T> extends UiControl<S> {
	@Nullable
	public final Listener<T> listener;

	@Nonnull
	public T value;

	protected boolean isPressed = false;

	@Nullable
	@Getter
	public S gestureShape;

	public UiToggle(@Nullable Listener<T> listener, @Nonnull T value) {
		this.listener = listener;
		this.value = value;

		TapGestureRecognizer tapGesture = new TapGestureRecognizer(this, recognizer -> {
			switchValue();
			if (listener != null)
				listener.onToggleChanged(this, value);
		});
		tapGesture.stateListeners.add((recognizer, state) -> {
			isPressed = state == GestureRecognizer.State.Began;
		});
		gestureRecognizers.add(tapGesture);
	}

	public abstract void updateGestureShape();

	public abstract void switchValue();

	public static abstract class Binary<S extends Shape.Filled> extends UiToggle<S, Boolean> {
		public Binary(@Nullable Listener<Boolean> listener) {
			this(listener, false);
		}

		public Binary(@Nullable Listener<Boolean> listener, boolean value) {
			super(listener, value);
		}

		@Override
		public void switchValue() {
			value = !value;
		}

		public static abstract class Rectangle extends Binary<pl.shockah.godwit.geom.Rectangle> {
			@Nonnull
			public Padding padding = new Padding();

			public Rectangle(@Nullable Listener<Boolean> listener) {
				super(listener);
			}

			public Rectangle(@Nullable Listener<Boolean> listener, boolean value) {
				super(listener, value);
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
			public final pl.shockah.godwit.gl.NinePatch normalUnchecked;

			@Nonnull
			public final pl.shockah.godwit.gl.NinePatch pressedUnchecked;

			@Nonnull
			public final pl.shockah.godwit.gl.NinePatch normalChecked;

			@Nonnull
			public final pl.shockah.godwit.gl.NinePatch pressedChecked;

			public NinePatch(
					@Nonnull pl.shockah.godwit.gl.NinePatch normalUnchecked,
					@Nonnull pl.shockah.godwit.gl.NinePatch pressedUnchecked,
					@Nonnull pl.shockah.godwit.gl.NinePatch normalChecked,
					@Nonnull pl.shockah.godwit.gl.NinePatch pressedChecked,
					@Nullable Listener<Boolean> listener
			) {
				this(normalUnchecked, pressedUnchecked, normalChecked, pressedChecked, listener, false);
			}

			public NinePatch(
					@Nonnull pl.shockah.godwit.gl.NinePatch normalUnchecked,
					@Nonnull pl.shockah.godwit.gl.NinePatch pressedUnchecked,
					@Nonnull pl.shockah.godwit.gl.NinePatch normalChecked,
					@Nonnull pl.shockah.godwit.gl.NinePatch pressedChecked,
					@Nullable Listener<Boolean> listener,
					boolean value
			) {
				super(listener, value);
				this.normalUnchecked = normalUnchecked;
				this.pressedUnchecked = pressedUnchecked;
				this.normalChecked = normalChecked;
				this.pressedChecked = pressedChecked;
			}

			@Override
			public void render(@Nonnull Gfx gfx, @Nonnull IVec2 v) {
				super.render(gfx, v);

				pl.shockah.godwit.gl.NinePatch ninePatch = isPressed ? (value ? pressedChecked : pressedUnchecked) : (value ? normalChecked : normalUnchecked);
				ninePatch.rectangle = getBounds();
				ninePatch.render(gfx);
			}

			@Nonnull
			@Override
			public NinePatch setPadding(@Nonnull Padding padding) {
				super.setPadding(padding);
				return this;
			}
		}
	}

	public interface Listener<T> {
		void onToggleChanged(@Nonnull UiToggle<? extends Shape.Filled, T> button, @Nonnull T value);
	}
}