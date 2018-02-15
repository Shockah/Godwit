package pl.shockah.godwit.animfx.object

import groovy.transform.CompileStatic
import pl.shockah.godwit.animfx.Easing

import javax.annotation.Nonnull

@CompileStatic
abstract class ObjectFxImpl<T> implements ObjectFx<T> {
	private final float duration
	@Nonnull Easing method = Easing.linear

	ObjectFxImpl(float duration) {
		this.duration = duration
	}

	float getDuration() {
		return duration
	}
}