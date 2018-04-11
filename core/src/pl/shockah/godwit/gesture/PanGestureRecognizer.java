package pl.shockah.godwit.gesture;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import pl.shockah.godwit.Godwit;
import pl.shockah.godwit.geom.Circle;
import pl.shockah.godwit.geom.IVec2;
import pl.shockah.godwit.geom.Vec2;

public class PanGestureRecognizer extends ContinuousGestureRecognizer {
	public static float getStationaryRadius() {
		IVec2 ppi = Godwit.getInstance().getPpi();
		float average = (ppi.x() + ppi.y()) * 0.5f;
		return average * 0.1f;
	}

	@Nonnull public final Delegate delegate;

	@Nullable public Touch touch = null;

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
				delegate.onPan(this, touch.points.get(0).position, point, point - touch.points.get(touch.points.size() - 2).position);
			}
		} else if (isInProgress()) {
			setState(State.Changed);
			delegate.onPan(this, touch.points.get(0).position, point, point - touch.points.get(touch.points.size() - 2).position);
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
			delegate.onPan(this, touch.points.get(0).position, point, point - touch.points.get(touch.points.size() - 2).position);
		}
	}

	public interface Delegate {
		void onPan(@Nonnull PanGestureRecognizer recognizer, @Nonnull Vec2 initialPoint, @Nonnull Vec2 currentPoint, @Nonnull Vec2 delta);
	}
}