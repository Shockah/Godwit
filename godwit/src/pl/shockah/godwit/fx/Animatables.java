package pl.shockah.godwit.fx;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

import javax.annotation.Nonnull;

import java8.util.Maps;
import lombok.experimental.UtilityClass;

@UtilityClass
public final class Animatables {
	@Nonnull private static final Map<Animatable, List<FxInstance<? extends Animatable>>> fxes = new WeakHashMap<>();

	@SuppressWarnings("unchecked")
	public static <T extends Animatable<T>> List<FxInstance<? super T>> getFxInstances(T animatable) {
		return (List<FxInstance<? super T>>)(List)Maps.computeIfAbsent(fxes, animatable, key -> new ArrayList<>());
	}
}