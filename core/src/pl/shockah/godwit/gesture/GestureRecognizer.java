package pl.shockah.godwit.gesture;

import java.util.HashSet;
import java.util.Set;

import javax.annotation.Nonnull;

import lombok.Getter;
import pl.shockah.godwit.Godwit;
import pl.shockah.godwit.geom.Vec2;

public abstract class GestureRecognizer {
	@Nonnull public final GestureHandler handler;

	@Getter
	@Nonnull private State state = State.Possible;

	@Nonnull protected final Set<GestureRecognizer> requireToFail = new HashSet<>();
	@Nonnull protected final Set<GestureRecognizer> failListeners = new HashSet<>();

	protected GestureRecognizer(@Nonnull GestureHandler handler) {
		this.handler = handler;
	}

	public void register() {
		Godwit.getInstance().inputManager.gestureManager.recognizers.add(this);
	}

	public void unregister() {
		Godwit.getInstance().inputManager.gestureManager.recognizers.remove(this);
	}

	public final boolean isRegistered() {
		return Godwit.getInstance().inputManager.gestureManager.recognizers.contains(this);
	}

	@Override
	public String toString() {
		return String.format("[%s]", getClass().getSimpleName());
	}

	protected final void cloneDependencies(@Nonnull GestureRecognizer clone) {
		for (GestureRecognizer failRecognizer : requireToFail) {
			clone.requireToFail(failRecognizer);
		}
		for (GestureRecognizer listener : failListeners) {
			listener.requireToFail(clone);
		}
	}

	public abstract GestureRecognizer clone();

	public void setState(@Nonnull State state) {
		if (state == this.state)
			return;

		//System.out.println(String.format("%s for %s state change %s -> %s", toString(), handler.toString(), this.state.name(), state.name()));
		this.state = state;

//		Gdx.app.log("GestureRecognizer", new Date().getTime() + " | " + StreamSupport.stream(Godwit.getInstance().inputManager.gestureManager.recognizers)
//				.map(recognizer -> String.format("%s %s", recognizer.toString(), recognizer.getState().name()))
//				.map(text -> String.format("%1$-40s", text))
//				.collect(Collectors.joining("")));

		if (state == State.Ended) {
			for (GestureRecognizer recognizer : failListeners) {
				recognizer.onRequiredFailEnded(this);
			}
		} else if (state == State.Failed) {
			for (GestureRecognizer recognizer : failListeners) {
				recognizer.onRequiredFailFailed(this);
			}
		}
	}

	public final void cancel() {
		if (state != State.Possible)
			setState(State.Cancelled);
	}

	public final boolean isInProgress() {
		return state == State.Began || state == State.Changed;
	}

	public final boolean isFinished() {
		return state == State.Ended || state == State.Failed || state == State.Cancelled;
	}

	public void requireToFail(@Nonnull GestureRecognizer recognizer) {
		if (recognizer == this)
			throw new IllegalArgumentException("Cannot require itself GestureRecognizer to fail.");
		if (recognizer.requireToFail.contains(this))
			throw new IllegalArgumentException("Detected circular failure GestureRecognizer dependency.");

		requireToFail.add(recognizer);
		recognizer.failListeners.add(this);
	}

	public final boolean requiredRecognizersFailed() {
		for (GestureRecognizer recognizer : requireToFail) {
			if (recognizer.getState() != State.Failed && recognizer.isRegistered())
				return false;
		}
		return true;
	}

	protected void onRequiredFailEnded(@Nonnull GestureRecognizer recognizer) {
		setState(State.Failed);
	}

	protected void onRequiredFailFailed(@Nonnull GestureRecognizer recognizer) {
	}

	protected void handleTouchDown(@Nonnull Touch touch, @Nonnull Vec2 point) {
	}

	protected void handleTouchDragged(@Nonnull Touch touch, @Nonnull Vec2 point) {
	}

	protected void handleTouchUp(@Nonnull Touch touch, @Nonnull Vec2 point) {
	}

	public enum State {
		Possible, Detecting, Began, Changed, Failed, Cancelled, Ended;
	}
}