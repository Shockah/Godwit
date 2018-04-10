package pl.shockah.godwit.gesture;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Timer;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import pl.shockah.godwit.Godwit;
import pl.shockah.godwit.geom.Circle;
import pl.shockah.godwit.geom.IVec2;
import pl.shockah.godwit.geom.Vec2;

public class LongPressGestureRecognizer extends GestureRecognizer {
	public static float getStationaryRadius() {
		IVec2 ppi = Godwit.getInstance().getPpi();
		float average = (ppi.x() + ppi.y()) * 0.5f;
		return average * 0.1f;
	}

	public final float delay;
	@Nonnull public final Delegate delegate;

	@Nullable public Touch touch = null;
	private Timer.Task task = null;
	private boolean shouldEnd = false;

	public LongPressGestureRecognizer(@Nonnull GestureHandler group, float delay, @Nonnull Delegate delegate) {
		super(group);
		this.delay = delay;
		this.delegate = delegate;
	}

	@Override
	protected void setState(@Nonnull State state) {
		super.setState(state);

		if (state == State.Failed || state == State.Ended || state == State.Cancelled) {
			touch = null;
			shouldEnd = false;
			if (task != null) {
				task.cancel();
				task = null;
			}
		}
	}

	@Override
	protected boolean onRequiredFailFailed(@Nonnull GestureRecognizer recognizer) {
		if (shouldEnd) {
			setState(State.Ended);
			delegate.onLongPress(LongPressGestureRecognizer.this);
			return true;
		} else {
			return false;
		}
	}

	@Override
	public boolean handleTouchDown(@Nonnull Touch touch, @Nonnull Vec2 point) {
		if (getState() == State.Possible) {
			setState(State.Began);
			this.touch = touch;
			task = Timer.schedule(new Timer.Task() {
				@Override
				public void run() {
					Gdx.app.postRunnable(() -> {
						task = null;
						if (requiredRecognizersFailed()) {
							setState(State.Ended);
							delegate.onLongPress(LongPressGestureRecognizer.this);
						} else {
							shouldEnd = true;
						}
					});
				}
			}, delay);
			return true;
		}

		return false;
	}

	@Override
	public boolean handleTouchDragged(@Nonnull Touch touch, @Nonnull Vec2 point) {
		if (isInProgress()) {
			if (!new Circle(touch.points.get(0).position, getStationaryRadius()).contains(point)) {
				setState(State.Failed);
				return true;
			} else {
				setState(State.Changed);
				return true;
			}
		}

		return false;
	}

	@Override
	public boolean handleTouchUp(@Nonnull Touch touch, @Nonnull Vec2 point) {
		if (isInProgress() && this.touch == touch) {
			setState(State.Failed);
			return true;
		}

		return false;
	}

	public interface Delegate {
		void onLongPress(@Nonnull LongPressGestureRecognizer recognizer);
	}
}