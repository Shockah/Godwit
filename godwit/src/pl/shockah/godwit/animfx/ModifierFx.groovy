package pl.shockah.godwit.animfx

import groovy.transform.CompileStatic
import pl.shockah.godwit.animfx.ease.Easing

import javax.annotation.Nonnull

@CompileStatic
abstract class ModifierFx<F extends Fx> implements Fx {
	@Nonnull final F fx

	ModifierFx(@Nonnull F fx) {
		this.fx = fx
	}

	@Override
	float getDuration() {
		return fx.duration
	}

	@Override
	Easing getMethod() {
		return fx.method
	}

	@Override
	void setMethod(@Nonnull Easing method) {
		fx.method = method
	}

	abstract protected float getModifiedValue(float f, float previous)
}