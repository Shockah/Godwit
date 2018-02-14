package pl.shockah.godwit.animfx

import groovy.transform.CompileStatic

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

	abstract float ease(float f)
}