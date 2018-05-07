package pl.shockah.godwit.gesture;

import java.util.Date;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import pl.shockah.godwit.Godwit;
import pl.shockah.godwit.geom.Circle;
import pl.shockah.godwit.geom.IVec2;
import pl.shockah.godwit.geom.MutableVec2;
import pl.shockah.godwit.geom.Vec2;

public class PanGestureRecognizer extends ContinuousGestureRecognizer {
	public static float getStationaryRadius() {
		IVec2 ppi = Godwit.getInstance().getPpi();
		float average = (ppi.x() + ppi.y()) * 0.5f;
		return average * 0.1f;
	}

	@Nonnull public final Delegate delegate;

	@Nullable public Touch touch = null;

	public PanGestureRecognizer(@Nonnull GestureHandler handler, @Nonnull SimpleDelegate delegate) {
		this(handler, (recognizer, touch, initialPoint, currentPoint, delta) -> delegate.onPan(recognizer, initialPoint, currentPoint, delta));
	}

	public PanGestureRecognizer(@Nonnull GestureHandler handler, @Nonnull Delegate delegate) {
		super(handler);
		this.delegate = delegate;
	}

	@Override
	public PanGestureRecognizer clone() {
		PanGestureRecognizer clone = new PanGestureRecognizer(handler, delegate);
		cloneDependencies(clone);
		return clone;
	}

	@Override
	public void setState(@Nonnull State state) {
		super.setState(state);
		if (isFinished())
			touch = null;
	}

	@Override
	protected void onTouchUsedByRecognizer(@Nonnull Touch touch) {
		if (touch != this.touch)
			return;
		if (isInProgress() || getState() == State.Detecting)
			setState(State.Failed);
	}

	@Override
	protected void handleTouchDown(@Nonnull Touch touch, @Nonnull Vec2 point) {
		if ((getState() == State.Possible || getState() == State.Failed) && this.touch == null && touch.getRecognizer() == null) {
			this.touch = touch;
			setState(State.Detecting);
		}
	}

	@Override
	protected void handleTouchDragged(@Nonnull Touch touch, @Nonnull Vec2 point) {
		if (touch != this.touch)
			return;

		if (getState() == State.Detecting) {
			if (!requiredRecognizersFailed())
				return;
			if (touch.getRecognizer() != null)
				return;

			if (!new Circle(touch.points.get(0).position, getStationaryRadius()).contains(point)) {
				touch.setRecognizer(this);
				setState(State.Began);
				delegate.onPan(this, touch, touch.points.get(0).position, point, point.subtract(touch.points.get(touch.points.size() - 2).position));
			}
		} else if (isInProgress()) {
			setState(State.Changed);
			delegate.onPan(this, touch, touch.points.get(0).position, point, point.subtract(touch.points.get(touch.points.size() - 2).position));
		}
	}

	@Override
	protected void handleTouchUp(@Nonnull Touch touch, @Nonnull Vec2 point) {
		if (touch != this.touch)
			return;
		this.touch = null;

		if (getState() == State.Detecting) {
			setState(State.Failed);
		} else if (isInProgress()) {
			setState(State.Ended);
			delegate.onPan(this, touch, touch.points.get(0).position, point, point.subtract(touch.points.get(touch.points.size() - 2).position));
		}
	}

	@Nonnull public static Vec2 getFlingVelocityForDuration(@Nonnull Touch touch, float duration) {
		return getFlingVelocity(touch, duration, null);
	}

	@Nonnull public static Vec2 getFlingVelocityForSamples(@Nonnull Touch touch, int samples) {
		return getFlingVelocity(touch, null, samples);
	}

	@Nonnull public static Vec2 getFlingVelocity(@Nonnull Touch touch, @Nullable Float duration, @Nullable Integer samples) {
		MutableVec2 v = new MutableVec2();
		Vec2 firstPoint = null;
		int currentSamples = 0;
		Date lastDate = touch.points.get(touch.points.size() - 1).date;
		Date firstDate = null;

		for (int i = touch.points.size() - 1; i >= 0; i--) {
			if (samples != null && currentSamples >= samples)
				break;
			Touch.Point point = touch.points.get(i);
			if (duration != null && lastDate.getTime() - point.date.getTime() > duration * 1000)
				break;
			v.x += point.position.x;
			v.y += point.position.y;
			currentSamples++;
			firstPoint = point.position;
			firstDate = point.date;
		}

		if (firstPoint == null)
			return Vec2.zero;
		if (lastDate.getTime() <= firstDate.getTime())
			return Vec2.zero;

		return ((v.divide(currentSamples)).subtract(firstPoint)).multiply((lastDate.getTime() - firstDate.getTime()) / 1000f);
	}

	public interface Delegate {
		void onPan(@Nonnull PanGestureRecognizer recognizer, @Nonnull Touch touch, @Nonnull Vec2 initialPoint, @Nonnull Vec2 currentPoint, @Nonnull Vec2 delta);
	}

	public interface SimpleDelegate {
		void onPan(@Nonnull PanGestureRecognizer recognizer, @Nonnull Vec2 initialPoint, @Nonnull Vec2 currentPoint, @Nonnull Vec2 delta);
	}
}