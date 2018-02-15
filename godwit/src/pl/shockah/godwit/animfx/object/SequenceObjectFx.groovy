package pl.shockah.godwit.animfx.object

import groovy.transform.CompileStatic
import pl.shockah.godwit.animfx.SequenceFx

import javax.annotation.Nonnull

@CompileStatic
class SequenceObjectFx<T> extends SequenceFx<ObjectFx<T>> implements ObjectFx<T> {
	SequenceObjectFx(ObjectFx<T>... fxes) {
		super(fxes)
	}

	SequenceObjectFx(@Nonnull List<? extends ObjectFx<T>> fxes) {
		super(fxes)
	}

	protected void update(@Nonnull T object, float f, float previous, boolean finish) {
		if (fxes.isEmpty())
			return

		f = f * duration as float
		previous = previous * duration as float

		for (ObjectFx<T> fx : getFxesToFinish(f, previous)) {
			float value = f > previous ? 1f : 0f
			fx.finish(object, value, value)
		}

		FxResult<ObjectFx<T>> result = getFx(f)
		if (finish)
			result.fx.finish(object, result.getFxF(f), result.getFxF(previous))
		else
			result.fx.update(object, result.getEased(f), result.getEased(previous))
	}

	@Override
	void update(@Nonnull T object, float f, float previous) {
		update(object, f, previous, false)
	}

	@Override
	void finish(@Nonnull T object, float f, float previous) {
		update(object, f, previous, true)
	}
}