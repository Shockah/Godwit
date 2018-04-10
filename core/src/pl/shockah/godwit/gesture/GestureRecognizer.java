package pl.shockah.godwit.gesture;

import com.badlogic.gdx.Gdx;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.annotation.Nonnull;

import java8.util.stream.Collectors;
import java8.util.stream.StreamSupport;
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

	@Override
	public String toString() {
		return String.format("[%s]", getClass().getSimpleName());
	}

	protected void setState(@Nonnull State state) {
		if (state == this.state)
			return;

		//System.out.println(String.format("%s for %s state change %s -> %s", toString(), handler.toString(), this.state.name(), state.name()));
		this.state = state;

		Gdx.app.log("GestureRecognizer", new Date().getTime() + " | " + StreamSupport.stream(Godwit.getInstance().inputManager.gestureManager.recognizers)
				.map(recognizer -> String.format("%s %s", recognizer.toString(), recognizer.getState().name()))
				.map(text -> String.format("%1$-40s", text))
				.collect(Collectors.joining("")));

		if (state == State.Ended) {
			for (GestureRecognizer recognizer : failListeners) {
				if (recognizer.onRequiredFailEnded(this))
					return;
			}
		} else if (state == State.Failed) {
			for (GestureRecognizer recognizer : failListeners) {
				if (recognizer.onRequiredFailFailed(this))
					return;
			}
		}
	}

	public boolean isInProgress() {
		return state == State.Began || state == State.Changed;
	}

	public void requireToFail(@Nonnull GestureRecognizer recognizer) {
		if (recognizer == this)
			throw new IllegalArgumentException("Cannot require the same GestureRecognizer to fail.");

		requireToFail.add(recognizer);
		recognizer.failListeners.add(this);
	}

	public final boolean requiredRecognizersFailed() {
		for (GestureRecognizer recognizer : requireToFail) {
			if (recognizer.getState() != State.Failed)
				return false;
		}
		return true;
	}

	protected boolean onRequiredFailEnded(@Nonnull GestureRecognizer recognizer) {
		setState(State.Failed);
		return false;
	}

	protected boolean onRequiredFailFailed(@Nonnull GestureRecognizer recognizer) {
		return false;
	}

	protected boolean handleTouchDown(@Nonnull Touch touch, @Nonnull Vec2 point) {
		return false;
	}

	protected boolean handleTouchDragged(@Nonnull Touch touch, @Nonnull Vec2 point) {
		return false;
	}

	protected boolean handleTouchUp(@Nonnull Touch touch, @Nonnull Vec2 point) {
		return false;
	}

	public enum State {
		Possible, Detecting, Began, Changed, Failed, Cancelled, Ended;
	}
}