package pl.shockah.godwit.animfx

import groovy.transform.CompileStatic

import javax.annotation.Nonnull

@CompileStatic
abstract class RepeatFx<F extends Fx> implements Fx {
	@Nonnull final F fx
	final int count

	RepeatFx(@Nonnull F fx, int count) {
		this.fx = fx
		this.count = count
	}

	@Override
	float getDuration() {
		return fx.duration * count
	}

	@Override
	Easing getMethod() {
		return fx.method
	}

	@Override
	void setMethod(@Nonnull Easing method) {
		fx.method = method
	}
}