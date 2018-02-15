package pl.shockah.godwit.animfx

import com.badlogic.gdx.Gdx
import groovy.transform.CompileStatic
import pl.shockah.godwit.animfx.object.ObjectFx
import pl.shockah.godwit.animfx.raw.RawFx

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

	final void updateDelta(@Nullable T object) {
		updateBy(object, Gdx.graphics.deltaTime)
	}

	void updateBy(@Nullable T object, float delta) {
		if (stopped)
			return

		elapsed += delta * (reversed ? -1f : 1f)
		update(object)
	}

	protected void updateFx(@Nullable T object, float f, float previous) {
		f = fx.method.ease(f)
		previous = fx.method.ease(previous)

		if (fx instanceof RawFx)
			((RawFx)fx).update(f, previous)
		else if (fx instanceof ObjectFx<T> && object)
			((ObjectFx<T>)fx).update(object, f, previous)
		else
			throw new IllegalArgumentException()
	}

	protected void finishFx(@Nullable T object, float f, float previous) {
		if (fx instanceof RawFx)
			((RawFx)fx).finish(f, previous)
		else if (fx instanceof ObjectFx<T> && object)
			((ObjectFx<T>)fx).finish(object, f, previous)
		else
			throw new IllegalArgumentException()
	}

	void update(@Nullable T object) {
		if (stopped)
			return

		float current = elapsed / fx.duration as float
		float currentBound = Math.min(Math.max(current, 0f), 1f)
		updateFx(object, currentBound, previous)
		previous = current

		if (reversed) {
			if (current < 0f) {
				finishFx(object, currentBound, previous)
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
				finishFx(object, currentBound, previous)
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