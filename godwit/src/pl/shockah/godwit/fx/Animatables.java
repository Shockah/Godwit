package pl.shockah.godwit.fx;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

import javax.annotation.Nonnull;

import java8.util.Maps;
import lombok.experimental.UtilityClass;

@UtilityClass
public final class Animatables {
	public static final class Properties<T extends Animatable<T>> {
		@Nonnull public final WeakReference<T> self;
		@Nonnull public final List<FxInstance<? super T>> fxes = new ArrayList<>();
		public float animationSpeed = 1f;

		private Properties(T self) {
			this.self = new WeakReference<>(self);
		}
	}

	@Nonnull private static final Map<Animatable<?>, Properties> properties = new WeakHashMap<>();

	@SuppressWarnings("unchecked")
	public static <T extends Animatable<T>> Properties getAnimatableProperties(T animatable) {
		return Maps.computeIfAbsent(properties, animatable, key -> new Properties<>((T)key));
	}
}