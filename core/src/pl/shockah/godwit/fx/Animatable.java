package pl.shockah.godwit.fx;

import javax.annotation.Nonnull;

import pl.shockah.godwit.Godwit;
import pl.shockah.godwit.fx.object.ObjectFx;
import pl.shockah.godwit.fx.object.SequenceObjectFx;
import pl.shockah.godwit.fx.raw.RawFx;
import pl.shockah.godwit.fx.raw.SequenceRawFx;
import pl.shockah.unicorn.SafeList;
import pl.shockah.unicorn.func.Action0;

public interface Animatable<T extends Animatable<T>> {
	@SuppressWarnings("unchecked")
	@Nonnull default Animatables.Properties<T> getAnimatableProperties() {
		return Animatables.getAnimatableProperties((T)this);
	}

	@SuppressWarnings("unchecked")
	@Nonnull default SafeList<FxInstance<? super T>> getFxInstances() {
		return getAnimatableProperties().fxes;
	}

	default void run(@Nonnull FxInstance<T> instance) {
		getFxInstances().add(instance);
	}

	default void run(@Nonnull RawFx fx) {
		getFxInstances().add(fx.instance());
	}

	default void run(@Nonnull ObjectFx<? super T> fx) {
		getFxInstances().add(fx.instance());
	}

	default void runDelayed(float delay, @Nonnull RawFx fx) {
		run(new SequenceRawFx(new WaitFx(delay), fx));
	}

	@SuppressWarnings("unchecked")
	default void runDelayed(float delay, @Nonnull ObjectFx<? super T> fx) {
		run(new SequenceObjectFx<>(new WaitFx(delay).asObject(Object.class), fx));
	}

	default void runDelayed(float delay, @Nonnull Action0 func) {
		run(new SequenceRawFx(new WaitFx(delay), new RunnableFx(func::call)));
	}

	@SuppressWarnings("unchecked")
	default void updateFx() {
		SafeList<FxInstance<? super T>> fxes = getFxInstances();
		fxes.update();
		float delta = Godwit.getInstance().getDeltaTime() * getAnimatableProperties().animationSpeed;
		for (FxInstance<? super T> fx : fxes.get()) {
			fx.updateBy((T)this, delta);
			if (fx.isStopped())
				fxes.remove(fx);
		}
	}
}