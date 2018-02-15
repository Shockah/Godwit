package pl.shockah.godwit.animfx.ease

import groovy.transform.CompileStatic

import javax.annotation.Nonnull

@CompileStatic
abstract class Easing {
	static final Easing linear = new Easing() {
		@Override
		float ease(float f) {
			return f
		}
	}

	final float ease(float a, float b, float f) {
		return a + ease(f) * (b - a)
	}

	final <T extends Easable<T>> T ease(@Nonnull T a, @Nonnull T b, float f) {
		return a.ease(b, ease(f))
	}

	abstract float ease(float f)
}