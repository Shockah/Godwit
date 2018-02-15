package pl.shockah.godwit.animfx.object

import groovy.transform.CompileStatic
import pl.shockah.godwit.animfx.AdditiveFx
import pl.shockah.godwit.animfx.Easing

import javax.annotation.Nonnull

@CompileStatic
class AdditiveObjectFx<T> extends AdditiveFx<ObjectFx<T>> implements ObjectFx<T> {
	AdditiveObjectFx(@Nonnull ObjectFx<T> fx) {
		super(fx)
	}

	@Override
	float getDuration() {
		return fx.duration
	}

	@Override
	Easing getMethod() {
		return fx.method
	}

	@Override
	void setMethod(@Nonnull Easing method) {
		fx.method = method
	}

	@Override
	void update(@Nonnull T object, float f, float previous) {
		float newf = f - fx.method.ease(previous) as float
		((ObjectFx<T>)fx).update(object, newf, previous)
	}

	@Override
	void finish(@Nonnull T object, float f, float previous) {
		((ObjectFx<T>)fx).finish(object, 0f, previous)
	}
}