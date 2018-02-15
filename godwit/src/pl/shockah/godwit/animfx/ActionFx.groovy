package pl.shockah.godwit.animfx

import groovy.transform.CompileStatic
import groovy.transform.stc.ClosureParams
import groovy.transform.stc.SimpleType

import javax.annotation.Nonnull
import javax.annotation.Nullable

@CompileStatic
class ActionFx<T> extends FxImpl<T> {
	protected final Closure closure

	ActionFx(
			float duration,
			@ClosureParams(value = SimpleType, options = ["T"]) @Nonnull Closure closure) {
		super(duration)
		this.closure = closure
	}

	@Override
	final void update(@Nullable T object, float f, float previous) {
	}

	@Override
	void finish(@Nullable T object, float v) {
		closure(object)
	}
}