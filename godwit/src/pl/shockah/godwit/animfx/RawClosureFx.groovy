package pl.shockah.godwit.animfx

import groovy.transform.CompileStatic
import groovy.transform.stc.ClosureParams
import groovy.transform.stc.SimpleType

import javax.annotation.Nonnull

@CompileStatic
class RawClosureFx extends RawFx {
	protected final Closure closure

	RawClosureFx(
			float duration,
			@ClosureParams(value = SimpleType, options = ["float,float"]) @Nonnull Closure closure) {
		super(duration)
		this.closure = closure
	}

	@Override
	void update(float f, float previous) {
		closure(f, previous)
	}

	@Override
	final void finish(float f) {

	}
}