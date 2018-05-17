package pl.shockah.godwit.fx;

import javax.annotation.Nonnull;

import pl.shockah.unicorn.collection.SafeList;

public interface Animatable {
	float getAnimationSpeed();

	void setAnimationSpeed(float animationSpeed);

	@Nonnull
	SafeList<FxInstance> getFxInstances();

	void run(@Nonnull FxInstance instance);

	void run(@Nonnull Fx fx);

	void run(@Nonnull Runnable func);

	void runDelayed(float delay, @Nonnull Fx fx);

	void runDelayed(float delay, @Nonnull Runnable func);

	void updateFx();
}