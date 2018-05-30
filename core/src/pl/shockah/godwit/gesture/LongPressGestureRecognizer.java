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

	@Nonnull
	public final Delegate delegate;

	@Nullable
	public Touch touch = null;

	private Timer.Task task = null;

	private boolean shouldEnd = false;

	public LongPressGestureRecognizer(@Nonnull GestureHandler group, float delay, @Nonnull SimpleDelegate delegate) {
		this(group, delay, (recognizer, touch) -> delegate.onLongPress(recognizer));
	}

	public LongPressGestureRecognizer(@Nonnull GestureHandler group, float delay, @Nonnull Delegate delegate) {
		super(group);
		this.delay = delay;
		this.delegate = delegate;
	}

	@Override
	public LongPressGestureRecognizer clone() {
		LongPressGestureRecognizer clone = new LongPressGestureRecognizer(handler, delay, delegate);
		cloneDependencies(clone);
		return clone;
	}

	@Override
	public void setState(@Nonnull State state) {
		super.setState(state);

		if (isFinished()) {
			touch = null;
			shouldEnd = false;
			if (task != null) {
				task.cancel();
				task = null;
			}
		}
	}

	@Override
	protected void onRequiredFailFailed(@Nonnull GestureRecognizer recognizer) {
		if (shouldEnd && touch != null) {
			Touch touch = this.touch;
			touch.setRecognizer(this);
			setState(State.Ended);
			delegate.onLongPress(LongPressGestureRecognizer.this, touch);
		}
	}

	@Override
	public void handleTouchDown(@Nonnull Touch touch, @Nonnull Vec2 point) {
		if (getState() == State.Possible) {
			setState(State.Began);
			this.touch = touch;
			task = Timer.schedule(new Timer.Task() {
				@Override
				public void run() {
					Gdx.app.postRunnable(() -> {
						task = null;
						if (requiredRecognizersFailed()) {
							touch.setRecognizer(LongPressGestureRecognizer.this);
							setState(State.Ended);
							delegate.onLongPress(LongPressGestureRecognizer.this, touch);
						} else {
							shouldEnd = true;
						}
					});
				}
			}, delay);
		}
	}

	@Override
	public void handleTouchDragged(@Nonnull Touch touch, @Nonnull Vec2 point) {
		if (isInProgress()) {
			if (!new Circle(touch.points.get(0).position, getStationaryRadius()).contains(point)) {
				setState(State.Failed);
			} else {
				setState(State.Changed);
			}
		}
	}

	@Override
	public void handleTouchUp(@Nonnull Touch touch, @Nonnull Vec2 point) {
		if (isInProgress() && this.touch == touch)
			setState(State.Failed);
	}

	public interface Delegate {
		void onLongPress(@Nonnull LongPressGestureRecognizer recognizer, @Nonnull Touch touch);
	}

	public interface SimpleDelegate {
		void onLongPress(@Nonnull LongPressGestureRecognizer recognizer);
	}
}