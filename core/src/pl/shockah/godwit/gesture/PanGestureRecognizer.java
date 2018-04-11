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
	protected void setState(@Nonnull State state) {
		super.setState(state);

		if (isFinished())
			touch = null;
	}

	@Override
	protected void onTouchUsedByContinuousRecognizer(@Nonnull Touch touch) {
		if (touch != this.touch)
			return;

		if (isInProgress() || getState() == State.Detecting)
			setState(State.Failed);
	}

	@Override
	protected boolean handleTouchDown(@Nonnull Touch touch, @Nonnull Vec2 point) {
		if ((getState() == State.Possible || getState() == State.Failed) && this.touch == null && touch.getContinuousRecognizer() == null) {
			this.touch = touch;
			//touch.continuousRecognizer = this;
			setState(State.Detecting);
			return true;
		}

		return false;
	}

	@Override
	protected boolean handleTouchDragged(@Nonnull Touch touch, @Nonnull Vec2 point) {
		if (touch != this.touch)
			return false;

		if (getState() == State.Detecting) {
			if (!requiredRecognizersFailed())
				return false;

			if (touch.getContinuousRecognizer() != null)
				return false;

			if (!new Circle(touch.points.get(0).position, getStationaryRadius()).contains(point)) {
				setState(State.Began);
				touch.setContinuousRecognizer(this);
				delegate.onPan(this, touch.points.get(0).position, point, point - touch.points.get(touch.points.size() - 2).position);
			}

			return true;
		} else if (isInProgress()) {
			setState(State.Changed);
			delegate.onPan(this, touch.points.get(0).position, point, point - touch.points.get(touch.points.size() - 2).position);
			return true;
		}

		return false;
	}

	@Override
	protected boolean handleTouchUp(@Nonnull Touch touch, @Nonnull Vec2 point) {
		if (touch != this.touch)
			return false;
		this.touch = null;

		if (getState() == State.Detecting) {
			setState(State.Failed);
			return true;
		} else if (isInProgress()) {
			setState(State.Ended);
			delegate.onPan(this, touch.points.get(0).position, point, point - touch.points.get(touch.points.size() - 2).position);
			return true;
		}
		return false;
	}

	public interface Delegate {
		void onPan(@Nonnull PanGestureRecognizer recognizer, @Nonnull IVec2 initialPoint, @Nonnull IVec2 currentPoint, @Nonnull IVec2 delta);
	}
}