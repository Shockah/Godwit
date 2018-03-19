package pl.shockah.godwit.fx.object;

import pl.shockah.godwit.fx.FxImpl;

public abstract class ObjectFxImpl<T> extends FxImpl implements ObjectFx<T> {
	public ObjectFxImpl(float duration) {
		super(duration);
	}
}