package pl.shockah.godwit.fx;

import javax.annotation.Nonnull;

public class AdditiveFx extends ModifierFx {
	public AdditiveFx(@Nonnull Fx fx) {
		super(fx);
	}

	@Override
	protected float getModifiedValue(float f, float previous) {
		return f - previous;
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