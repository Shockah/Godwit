package pl.shockah.godwit.fx;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

import javax.annotation.Nonnull;

import java8.util.Maps;

public final class Animatables {
	@Nonnull static final Map<Animatable, List<FxInstance<? extends Animatable>>> fxes = new WeakHashMap<>();

	static List<FxInstance<? extends Animatable>> getFxInstances(Animatable animatable) {
		return Maps.computeIfAbsent(fxes, animatable, key -> new ArrayList<>());
	}

	private Animatables() {
		throw new UnsupportedOperationException();
	}
}