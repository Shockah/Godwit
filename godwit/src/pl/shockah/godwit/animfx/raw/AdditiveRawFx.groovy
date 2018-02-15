package pl.shockah.godwit.animfx.raw

import groovy.transform.CompileStatic
import pl.shockah.godwit.animfx.AdditiveFx

import javax.annotation.Nonnull

@CompileStatic
class AdditiveRawFx extends AdditiveFx<RawFx> implements RawFx {
	AdditiveRawFx(@Nonnull RawFx fx) {
		super(fx)
	}

	@Override
	void update(float f, float previous) {
		fx.update(getModifiedValue(f, previous), previous)
	}

	@Override
	void finish(float f, float previous) {
		fx.finish(0f, 0f)
	}
}