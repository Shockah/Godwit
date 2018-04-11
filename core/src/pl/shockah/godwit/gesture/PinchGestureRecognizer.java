package pl.shockah.godwit.gesture;

import javax.annotation.Nonnull;

import lombok.EqualsAndHashCode;
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
	public PinchGestureRecognizer clone() {
		PinchGestureRecognizer clone = new PinchGestureRecognizer(handler, delegate);
		cloneDependencies(clone);
		return clone;
	}

	@Override
	protected void setState(@Nonnull State state) {
		super.setState(state);

		if (isFinished())
			touches = new UnorderedPair<>(null, null);
	}

	@Override
	protected void onTouchUsedByContinuousRecognizer(@Nonnull Touch touch) {
		if (touches.contains(touch))
			setState(State.Failed);
	}

	@Override
	protected boolean handleTouchDown(@Nonnull Touch touch, @Nonnull Vec2 point) {
		if (getState() == State.Possible && touch.getContinuousRecognizer() == null) {
			for (int i = 0; i < 2; i++) {
				if (touches.first != null && touches.first.getContinuousRecognizer() != null)
					touches = touches.without(touches.first);
			}

			if (touches.isFull())
				return false;

			touches = touches.with(touch);
			if (touches.isFull()) {
				touches.first.setContinuousRecognizer(this);
				touches.second.setContinuousRecognizer(this);
				setState(State.Began);
				callDelegate();
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
		IVec2 currentPoint2 = touches.second.points.get(touches.second.points.size() - 1).position;
		float currentDistance = (currentPoint2 - currentPoint1).getLength();

		delegate.onPinch(
				this,
				new PinchInfo(initialPoint1, initialPoint2, initialDistance),
				new PinchInfo(currentPoint1, currentPoint2, currentDistance)
		);
	}

	public interface Delegate {
		void onPinch(@Nonnull PinchGestureRecognizer recognizer, @Nonnull PinchInfo initial, @Nonnull PinchInfo current);
	}

	@EqualsAndHashCode
	public static final class PinchInfo {
		@Nonnull public final IVec2 point1;
		@Nonnull public final IVec2 point2;
		public final float distance;

		public PinchInfo(@Nonnull IVec2 point1, @Nonnull IVec2 point2, float distance) {
			this.point1 = point1;
			this.point2 = point2;
			this.distance = distance;
		}
	}
}