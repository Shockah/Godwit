package pl.shockah.godwit.animfx

import com.badlogic.gdx.Gdx
import groovy.transform.CompileStatic

import javax.annotation.Nonnull
import javax.annotation.Nullable

@CompileStatic
class FxInstance<T> {
	static enum EndAction {
		End,
		Loop,
		Reverse,
		ReverseLoop
	}

	@Nonnull final Fx<T> fx
	@Nonnull final EndAction endAction
	boolean stopped = false
	protected float elapsed = 0f
	protected float previous = 0f
	protected boolean reversed = false

	FxInstance(@Nonnull Fx<T> fx, @Nonnull EndAction endAction) {
		this.fx = fx
		this.endAction = endAction
	}

	final void updateDelta(@Nullable T object) {
		updateBy(object, Gdx.graphics.deltaTime)
	}

	void updateBy(@Nullable T object, float delta) {
		if (stopped)
			return

		elapsed += delta * (reversed ? -1f : 1f)
		update(object)
	}

	void update(@Nullable T object) {
		if (stopped)
			return

		float current = elapsed / fx.duration as float
		float currentBound = Math.min(Math.max(current, 0f), 1f)
		fx.update(object, fx.method.ease(currentBound), fx.method.ease(previous))
		previous = current

		if (reversed) {
			if (current < 0f) {
				fx.finish(object, fx.method.ease(currentBound))
				switch (endAction) {
					case EndAction.End:
					case EndAction.Reverse:
						stopped = true
						break
					case EndAction.Loop:
						elapsed = fx.duration
						previous = 1f
						update(object)
						break
					case EndAction.ReverseLoop:
						elapsed = -current * fx.duration as float
						reversed = false
						update(object)
						break
					default:
						break
				}
			}
		} else {
			if (current >= 1f) {
				fx.finish(object, fx.method.ease(currentBound))
				switch (endAction) {
					case EndAction.End:
						stopped = true
						break
					case EndAction.Loop:
						elapsed = 0f
						previous = 0f
						update(object)
						break
					case EndAction.Reverse:
					case EndAction.ReverseLoop:
						elapsed = fx.duration * (1f - (current - 1f)) as float
						reversed = true
						update(object)
						break
					default:
						break
				}
			}
		}
	}
}