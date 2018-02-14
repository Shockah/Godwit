package pl.shockah.godwit.animfx

import com.badlogic.gdx.Gdx
import groovy.transform.CompileStatic

import javax.annotation.Nonnull

@CompileStatic
class FxInstance {
	enum EndAction {
		End,
		Loop,
		Reverse,
		ReverseLoop
	}

	@Nonnull final Fx fx
	@Nonnull final EndAction endAction
	boolean stopped = false
	protected float elapsed = 0f
	protected float previous = 0f
	protected boolean reversed = false

	FxInstance(@Nonnull Fx fx, @Nonnull EndAction endAction) {
		this.fx = fx
		this.endAction = endAction
	}

	final void updateDelta() {
		updateBy(Gdx.graphics.deltaTime)
	}

	void updateBy(float delta) {
		if (stopped)
			return

		elapsed += delta * (reversed ? -1f : 1f)
		update()
	}

	void update() {
		if (stopped)
			return

		float current = elapsed / fx.duration as float
		float currentBound = Math.min(Math.max(current, 0f), 1f)
		fx.update(fx.method.ease(currentBound), fx.method.ease(previous))
		previous = current

		if (reversed) {
			if (current < 0f) {
				switch (endAction) {
					case EndAction.End:
					case EndAction.Reverse:
						stopped = true
						break
					case EndAction.Loop:
						elapsed = fx.duration
						previous = 1f
						update()
						break
					case EndAction.ReverseLoop:
						elapsed = -current * fx.duration
						reversed = false
						update()
						break
					default:
						break
				}
			}
		} else {
			if (current >= 1f) {
				switch (endAction) {
					case EndAction.End:
						stopped = true
						break
					case EndAction.Loop:
						elapsed = 0f
						previous = 0f
						update()
						break
					case EndAction.Reverse:
					case EndAction.ReverseLoop:
						elapsed = fx.duration * (1f - (current - 1f))
						reversed = true
						update()
						break
					default:
						break
				}
			}
		}
	}
}