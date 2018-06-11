package pl.shockah.godwit.ui;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import lombok.Getter;
import pl.shockah.godwit.geom.Shape;
import pl.shockah.godwit.gesture.GestureRecognizer;
import pl.shockah.godwit.gesture.TapGestureRecognizer;

public abstract class AbstractUiButton<S extends Shape.Filled> extends UiControl<S> {
	@Nullable
	public final Listener listener;

	protected boolean isPressed = false;

	@Nullable
	@Getter
	public S gestureShape;

	public AbstractUiButton(@Nullable Listener listener) {
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

	@Override
	public void updateSelf() {
		super.updateSelf();
		updateGestureShape();
	}

	public abstract void updateGestureShape();

	public interface Listener {
		void onButtonPressed(@Nonnull AbstractUiButton<? extends Shape.Filled> button);
	}
}