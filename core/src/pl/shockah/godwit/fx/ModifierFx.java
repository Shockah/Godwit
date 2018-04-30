package pl.shockah.godwit.fx;

import javax.annotation.Nonnull;

import pl.shockah.unicorn.ease.Easing;

public abstract class ModifierFx<F extends Fx> implements Fx {
	@Nonnull public final F fx;

	public ModifierFx(@Nonnull F fx) {
		this.fx = fx;
	}

	@Override
	public float getDuration() {
		return fx.getDuration();
	}

	@Override
	@Nonnull public Easing getMethod() {
		return fx.getMethod();
	}

	@Override
	public void setMethod(@Nonnull Easing method) {
		fx.setMethod(method);
	}

	protected abstract float getModifiedValue(float f, float previous);
}