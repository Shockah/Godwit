package pl.shockah.godwit.gesture;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Timer;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import pl.shockah.godwit.geom.Vec2;

public class TapGestureRecognizer extends GestureRecognizer {
	public final int tapsRequired;
	public final float delay;

	@Nonnull
	public final Delegate delegate;

	@Nullable
	public Touch touch = null;

	private Timer.Task task = null;

	private int taps = 0;

	public TapGestureRecognizer(@Nonnull GestureHandler group, @Nonnull SimpleDelegate delegate) {
		this(group, (recognizer, touch) -> delegate.onTap(recognizer));
	}

	public TapGestureRecognizer(@Nonnull GestureHandler group, @Nonnull Delegate delegate) {
		this(group, 1, 0f, delegate);
	}

	public TapGestureRecognizer(@Nonnull GestureHandler group, int tapsRequired, float delay, @Nonnull SimpleDelegate delegate) {
		this(group, tapsRequired, delay, (recognizer, touch) -> delegate.onTap(recognizer));
	}

	public TapGestureRecognizer(@Nonnull GestureHandler group, int tapsRequired, float delay, @Nonnull Delegate delegate) {
		super(group);
		this.tapsRequired = tapsRequired;
		this.delay = delay;
		this.delegate = delegate;
	}

	@Override
	public String toString() {
		return String.format("[%s:%d]", getClass().getSimpleName(), tapsRequired);
	}

	@Override
	public TapGestureRecognizer clone() {
		TapGestureRecognizer clone = new TapGestureRecognizer(handler, tapsRequired, delay, delegate);
		cloneDependencies(clone);
		return clone;
	}

	@Override
	public void setState(@Nonnull State state) {
		super.setState(state);

		if (isFinished()) {
			touch = null;
			taps = 0;
			if (task != null) {
				task.cancel();
				task = null;
			}
		}
	}

	@Override
	protected void onRequiredFailFailed(@Nonnull GestureRecognizer recognizer) {
		if (touch != null && taps >= tapsRequired) {
			if (!requiredRecognizersFailed())
				return;
			if (touch.getRecognizer() != null) {
				setState(State.Failed);
				return;
			}
			Touch touch = this.touch;
			touch.setRecognizer(this);
			setState(State.Ended);
			delegate.onTap(this, touch);
		}
	}

	@Override
	protected void handleTouchDown(@Nonnull Touch touch, @Nonnull Vec2 point) {
		if (getState() == State.Possible || isInProgress()) {
			if (isInProgress()) {
				setState(State.Changed);
				taps++;
			} else {
				setState(State.Began);
				taps = 1;
			}
			this.touch = touch;
			if (task != null) {
				task.cancel();
				task = null;
			}
			if (delay > 0f) {
				task = Timer.schedule(new Timer.Task() {
					@Override
					public void run() {
						Gdx.app.postRunnable(() -> {
							setState(State.Failed);
							TapGestureRecognizer.this.touch = null;
							taps = 0;
							task = null;
						});
					}
				}, delay);
			}
		}
	}

	@Override
	protected void handleTouchUp(@Nonnull Touch touch, @Nonnull Vec2 point) {
		if (isInProgress()) {
			if (this.touch == touch) {
				if (taps >= tapsRequired) {
					if (!requiredRecognizersFailed())
						return;
					if (touch.getRecognizer() != null) {
						setState(State.Failed);
						return;
					}
					touch.setRecognizer(this);
					setState(State.Ended);
					delegate.onTap(this, touch);
				}
			}
		}
	}

	public interface Delegate {
		void onTap(@Nonnull TapGestureRecognizer recognizer, @Nonnull Touch touch);
	}

	public interface SimpleDelegate {
		void onTap(@Nonnull TapGestureRecognizer recognizer);
	}
}