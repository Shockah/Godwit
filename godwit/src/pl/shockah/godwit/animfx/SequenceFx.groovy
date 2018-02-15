package pl.shockah.godwit.animfx

import groovy.transform.CompileStatic

import javax.annotation.Nonnull

@CompileStatic
class SequenceFx<T> implements Fx<T> {
	@Nonnull private final List<Fx<T>> fxes

	SequenceFx(Fx<T>... fxes) {
		this(Arrays.asList(fxes))
	}

	SequenceFx(@Nonnull List<Fx<T>> fxes) {
		this.fxes = new ArrayList<>(fxes)
	}

	@Override
	@Nonnull Easing getMethod() {
		return Easing.linear
	}

	@Override
	void setMethod(@Nonnull Easing method) {
	}

	@Override
	float getDuration() {
		return fxes.collect { it.duration }.sum() as float
	}

	@Override
	void update(T object, float f, float previous) {
		if (fxes.isEmpty())
			return

		FxResult resultPrevious = getFx(previous)
		FxResult result = getFx(f)

		if (resultPrevious.index != result.index) {
			if (f > previous) {
				for (int i in resultPrevious.index..<result.index) {
					fxes[i].finish(object, 1f)
				}
			} else if (f < previous) {
				for (int i = resultPrevious.index; i > result.index; i--) {
					fxes[i].finish(object, 0f)
				}
			}
		}

		getFx(f).update(object, f, previous)
	}

	@Nonnull private FxResult getFx(float f) {
		float fStart = 0f
		for (int i in 0..<fxes.size()) {
			Fx<T> fx = fxes[i]
			float duration = fx.duration
			if (duration < f + fStart)
				return new FxResult(fx, i, fStart, fStart + fx.duration as float)
			fStart += duration
		}

		int lastIndex = fxes.size() - 1
		Fx<T> last = fxes[lastIndex]
		return new FxResult(last, lastIndex, fStart - last.duration as float, fStart)
	}

	protected final class FxResult {
		final Fx<T> fx
		final int index
		final float a
		final float b

		FxResult(Fx<T> fx, int index, float a, float b) {
			this.fx = fx
			this.index = index
			this.a = a
			this.b = b
		}

		float getFxF(float f) {
			return (f - a) / (b - a) as float
		}

		void update(T object, float f, float previous) {
			f = Math.min(Math.max(getFxF(f), 0f), 1f)
			previous = Math.min(Math.max(getFxF(previous), 0f), 1f)
			fx.update(object, f, previous)
		}

		void finish(T object, float f) {
			f = Math.min(Math.max(getFxF(f), 0f), 1f)
			fx.finish(object, f)
		}
	}
}