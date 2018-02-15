package pl.shockah.godwit.animfx

import javax.annotation.Nonnull

abstract class AdditiveFx<F extends Fx> implements Fx {
	@Nonnull final F fx

	AdditiveFx(@Nonnull F fx) {
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
}