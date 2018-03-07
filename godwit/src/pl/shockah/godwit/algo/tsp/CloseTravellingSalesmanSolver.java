package pl.shockah.godwit.algo.tsp;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import java8.util.stream.Collectors;
import java8.util.stream.StreamSupport;
import lombok.Getter;
import pl.shockah.func.Func1;
import pl.shockah.godwit.algo.DistanceAlgorithm;
import pl.shockah.godwit.algo.EuclideanDistanceAlgorithm;
import pl.shockah.godwit.geom.IVec2;

public class CloseTravellingSalesmanSolver<T> extends TravellingSalesmanSolver<T> {
	public final float ratio;

	public CloseTravellingSalesmanSolver(@Nonnull Func1<T, float[]> toVectorFunc) {
		this(toVectorFunc, 0.3f);
	}

	public CloseTravellingSalesmanSolver(@Nonnull Func1<T, float[]> toVectorFunc, @Nonnull DistanceAlgorithm distanceAlgorithm) {
		this(toVectorFunc, distanceAlgorithm, 0.3f);
	}

	public CloseTravellingSalesmanSolver(@Nonnull Func1<T, float[]> toVectorFunc, float ratio) {
		this(toVectorFunc, EuclideanDistanceAlgorithm.instance, ratio);
	}

	public CloseTravellingSalesmanSolver(@Nonnull Func1<T, float[]> toVectorFunc, @Nonnull DistanceAlgorithm distanceAlgorithm, float ratio) {
		super(toVectorFunc, distanceAlgorithm);
		this.ratio = ratio;
	}

	@Nonnull public static CloseTravellingSalesmanSolver<IVec2> forVec2() {
		return forVec2(0.3f);
	}

	@Nonnull public static CloseTravellingSalesmanSolver<IVec2> forVec2(@Nonnull DistanceAlgorithm distanceAlgorithm) {
		return forVec2(distanceAlgorithm, 0.3f);
	}

	@Nonnull public static CloseTravellingSalesmanSolver<IVec2> forVec2(float ratio) {
		return forVec2(EuclideanDistanceAlgorithm.instance, ratio);
	}

	@Nonnull public static CloseTravellingSalesmanSolver<IVec2> forVec2(@Nonnull DistanceAlgorithm distanceAlgorithm, float ratio) {
		return new CloseTravellingSalesmanSolver<>(v -> new float[] { v.x(), v.y() }, distanceAlgorithm, ratio);
	}

	@Nonnull public Route solve(@Nonnull Set<T> nodes) {
		SolveInstance<Route> instance = new SolveInstance<Route>(nodes);
		for (T node : nodes) {
			new Route(null, node, 0f).solve(instance);
		}
		if (instance.bestRoute == null)
			throw new NullPointerException();
		return instance.bestRoute;
	}

	public class Route extends TravellingSalesmanSolver<T>.Route {
		@Nullable public final Route parent;
		@Nonnull public final T node;
		public final float length;

		@Getter(lazy = true)
		private final List<T> nodes = calculateNodes();

		public Route(@Nullable Route parent, @Nonnull T node, float length) {
			this.parent = parent;
			this.node = node;
			this.length = length;
		}

		protected boolean contains(@Nonnull T node) {
			Route current = this;
			while (current != null) {
				if (current.node.equals(node))
					return true;
				current = current.parent;
			}
			return false;
		}

		protected boolean solveInternal(@Nonnull SolveInstance<Route> instance) {
			List<T> filteredNodes = StreamSupport.stream(instance.nodes)
					.filter(node -> !contains(node))
					.sorted(Comparator.comparingDouble(node -> instance.getDistance(this.node, node)))
					.collect(Collectors.toList());

			if (filteredNodes.isEmpty())
				return false;

			StreamSupport.stream(filteredNodes)
					.limit(Math.max((int)Math.ceil(filteredNodes.size() * ratio), 1))
					.forEach(node -> new CloseTravellingSalesmanSolver<T>.Route(this, node, length + instance.getDistance(this.node, node)).solve(instance));
			return true;
		}

		protected void solve(@Nonnull SolveInstance<Route> instance) {
			if (instance.bestRoute != null && instance.bestRoute.length < length)
				return;

			boolean hadNodesLeft = solveInternal(instance);
			if (!hadNodesLeft) {
				if (instance.bestRoute == null || instance.bestRoute.length > length)
					instance.bestRoute = this;
			}
		}

		@Override
		@Nonnull protected List<T> calculateNodes() {
			LinkedList<T> list = new LinkedList<>();
			Route current = this;
			while (current != null) {
				list.addFirst(current.node);
				current = current.parent;
			}
			return new ArrayList<>(list);
		}
	}
}