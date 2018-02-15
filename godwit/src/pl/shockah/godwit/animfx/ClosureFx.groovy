package pl.shockah.godwit.animfx

import groovy.transform.CompileStatic
import groovy.transform.stc.ClosureParams
import groovy.transform.stc.SimpleType

import javax.annotation.Nonnull
import javax.annotation.Nullable

@CompileStatic
class ClosureFx<T> extends FxImpl<T> {
	protected final Closure closure

	ClosureFx(
			float duration,
			@ClosureParams(value = SimpleType, options = ["T,float,float"]) @Nonnull Closure closure) {
		super(duration)
		this.closure = closure
	}

	@Override
	void update(@Nullable T object, float f, float previous) {
		closure(object, f, previous)
	}
}