package pl.shockah.godwit.animfx

import groovy.transform.CompileStatic
import pl.shockah.godwit.animfx.raw.RawFxImpl

import javax.annotation.Nonnull

@CompileStatic
class ActionFx extends RawFxImpl {
	protected final Closure closure

	ActionFx(@Nonnull Closure closure) {
		super(0f)
		this.closure = closure
	}

	@Override
	final void update(float f, float previous) {
	}

	@Override
	void finish(float f, float previous) {
		closure()
	}
}