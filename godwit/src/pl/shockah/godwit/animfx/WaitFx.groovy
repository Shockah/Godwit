package pl.shockah.godwit.animfx

import groovy.transform.CompileStatic
import pl.shockah.godwit.animfx.raw.RawFxImpl

@CompileStatic
final class WaitFx extends RawFxImpl {
	WaitFx(float duration) {
		super(duration)
	}

	@Override
	final void update(float f, float previous) {
	}
}