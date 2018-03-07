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

public class NearestNeighborTravellingSalesmanSolver<T> extends TravellingSalesmanSolver<T> {
	public final boolean skipOnFirst;
	public final float ratio;

	public NearestNeighborTravellingSalesmanSolver(@Nonnull Func1<T, float[]> toVectorFunc) {
		this(toVectorFunc, 0.3f);
	}

	public NearestNeighborTravellingSalesmanSolver(@Nonnull Func1<T, float[]> toVectorFunc, @Nonnull DistanceAlgorithm distanceAlgorithm) {
		this(toVectorFunc, distanceAlgorithm, 0.3f);
	}

	public NearestNeighborTravellingSalesmanSolver(@Nonnull Func1<T, float[]> toVectorFunc, @Nullable Float ratio) {
		this(toVectorFunc, EuclideanDistanceAlgorithm.instance, ratio);
	}

	public NearestNeighborTravellingSalesmanSolver(@Nonnull Func1<T, float[]> toVectorFunc, @Nonnull DistanceAlgorithm distanceAlgorithm, @Nullable Float ratio) {
		super(toVectorFunc, distanceAlgorithm);
		this.ratio = ratio != null ? ratio : 0f;
		skipOnFirst = ratio == null;
	}

	@Nonnull public static NearestNeighborTravellingSalesmanSolver<IVec2> forVec2() {
		return forVec2(0.3f);
	}

	@Nonnull public static NearestNeighborTravellingSalesmanSolver<IVec2> forVec2(@Nonnull DistanceAlgorithm distanceAlgorithm) {
		return forVec2(distanceAlgorithm, 0.3f);
	}

	@Nonnull public static NearestNeighborTravellingSalesmanSolver<IVec2> forVec2(@Nullable Float ratio) {
		return forVec2(EuclideanDistanceAlgorithm.instance, ratio);
	}

	@Nonnull public static NearestNeighborTravellingSalesmanSolver<IVec2> forVec2(@Nonnull DistanceAlgorithm distanceAlgorithm, @Nullable Float ratio) {
		return new NearestNeighborTravellingSalesmanSolver<>(v -> new float[] { v.x(), v.y() }, distanceAlgorithm, ratio);
	}

	@Nonnull public Route solve(@Nonnull Set<T> nodes) {
		SolveInstance instance = new SolveInstance(nodes);
		for (T node : nodes) {
			instance.queue.add(new Route(null, node, 0f));
		}
		instance.solve();
		if (instance.bestRoute == null)
			throw new NullPointerException();
		return (Route)instance.bestRoute;
	}

	protected class SolveInstance extends TravellingSalesmanSolver<T>.SolveInstance {
		@Nonnull public final LinkedList<Route> queue = new LinkedList<>();

		public SolveInstance(@Nonnull Set<T> nodes) {
			super(nodes);
		}

		public void solve() {
			while (!queue.isEmpty()) {
				queue.removeLast().solve(this);
				if (skipOnFirst && bestRoute != null)
					break;
			}
		}
	}

	public class Route extends TravellingSalesmanSolver<T>.Route {
		@Nullable public final Route parent;
		@Nonnull public final T node;

		@Getter
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

		protected boolean solveInternal(@Nonnull SolveInstance instance) {
			List<T> filteredNodes = StreamSupport.stream(instance.nodes)
					.filter(node -> !contains(node))
					.sorted(Comparator.comparingDouble(node -> instance.getDistance(this.node, node)))
					.collect(Collectors.toList());

			if (filteredNodes.isEmpty())
				return false;

			instance.queue.addAll(
					StreamSupport.stream(filteredNodes)
							.limit(Math.max((int)Math.ceil(filteredNodes.size() * ratio), 1))
							.map(node -> new NearestNeighborTravellingSalesmanSolver<T>.Route(this, node, length + instance.getDistance(this.node, node)))
							.collect(Collectors.toList())
			);
			return true;
		}

		protected void solve(@Nonnull SolveInstance instance) {
			if (instance.bestRoute != null && instance.bestRoute.getLength() < length)
				return;

			boolean hadNodesLeft = solveInternal(instance);
			if (!hadNodesLeft) {
				if (instance.bestRoute == null || instance.bestRoute.getLength() > length)
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