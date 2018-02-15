package pl.shockah.godwit.animfx.raw

import groovy.transform.CompileStatic
import pl.shockah.godwit.animfx.RepeatFx

import javax.annotation.Nonnull

@CompileStatic
class RepeatRawFx extends RepeatFx<RawFx> implements RawFx {
	RepeatRawFx(@Nonnull RawFx fx, int count) {
		super(fx, count)
	}

	@Override
	void update(float f, float previous) {
		f = f * count as float
		previous = previous * count as float

		if (((int)f) != ((int)previous))
			fx.finish(f >= previous ? 1f : 0f, previous % 1f)
		fx.update(fx.method.ease(f % 1f), previous % 1f)
	}

	@Override
	void finish(float f, float previous) {
		fx.finish(f >= previous ? 1f : 0f, previous % 1f)
	}
}