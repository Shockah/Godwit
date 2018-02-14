package pl.shockah.godwit.animfx

import javax.annotation.Nullable

abstract class RawFx<Void> extends FxImpl {
	RawFx(float duration) {
		super(duration)
	}

	@Override
	final void update(@Nullable Object object, float f, float previous) {
		update(f, previous)
	}

	abstract void update(float f, float previous)
}