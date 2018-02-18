package pl.shockah.godwit.fx;

import java.util.List;

public interface Animatable<T extends Animatable<T>> {
	@SuppressWarnings("unchecked")
	default List<FxInstance<T>> getFxInstances() {
		return (List<FxInstance<T>>)(List<?>)Animatables.getFxInstances(this);
	}

	@SuppressWarnings("unchecked")
	default void updateFx() {
		List<FxInstance<T>> fxes = getFxInstances();
		for (int i = 0; i < fxes.size(); i++) {
			FxInstance<Animatable<? extends T>> fx = (FxInstance<Animatable<? extends T>>)fxes[i];
			fx.updateDelta(this);
			if (fx.stopped)
				fxes.remove(i--);
		}
	}
}