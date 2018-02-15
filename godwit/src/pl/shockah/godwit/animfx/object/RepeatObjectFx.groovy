package pl.shockah.godwit.animfx.object

import groovy.transform.CompileStatic
import pl.shockah.godwit.animfx.RepeatFx

import javax.annotation.Nonnull

@CompileStatic
class RepeatObjectFx<T> extends RepeatFx<ObjectFx<T>> implements ObjectFx<T> {
	RepeatObjectFx(@Nonnull ObjectFx<T> fx, int count) {
		super(fx, count)
	}

	@Override
	void update(@Nonnull T object, float f, float previous) {
		f = f * count as float
		previous = previous * count as float

		if (((int)f) != ((int)previous))
			((ObjectFx<T>)fx).finish(object, f >= previous ? 1f : 0f, previous % 1f)
		((ObjectFx<T>)fx).update(object, fx.method.ease(f % 1f), previous % 1f)
	}

	@Override
	void finish(@Nonnull T object, float f, float previous) {
		((ObjectFx<T>)fx).finish(object, f >= previous ? 1f : 0f, previous % 1f)
	}
}