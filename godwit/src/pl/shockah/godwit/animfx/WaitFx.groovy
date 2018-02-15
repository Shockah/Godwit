package pl.shockah.godwit.animfx

import groovy.transform.CompileStatic

@CompileStatic
final class WaitFx extends RawFx {
	WaitFx(float duration) {
		super(duration)
	}

	@Override
	final void update(float f, float previous) {
	}

	@Override
	final void finish(float f) {
	}
}