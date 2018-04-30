package pl.shockah.godwit.fx;

import javax.annotation.Nonnull;

import pl.shockah.godwit.Godwit;
import pl.shockah.unicorn.SafeList;

public interface Animatable<T extends Animatable<T>> {
	@SuppressWarnings("unchecked")
	@Nonnull default Animatables.Properties<T> getAnimatableProperties() {
		return Animatables.getAnimatableProperties((T)this);
	}

	@Nonnull default SafeList<FxInstance> getFxInstances() {
		return getAnimatableProperties().fxes;
	}

	default void run(@Nonnull FxInstance instance) {
		getFxInstances().add(instance);
	}

	default void run(@Nonnull Fx fx) {
		getFxInstances().add(fx.instance());
	}

	default void run(@Nonnull Runnable func) {
		getFxInstances().add(new RunnableFx(func).instance());
	}

	default void runDelayed(float delay, @Nonnull Fx fx) {
		run(new SequenceFx(new WaitFx(delay), fx));
	}

	default void runDelayed(float delay, @Nonnull Runnable func) {
		run(new SequenceFx(new WaitFx(delay), new RunnableFx(func)));
	}

	default void updateFx() {
		SafeList<FxInstance> fxes = getFxInstances();
		fxes.update();
		float delta = Godwit.getInstance().getDeltaTime() * getAnimatableProperties().animationSpeed;
		for (FxInstance fx : fxes.get()) {
			fx.updateBy(delta);
			if (fx.isStopped())
				fxes.remove(fx);
		}
	}
}