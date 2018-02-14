package pl.shockah.godwit.animfx

import groovy.transform.CompileStatic

import javax.annotation.Nonnull

@CompileStatic
trait Fx {
	abstract Easing getMethod()
	abstract void setMethod(Easing method)

	abstract float getDuration()
	abstract void update(float f, float previous)

	Fx repeat(int count) {
		return new RepeatFx(this, count)
	}

	FxInstance instance() {
		return new FxInstance(this, FxInstance.EndAction.End)
	}

	FxInstance instance(@Nonnull FxInstance.EndAction endAction) {
		return new FxInstance(this, endAction)
	}
}