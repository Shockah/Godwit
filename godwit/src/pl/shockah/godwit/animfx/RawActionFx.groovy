package pl.shockah.godwit.animfx

import groovy.transform.CompileStatic

import javax.annotation.Nonnull

@CompileStatic
class RawActionFx extends RawFx {
	protected final Closure closure

	RawActionFx(float duration, @Nonnull Closure closure) {
		super(duration)
		this.closure = closure
	}

	@Override
	final void update(float f, float previous) {
	}

	@Override
	void finish(float f) {
		closure()
	}
}