package pl.shockah.godwit.animfx

import groovy.transform.CompileStatic

import javax.annotation.Nonnull

@CompileStatic
trait Fx {
	abstract float getDuration()
	@Nonnull abstract Easing getMethod()
	abstract void setMethod(@Nonnull Easing method)

	FxInstance instance() {
		return new FxInstance(this, FxInstance.EndAction.End)
	}

	FxInstance instance(@Nonnull FxInstance.EndAction endAction) {
		return new FxInstance(this, endAction)
	}
}