package pl.shockah.godwit.animfx.raw

import groovy.transform.CompileStatic
import pl.shockah.godwit.animfx.SequenceFx

import javax.annotation.Nonnull

@CompileStatic
class SequenceRawFx extends SequenceFx<RawFx> implements RawFx {
	SequenceRawFx(RawFx... fxes) {
		super(fxes)
	}

	SequenceRawFx(@Nonnull List<? extends RawFx> fxes) {
		super(fxes)
	}

	protected void update(float f, float previous, boolean finish) {
		if (fxes.isEmpty())
			return

		f = f * duration as float
		previous = previous * duration as float

		for (RawFx fx : getFxesToFinish(f, previous)) {
			float value = f > previous ? 1f : 0f
			fx.finish(value, value)
		}

		FxResult<RawFx> result = getFx(f)
		if (finish)
			result.fx.finish(result.getFxF(f), result.getFxF(previous))
		else
			result.fx.update(result.getEased(f), result.getEased(previous))
	}

	@Override
	void update(float f, float previous) {
		update(f, previous, false)
	}

	@Override
	void finish(float f, float previous) {
		update(f, previous, true)
	}
}