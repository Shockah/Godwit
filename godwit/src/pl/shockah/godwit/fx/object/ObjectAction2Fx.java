package pl.shockah.godwit.fx.object;

import javax.annotation.Nonnull;

import pl.shockah.func.Action2;

public class ObjectAction2Fx<T> extends ObjectFxImpl<T> {
	@Nonnull public final Action2<T, Float> func;

	public ObjectAction2Fx(float duration, @Nonnull Action2<T, Float> func) {
		super(duration);
		this.func = func;
	}

	@Override
	public void update(@Nonnull T object, float f, float previous) {
		func.call(object, f);
	}
}