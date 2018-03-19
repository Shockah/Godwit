package pl.shockah.godwit.fx.raw;

import javax.annotation.Nonnull;

import pl.shockah.godwit.fx.AdditiveFx;

public class AdditiveRawFx extends AdditiveFx<RawFx> implements RawFx {
	public AdditiveRawFx(@Nonnull RawFx fx) {
		super(fx);
	}

	@Override
	public void update(float f, float previous) {
		fx.update(getModifiedValue(f, previous), previous);
	}

	@Override
	public void finish(float f, float previous) {
		fx.finish(0f, 0f);
	}
}