package pl.shockah.godwit.animfx

import groovy.transform.CompileStatic

import javax.annotation.Nonnull

@CompileStatic
abstract class FxImpl<T> implements Fx<T> {
	private final float duration
	@Nonnull Easing method = Easing.linear

	FxImpl(float duration) {
		this.duration = duration
	}

	float getDuration() {
		return duration
	}
}