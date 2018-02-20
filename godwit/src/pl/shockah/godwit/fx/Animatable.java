package pl.shockah.godwit.fx;

import java.util.List;

import javax.annotation.Nonnull;

import pl.shockah.godwit.fx.object.ObjectFx;
import pl.shockah.godwit.fx.raw.RawFx;

public interface Animatable<T extends Animatable<T>> {
	@SuppressWarnings("unchecked")
	default List<FxInstance<? super T>> getFxInstances() {
		return Animatables.getFxInstances((T)this);
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
		run(SequenceFx.ofRaw(new WaitFx(delay), fx));
	}

	@SuppressWarnings("unchecked")
	default void runDelayed(float delay, @Nonnull ObjectFx<? super T> fx) {
		run(SequenceFx.ofObject(new WaitFx(delay).asObject(Object.class), fx));
	}

	@SuppressWarnings("unchecked")
	default void updateFx() {
		List<FxInstance<? super T>> fxes = getFxInstances();
		for (int i = 0; i < fxes.size(); i++) {
			FxInstance<? super T> fx = fxes[i];
			fx.updateDelta((T)this);
			if (fx.stopped)
				fxes.remove(i--);
		}
	}
}