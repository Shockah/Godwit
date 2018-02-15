package pl.shockah.godwit.animfx

import groovy.transform.CompileStatic
import pl.shockah.godwit.animfx.ease.Easing
import pl.shockah.godwit.animfx.object.ObjectFx
import pl.shockah.godwit.animfx.object.SequenceObjectFx
import pl.shockah.godwit.animfx.raw.RawFx
import pl.shockah.godwit.animfx.raw.SequenceRawFx

import javax.annotation.Nonnull

@CompileStatic
abstract class SequenceFx<F extends Fx> implements Fx {
	@Nonnull protected final List<F> fxes

	SequenceFx(F... fxes) {
		this(Arrays.asList(fxes))
	}

	SequenceFx(@Nonnull List<? extends F> fxes) {
		this.fxes = new ArrayList<>(fxes)
	}

	static SequenceRawFx of(RawFx... fxes) {
		return new SequenceRawFx(fxes)
	}

	static SequenceRawFx of(List<? extends RawFx> fxes) {
		return new SequenceRawFx(fxes)
	}

	static <T2> SequenceObjectFx<T2> of(ObjectFx<? extends T2>... fxes) {
		return new SequenceObjectFx<>(fxes)
	}

	static <T2> SequenceObjectFx<T2> of(List<? extends ObjectFx<? extends T2>> fxes) {
		return new SequenceObjectFx<>(fxes)
	}

	@Override
	float getDuration() {
		return fxes.collect { it.duration }.sum() as float
	}

	@Override
	final Easing getMethod() {
		return Easing.linear
	}

	@Override
	void setMethod(@Nonnull Easing method) {
		throw new UnsupportedOperationException()
	}

	@Nonnull protected FxResult<F> getFx(float f) {
		float fStart = 0f
		for (int i in 0..<fxes.size()) {
			Fx fx = fxes[i]
			float duration = fx.duration
			if (f - fStart < duration)
				return new FxResult<>(fx, i, fStart, fStart + fx.duration as float)
			fStart += duration
		}

		int lastIndex = fxes.size() - 1
		Fx last = fxes[lastIndex]
		return new FxResult<>(last, lastIndex, fStart - last.duration as float, fStart)
	}

	@Nonnull protected List<F> getFxesToFinish(float f, float previous) {
		FxResult<F> resultPrevious = getFx(previous)
		FxResult<F> result = getFx(f)

		List<F> results = []
		if (resultPrevious.index != result.index) {
			if (f > previous) {
				for (int i in resultPrevious.index..<result.index) {
					results.add(fxes[i])
				}
			} else if (f < previous) {
				for (int i = resultPrevious.index; i > result.index; i--) {
					results.add(fxes[i])
				}
			}
		}
		return results
	}

	static final class FxResult<F extends Fx> {
		final F fx
		final int index
		final float a
		final float b

		FxResult(F fx, int index, float a, float b) {
			this.fx = fx
			this.index = index
			this.a = a
			this.b = b
		}

		float getFxF(float f) {
			return Math.min(Math.max((f - a) / (b - a) as float, 0f), 1f)
		}

		float getEased(float f) {
			return fx.method.ease(getFxF(f))
		}
	}
}