package pl.shockah.godwit.animfx

import groovy.transform.CompileStatic

import javax.annotation.Nonnull

@CompileStatic
class RepeatFx implements Fx {
	@Nonnull private final Fx fx
	private final int count

	RepeatFx(@Nonnull Fx fx, int count) {
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
	void update(float f, float previous) {
		fx.update((f * count) % 1f, (previous * count) % 1f)
	}
}