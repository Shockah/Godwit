package pl.shockah.godwit.fx.object;

import javax.annotation.Nonnull;

import pl.shockah.godwit.fx.AdditiveFx;

public class AdditiveObjectFx<T> extends AdditiveFx<ObjectFx<T>> implements ObjectFx<T> {
	public AdditiveObjectFx(@Nonnull ObjectFx<T> fx) {
		super(fx);
	}

	@Override
	public void update(@Nonnull T object, float f, float previous) {
		fx.update(object, getModifiedValue(f, previous), previous);
	}

	@Override
	public void finish(@Nonnull T object, float f, float previous) {
		fx.finish(object, 0f, previous);
	}
}