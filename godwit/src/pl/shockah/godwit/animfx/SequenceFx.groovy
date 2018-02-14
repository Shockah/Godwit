package pl.shockah.godwit.animfx

import groovy.transform.CompileStatic

import javax.annotation.Nonnull

@CompileStatic
class SequenceFx<T> implements Fx<T> {
	@Nonnull private final List<Fx<? extends T>> fxes

	SequenceFx(Fx<? extends T>... fxes) {
		this(Arrays.asList(fxes))
	}

	SequenceFx(@Nonnull List<Fx<? extends T>> fxes) {
		this.fxes = new ArrayList<>(fxes)
	}

	@Override
	Easing getMethod() {
		return Easing.linear
	}

	@Override
	void setMethod(Easing method) {
	}

	@Override
	float getDuration() {
		return fxes.collect { it.duration }.sum() as float
	}

	@Override
	void update(T object, float f, float previous) {
		if (fxes.isEmpty())
			return

		float duration = this.duration
		f = f * duration
		previous = previous * duration

		int previousIndex = -1

		for (int i in 0..<fxes.size()) {
			float fxDuration = fxes[i].duration

			if (previousIndex == -1) {
				if (previous < fxDuration)
					previousIndex = i
			} else {
				Fx fx = fxes[i]
				fxes[i].update(object, fx.method.ease(1f), fx.method.ease(Math.min(previous, 0f)))
			}

			if (f < fxDuration) {
				Fx fx = fxes[i]
				fxes[i].update(object, fx.method.ease(f), fx.method.ease(Math.max(previous, 0f)))
				break
			}

			f -= fxDuration
			previous -= fxDuration
		}
	}
}