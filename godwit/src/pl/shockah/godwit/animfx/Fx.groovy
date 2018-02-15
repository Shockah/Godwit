package pl.shockah.godwit.animfx

import groovy.transform.CompileStatic

import javax.annotation.Nonnull
import javax.annotation.Nullable

@CompileStatic
trait Fx<T> {
	@Nonnull abstract Easing getMethod()
	abstract void setMethod(@Nonnull Easing method)

	abstract float getDuration()
	abstract void update(@Nullable T object, float f, float previous)

	void finish(@Nullable T object, float f) {
		update(object, f, f)
	}

	Fx repeat(int count) {
		return new RepeatFx(this, count)
	}

	FxInstance instance(T object) {
		return new FxInstance(object, this, FxInstance.EndAction.End)
	}

	FxInstance instance(T object, @Nonnull FxInstance.EndAction endAction) {
		return new FxInstance(object, this, endAction)
	}
}