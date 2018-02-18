package pl.shockah.godwit.fx.object;

import javax.annotation.Nonnull;

import pl.shockah.func.Action3;

public class ObjectAction3Fx<T> extends ObjectFxImpl<T> {
	@Nonnull
	public final Action3<T, Float, Float> func;

	public ObjectAction3Fx(float duration, @Nonnull Action3<T, Float, Float> func) {
		super(duration);
		this.func = func;
	}

	@Override
	public void update(@Nonnull T object, float f, float previous) {
		func.call(object, f, previous);
	}
}