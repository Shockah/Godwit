package pl.shockah.godwit.fx.raw;

import javax.annotation.Nonnull;

import pl.shockah.func.Action1;

public class RawAction1Fx extends RawFxImpl {
	@Nonnull public final Action1<Float> func;

	public RawAction1Fx(float duration, @Nonnull Action1<Float> func) {
		super(duration);
		this.func = func;
	}

	@Override
	public void update(float f, float previous) {
		func.call(f);
	}
}