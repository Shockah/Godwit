package pl.shockah.godwit.animfx.object

import groovy.transform.CompileStatic
import pl.shockah.godwit.animfx.FxImpl

@CompileStatic
abstract class ObjectFxImpl<T> extends FxImpl implements ObjectFx<T> {
	ObjectFxImpl(float duration) {
		super(duration)
	}
}