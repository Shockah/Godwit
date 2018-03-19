package pl.shockah.godwit.fx;

import javax.annotation.Nonnull;

public abstract class AdditiveFx<F extends Fx> extends ModifierFx<F> {
	public AdditiveFx(@Nonnull F fx) {
		super(fx);
	}

	@Override
	protected float getModifiedValue(float f, float previous) {
		return f - previous;
	}
}