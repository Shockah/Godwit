package pl.shockah.godwit.algo.tsp;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import java8.util.Maps;
import lombok.Getter;
import pl.shockah.func.Func1;
import pl.shockah.godwit.UnorderedPair;
import pl.shockah.godwit.algo.DistanceAlgorithm;
import pl.shockah.godwit.algo.EuclideanDistanceAlgorithm;

public abstract class TravellingSalesmanSolver<T> {
	@Nonnull protected final Func1<T, float[]> toVectorFunc;
	@Nonnull protected final DistanceAlgorithm distanceAlgorithm;

	public TravellingSalesmanSolver(@Nonnull Func1<T, float[]> toVectorFunc) {
		this(toVectorFunc, EuclideanDistanceAlgorithm.instance);
	}

	public TravellingSalesmanSolver(@Nonnull Func1<T, float[]> toVectorFunc, @Nonnull DistanceAlgorithm distanceAlgorithm) {
		this.toVectorFunc = toVectorFunc;
		this.distanceAlgorithm = distanceAlgorithm;
	}

	@Nonnull public abstract Route solve(@Nonnull Set<T> nodes);

	protected class SolveInstance<R extends Route> {
		@Nonnull public final Set<T> nodes;
		@Nonnull public final Map<T, float[]> vectorCache = new HashMap<>();
		@Nonnull public final Map<UnorderedPair<T>, Float> distanceCache = new HashMap<>();
		@Nullable public R bestRoute;

		public SolveInstance(@Nonnull Set<T> nodes) {
			this.nodes = nodes;
		}

		public float getDistance(@Nonnull T node1, @Nonnull T node2) {
			return Maps.computeIfAbsent(distanceCache, new UnorderedPair<>(node1, node2), key -> {
				float[] node1Vector = Maps.computeIfAbsent(vectorCache, node1, toVectorFunc::call);
				float[] node2Vector = Maps.computeIfAbsent(vectorCache, node2, toVectorFunc::call);
				return distanceAlgorithm.getDistance(node1Vector, node2Vector);
			});
		}
	}

	public abstract class Route {
		@Getter(lazy = true)
		private final List<T> nodes = calculateNodes();

		@Nonnull protected abstract List<T> calculateNodes();
	}
}