package pl.shockah.godwit.fx;

import java.util.List;

public interface Animatable<T extends Animatable<T>> {
	@SuppressWarnings("unchecked")
	default List<FxInstance<T>> getFxInstances() {
		return Animatables.getFxInstances((T)this);
	}

	@SuppressWarnings("unchecked")
	default void updateFx() {
		List<FxInstance<T>> fxes = getFxInstances();
		for (int i = 0; i < fxes.size(); i++) {
			FxInstance<T> fx = fxes[i];
			fx.updateDelta((T)this);
			if (fx.stopped)
				fxes.remove(i--);
		}
	}
}