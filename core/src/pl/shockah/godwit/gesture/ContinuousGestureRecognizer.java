package pl.shockah.godwit.gesture;

import javax.annotation.Nonnull;

import pl.shockah.godwit.Godwit;

public abstract class ContinuousGestureRecognizer extends GestureRecognizer {
	protected ContinuousGestureRecognizer(@Nonnull GestureHandler handler) {
		super(handler);
	}

	protected void onTouchUsedByContinuousRecognizer(@Nonnull Touch touch) {
	}

	@Override
	protected void setState(@Nonnull State state) {
		super.setState(state);

		if (state == State.Detecting || state == State.Began || state == State.Changed) {
			Godwit.getInstance().inputManager.gestureManager.currentContinuousRecognizers.add(this);
			if (state == State.Began) {
				for (GestureRecognizer recognizer : Godwit.getInstance().inputManager.gestureManager.recognizers) {
					if (!(recognizer instanceof ContinuousGestureRecognizer))
						recognizer.setState(State.Failed);
				}
			}
		} else if (state == State.Ended || state == State.Cancelled || state == State.Failed) {
			Godwit.getInstance().inputManager.gestureManager.currentContinuousRecognizers.remove(this);
		}
	}
}