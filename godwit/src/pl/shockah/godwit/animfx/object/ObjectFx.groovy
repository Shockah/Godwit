package pl.shockah.godwit.animfx.object

import groovy.transform.CompileStatic
import pl.shockah.godwit.animfx.ease.Easing
import pl.shockah.godwit.animfx.Fx

import javax.annotation.Nonnull

@CompileStatic
trait ObjectFx<T> extends Fx {
	abstract void update(@Nonnull T object, float f, float previous)

	void finish(@Nonnull T object, float f, float previous) {
		update(object, f, previous)
	}

	ObjectFx<T> repeat(int count) {
		return new SequenceObjectFx<T>((1..count).collect { this } as List<ObjectFx<T>>)
	}

	ObjectFx<T> withMethod(@Nonnull Easing method) {
		this.method = method
		return this
	}

	ObjectFx<T> additive() {
		return new AdditiveObjectFx<T>(this)
	}
}