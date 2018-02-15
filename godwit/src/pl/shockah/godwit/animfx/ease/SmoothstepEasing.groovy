package pl.shockah.godwit.animfx.ease

import groovy.transform.CompileStatic

@CompileStatic
class SmoothstepEasing extends Easing {
	static final SmoothstepEasing smoothstep = new SmoothstepEasing()
	static final SmoothstepEasing smoothstep2 = new SmoothstepEasing() {
		@Override
		float ease(float f) {
			return super.ease(f) ** 2 as float
		}
	}
	static final SmoothstepEasing smoothstep3 = new SmoothstepEasing() {
		@Override
		float ease(float f) {
			return super.ease(f) ** 3 as float
		}
	}

	@Override
	float ease(float f) {
		return f ** 2 * (3f - 2f * f)
	}
}