package pl.shockah.godwit.fx;

import javax.annotation.Nonnull;

import pl.shockah.godwit.Godwit;

public class FxInstance {
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

	public final void updateDelta() {
		updateBy(Godwit.getInstance().getDeltaTime());
	}

	public void updateBy(float delta) {
		if (stopped)
			return;

		elapsed += delta * speed * (reversed ? -1f : 1f);
		update();
	}

	protected void updateFx(float f, float previous) {
		f = fx.getMethod().ease(f);
		previous = fx.getMethod().ease(previous);
		fx.update(f, previous);
	}

	protected void finishFx(float f, float previous) {
		fx.finish(f, previous);
	}

	public void update() {
		if (stopped)
			return;

		float current = elapsed / fx.getDuration();
		float currentBound = Math.min(Math.max(current, 0f), 1f);
		updateFx(currentBound, previous);
		previous = current;

		if (reversed) {
			if (current < 0f) {
				finishFx(currentBound, previous);
				switch (endAction) {
					case End:
					case Reverse:
						stopped = true;
						break;
					case Loop:
						elapsed = fx.getDuration();
						previous = 1f;
						update();
						break;
					case ReverseLoop:
						elapsed = -current * fx.getDuration();
						reversed = false;
						update();
						break;
					default:
						break;
				}
			}
		} else {
			if (current >= 1f) {
				finishFx(currentBound, previous);
				switch (endAction) {
					case End:
						stopped = true;
						break;
					case Loop:
						elapsed = 0f;
						previous = 0f;
						update();
						break;
					case Reverse:
					case ReverseLoop:
						elapsed = fx.getDuration() * (1f - (current - 1f));
						reversed = true;
						update();
						break;
					default:
						break;
				}
			}
		}
	}
}