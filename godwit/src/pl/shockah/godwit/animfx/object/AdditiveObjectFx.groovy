package pl.shockah.godwit.animfx.object

import groovy.transform.CompileStatic
import pl.shockah.godwit.animfx.AdditiveFx

import javax.annotation.Nonnull

@CompileStatic
class AdditiveObjectFx<T> extends AdditiveFx<ObjectFx<T>> implements ObjectFx<T> {
	AdditiveObjectFx(@Nonnull ObjectFx<T> fx) {
		super(fx)
	}

	@Override
	void update(@Nonnull T object, float f, float previous) {
		((ObjectFx<T>)fx).update(object, getModifiedValue(f, previous), previous)
	}

	@Override
	void finish(@Nonnull T object, float f, float previous) {
		((ObjectFx<T>)fx).finish(object, 0f, previous)
	}
}