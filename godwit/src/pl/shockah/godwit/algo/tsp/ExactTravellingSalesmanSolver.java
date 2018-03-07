package pl.shockah.godwit.algo.tsp;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import lombok.Getter;
import pl.shockah.func.Func1;
import pl.shockah.godwit.algo.DistanceAlgorithm;
import pl.shockah.godwit.algo.EuclideanDistanceAlgorithm;
import pl.shockah.godwit.geom.IVec2;

public class ExactTravellingSalesmanSolver<T> extends TravellingSalesmanSolver<T> {
	public ExactTravellingSalesmanSolver(@Nonnull Func1<T, float[]> toVectorFunc) {
		super(toVectorFunc);
	}

	public ExactTravellingSalesmanSolver(@Nonnull Func1<T, float[]> toVectorFunc, @Nonnull DistanceAlgorithm distanceAlgorithm) {
		super(toVectorFunc, distanceAlgorithm);
	}

	@Nonnull public static ExactTravellingSalesmanSolver<IVec2> forVec2() {
		return forVec2(EuclideanDistanceAlgorithm.instance);
	}

	@Nonnull public static ExactTravellingSalesmanSolver<IVec2> forVec2(@Nonnull DistanceAlgorithm distanceAlgorithm) {
		return new ExactTravellingSalesmanSolver<>(v -> new float[] { v.x(), v.y() }, distanceAlgorithm);
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
			boolean hadNodesLeft = false;
			for (T node : instance.nodes) {
				if (contains(node))
					continue;

				new Route(this, node, length + instance.getDistance(this.node, node)).solve(instance);
				hadNodesLeft = true;
			}
			return hadNodesLeft;
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