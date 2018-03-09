package pl.shockah.godwit.fx;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import pl.shockah.godwit.Godwit;
import pl.shockah.godwit.fx.object.ObjectFx;
import pl.shockah.godwit.fx.raw.RawFx;

public class FxInstance<T> {
	public enum EndAction {
		End,
		Loop,
		Reverse,
		ReverseLoop
	}

	@Nonnull public final Fx fx;
	@Nonnull public final EndAction endAction;
	public float speed = 1f;

	private boolean stopped = false;
	private float elapsed = 0f;
	private float previous = 0f;
	private boolean reversed = false;

	public FxInstance(@Nonnull Fx fx, @Nonnull EndAction endAction) {
		this.fx = fx;
		this.endAction = endAction;
	}

	public final boolean isStopped() {
		return stopped;
	}

	public final void stop() {
		stopped = true;
	}

	public final void updateDelta(@Nullable T object) {
		updateBy(object, Godwit.getInstance().getDeltaTime());
	}

	public void updateBy(@Nullable T object, float delta) {
		if (stopped)
			return;

		elapsed += delta * speed * (reversed ? -1f : 1f);
		update(object);
	}

	@SuppressWarnings("unchecked")
	protected void updateFx(@Nullable T object, float f, float previous) {
		f = fx.getMethod().ease(f);
		previous = fx.getMethod().ease(previous);

		if (fx instanceof RawFx)
			((RawFx)fx).update(f, previous);
		else if (fx instanceof ObjectFx<?> && object != null)
			((ObjectFx<T>)fx).update(object, f, previous);
		else
			throw new IllegalArgumentException();
	}

	@SuppressWarnings("unchecked")
	protected void finishFx(@Nullable T object, float f, float previous) {
		if (fx instanceof RawFx)
			((RawFx)fx).finish(f, previous);
		else if (fx instanceof ObjectFx<?> && object != null)
			((ObjectFx<T>)fx).finish(object, f, previous);
		else
			throw new IllegalArgumentException();
	}

	public void update(@Nullable T object) {
		if (stopped)
			return;

		float current = elapsed / fx.getDuration();
		float currentBound = Math.min(Math.max(current, 0f), 1f);
		updateFx(object, currentBound, previous);
		previous = current;

		if (reversed) {
			if (current < 0f) {
				finishFx(object, currentBound, previous);
				switch (endAction) {
					case End:
					case Reverse:
						stopped = true;
						break;
					case Loop:
						elapsed = fx.getDuration();
						previous = 1f;
						update(object);
						break;
					case ReverseLoop:
						elapsed = -current * fx.getDuration();
						reversed = false;
						update(object);
						break;
					default:
						break;
				}
			}
		} else {
			if (current >= 1f) {
				finishFx(object, currentBound, previous);
				switch (endAction) {
					case End:
						stopped = true;
						break;
					case Loop:
						elapsed = 0f;
						previous = 0f;
						update(object);
						break;
					case Reverse:
					case ReverseLoop:
						elapsed = fx.getDuration() * (1f - (current - 1f));
						reversed = true;
						update(object);
						break;
					default:
						break;
				}
			}
		}
	}
}