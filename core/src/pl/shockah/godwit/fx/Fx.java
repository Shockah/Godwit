package pl.shockah.godwit.fx;

import javax.annotation.Nonnull;

import pl.shockah.unicorn.ease.Easing;

public interface Fx {
	float getDuration();

	@Nonnull
	Easing getMethod();

	void setMethod(@Nonnull Easing method);

	void update(float f, float previous);

	void finish(float f, float previous);

	@Nonnull
	Fx repeat(int count);

	@Nonnull
	Fx withMethod(@Nonnull Easing method);

	@Nonnull
	Fx additive();

	@Nonnull
	FxInstance instance();

	@Nonnull
	FxInstance instance(@Nonnull FxInstance.EndAction endAction);
}