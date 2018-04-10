package pl.shockah.godwit.gesture;

import javax.annotation.Nonnull;

import pl.shockah.godwit.collection.UnorderedPair;
import pl.shockah.godwit.geom.IVec2;
import pl.shockah.godwit.geom.Vec2;

public class PinchGestureRecognizer extends ContinuousGestureRecognizer {
	@Nonnull public final Delegate delegate;

	@Nonnull private UnorderedPair<Touch> touches = new UnorderedPair<>(null, null);

	public PinchGestureRecognizer(@Nonnull GestureHandler handler, @Nonnull Delegate delegate) {
		super(handler);
		this.delegate = delegate;
	}

	@Override
	protected void setState(@Nonnull State state) {
		super.setState(state);

		if (state == State.Failed || state == State.Ended || state == State.Cancelled) {
			touches = new UnorderedPair<>(null, null);
		}
	}

	private void addTouch(@Nonnull Touch touch) {
		if (touches.first == null)
			touches = new UnorderedPair<>(touch, null);
		else if (touches.second == null)
			touches = new UnorderedPair<>(touches.first, touch);
	}

	@Override
	protected boolean handleTouchDown(@Nonnull Touch touch, @Nonnull Vec2 point) {
		if (getState() == State.Possible && touch.continuousRecognizer == null) {
			if (touches.isFull())
				return false;

			addTouch(touch);
			if (touches.isFull()) {
				touches.first.continuousRecognizer = this;
				touches.second.continuousRecognizer = this;
				setState(State.Began);
			}
			return true;
		}

		return false;
	}

	@Override
	protected boolean handleTouchDragged(@Nonnull Touch touch, @Nonnull Vec2 point) {
		if (isInProgress()) {
			if (!touches.contains(touch))
				return false;

			setState(State.Changed);
			callDelegate();

			return true;
		}
		return false;
	}

	@Override
	protected boolean handleTouchUp(@Nonnull Touch touch, @Nonnull Vec2 point) {
		if (isInProgress()) {
			UnorderedPair<Touch> touches = this.touches;
			setState(State.Ended);
			callDelegate(touches);
			return true;
		} else if (getState() == State.Possible) {
			if (!touches.isEmpty()) {
				setState(State.Failed);
				return true;
			}
		}
		return false;
	}

	private void callDelegate() {
		callDelegate(touches);
	}

	private void callDelegate(@Nonnull UnorderedPair<Touch> touches) {
		IVec2 initialPoint1 = touches.first.points.get(0).position;
		IVec2 initialPoint2 = touches.second.points.get(0).position;
		float initialDistance = (initialPoint2 - initialPoint1).getLength();

		IVec2 currentPoint1 = touches.first.points.get(touches.first.points.size() - 1).position;
		IVec2 currentPoint2 = touches.second.points.get(touches.first.points.size() - 1).position;
		float currentDistance = (currentPoint2 - currentPoint1).getLength();

		delegate.onPinch(this, initialPoint1, initialPoint2, currentPoint1, currentPoint2, initialDistance, currentDistance);
	}

	public interface Delegate {
		void onPinch(@Nonnull PinchGestureRecognizer recognizer, @Nonnull IVec2 initialPoint1, @Nonnull IVec2 initialPoint2, @Nonnull IVec2 currentPoint1, @Nonnull IVec2 currentPoint2, float initialDistance, float currentDistance);
	}
}