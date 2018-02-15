package pl.shockah.godwit.animfx

import groovy.transform.CompileStatic

import javax.annotation.Nonnull

@CompileStatic
abstract class AdditiveFx<F extends Fx> extends ModifierFx<F> {
	AdditiveFx(@Nonnull F fx) {
		super(fx)
	}

	@Override
	protected float getModifiedValue(float f, float previous) {
		return f - previous
	}
}