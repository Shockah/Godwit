package pl.shockah.godwit.animfx

import groovy.transform.CompileStatic

import javax.annotation.Nonnull

@CompileStatic
class RawActionFx extends RawFx {
	protected final Closure closure

	RawActionFx(@Nonnull Closure closure) {
		super(0f)
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