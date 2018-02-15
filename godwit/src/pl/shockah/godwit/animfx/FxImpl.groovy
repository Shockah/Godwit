package pl.shockah.godwit.animfx

import groovy.transform.CompileStatic
import pl.shockah.godwit.animfx.ease.Easing

import javax.annotation.Nonnull

@CompileStatic
abstract class FxImpl implements Fx {
	private final float duration
	@Nonnull Easing method = Easing.linear

	FxImpl(float duration) {
		this.duration = duration
	}

	float getDuration() {
		return duration
	}
}