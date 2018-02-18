package pl.shockah.godwit.fx.raw;

import javax.annotation.Nonnull;

import pl.shockah.func.Action2;

public class RawAction2Fx extends RawFxImpl {
	@Nonnull
	public final Action2<Float, Float> func;

	public RawAction2Fx(float duration, @Nonnull Action2<Float, Float> func) {
		super(duration);
		this.func = func;
	}

	@Override
	public void update(float f, float previous) {
		func.call(f, previous);
	}
}