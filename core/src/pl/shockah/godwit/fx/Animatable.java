package pl.shockah.godwit.fx;

import java.util.List;

import javax.annotation.Nonnull;

import pl.shockah.func.Action0;
import pl.shockah.godwit.Godwit;
import pl.shockah.godwit.fx.object.ObjectFx;
import pl.shockah.godwit.fx.object.SequenceObjectFx;
import pl.shockah.godwit.fx.raw.RawFx;
import pl.shockah.godwit.fx.raw.SequenceRawFx;

public interface Animatable<T extends Animatable<T>> {
	@SuppressWarnings("unchecked")
	@Nonnull default Animatables.Properties<T> getAnimatableProperties() {
		return Animatables.getAnimatableProperties((T)this);
	}

	@SuppressWarnings("unchecked")
	@Nonnull default List<FxInstance<? super T>> getFxInstances() {
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
		List<FxInstance<? super T>> fxes = getFxInstances();
		float delta = Godwit.getInstance().getDeltaTime() * getAnimatableProperties().animationSpeed;
		for (int i = 0; i < fxes.size(); i++) {
			FxInstance<? super T> fx = fxes[i];
			fx.updateBy((T)this, delta);
			if (fx.isStopped())
				fxes.remove(i--);
		}
	}
}