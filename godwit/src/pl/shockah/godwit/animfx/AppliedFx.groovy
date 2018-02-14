package pl.shockah.godwit.animfx

import groovy.transform.CompileStatic

import javax.annotation.Nonnull

@CompileStatic
abstract class AppliedFx<T> extends FxImpl {
	@Nonnull final T object

	AppliedFx(@Nonnull T object, float duration) {
		super(duration)
		this.object = object
	}
}