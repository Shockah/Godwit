package pl.shockah.godwit.animfx

import groovy.transform.CompileStatic

import javax.annotation.Nullable

@CompileStatic
abstract class RawFx extends FxImpl<Void> {
	RawFx(float duration) {
		super(duration)
	}

	@Override
	final void update(@Nullable Void object, float f, float previous) {
		update(f, previous)
	}

	@Override
	final void finish(@Nullable Void object, float f) {
		finish(f)
	}

	abstract void update(float f, float previous)

	void finish(float f) {
		update(f, f)
	}
}