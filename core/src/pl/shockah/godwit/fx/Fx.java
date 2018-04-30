package pl.shockah.godwit.fx;

import javax.annotation.Nonnull;

import pl.shockah.unicorn.ease.Easing;

public interface Fx {
	float getDuration();

	@Nonnull Easing getMethod();
	void setMethod(@Nonnull Easing method);
}