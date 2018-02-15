package pl.shockah.godwit.animfx.raw

import groovy.transform.CompileStatic
import groovy.transform.stc.ClosureParams
import groovy.transform.stc.SimpleType
import pl.shockah.godwit.animfx.ClosureFx

import javax.annotation.Nonnull

@CompileStatic
class RawClosureFx extends RawFxImpl implements ClosureFx {
	final Closure closure

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
	void finish(float f, float previous) {
		update(f, previous)
	}
}