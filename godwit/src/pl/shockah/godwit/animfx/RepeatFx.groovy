package pl.shockah.godwit.animfx

import groovy.transform.CompileStatic

import javax.annotation.Nonnull

@CompileStatic
class RepeatFx<T> implements Fx<T> {
	@Nonnull private final Fx<? extends T> fx
	private final int count

	RepeatFx(@Nonnull Fx<? extends T> fx, int count) {
		this.fx = fx
		this.count = count
	}

	@Override
	Easing getMethod() {
		return fx.method
	}

	@Override
	void setMethod(Easing method) {
		fx.method = method
	}

	@Override
	float getDuration() {
		return fx.duration * count
	}

	@Override
	void update(T object, float f, float previous) {
		fx.update(object, (f * count) % 1f, (previous * count) % 1f)
	}
}