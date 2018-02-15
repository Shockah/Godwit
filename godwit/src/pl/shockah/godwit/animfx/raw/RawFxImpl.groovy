package pl.shockah.godwit.animfx.raw

import groovy.transform.CompileStatic
import pl.shockah.godwit.animfx.FxImpl

@CompileStatic
abstract class RawFxImpl extends FxImpl implements RawFx {
	RawFxImpl(float duration) {
		super(duration)
	}
}