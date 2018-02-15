package pl.shockah.godwit.animfx.raw

import groovy.transform.CompileStatic
import pl.shockah.godwit.animfx.Easing

import javax.annotation.Nonnull

@CompileStatic
abstract class RawFxImpl implements RawFx {
	private final float duration
	@Nonnull Easing method = Easing.linear

	RawFxImpl(float duration) {
		this.duration = duration
	}

	float getDuration() {
		return duration
	}
}