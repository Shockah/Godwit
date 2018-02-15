package pl.shockah.godwit.animfx.object

import groovy.transform.CompileStatic
import groovy.transform.stc.ClosureParams
import groovy.transform.stc.SimpleType
import pl.shockah.godwit.animfx.ClosureFx

import javax.annotation.Nonnull

@CompileStatic
class ObjectClosureFx<T> extends ObjectFxImpl<T> implements ClosureFx {
	@Nonnull final Closure closure

	ObjectClosureFx(
			float duration,
			@ClosureParams(value = SimpleType, options = ["T,float,float"]) @Nonnull Closure closure) {
		super(duration)
		this.closure = closure
	}

	@Override
	void update(@Nonnull T object, float f, float previous) {
		closure(object, f, previous)
	}
}