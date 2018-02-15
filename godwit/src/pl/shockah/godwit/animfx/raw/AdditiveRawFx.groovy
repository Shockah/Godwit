package pl.shockah.godwit.animfx.raw

import groovy.transform.CompileStatic
import pl.shockah.godwit.animfx.AdditiveFx
import pl.shockah.godwit.animfx.Easing

import javax.annotation.Nonnull

@CompileStatic
class AdditiveRawFx extends AdditiveFx<RawFx> implements RawFx {
	AdditiveRawFx(@Nonnull RawFx fx) {
		super(fx)
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

	@Override
	void update(float f, float previous) {
		float newf = f - previous as float
		fx.update(newf, previous)
	}

	@Override
	void finish(float f, float previous) {
		fx.finish(0f, 0f)
	}
}