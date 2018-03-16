package pl.shockah.godwit.algo.cluster;

import java.util.HashSet;
import java.util.List;

import javax.annotation.Nonnull;

import java8.util.stream.IntStreams;
import pl.shockah.func.Func1;
import pl.shockah.godwit.algo.DistanceAlgorithm;
import pl.shockah.godwit.algo.EuclideanDistanceAlgorithm;
import pl.shockah.godwit.algo.tsp.NearestNeighborTravellingSalesmanSolver;
import pl.shockah.godwit.algo.tsp.TravellingSalesmanSolver;

public class TravellingSalesmanKMeansClustering<T> extends KMeansClustering<T> {
	@Nonnull public final TravellingSalesmanSolver<T> solver;

	public TravellingSalesmanKMeansClustering(@Nonnull Func1<T, float[]> toVectorFunc, @Nonnull Func1<float[], T> fromVectorFunc, int clusterCount) {
		this(toVectorFunc, fromVectorFunc, EuclideanDistanceAlgorithm.instance, clusterCount);
	}

	public TravellingSalesmanKMeansClustering(@Nonnull Func1<T, float[]> toVectorFunc, @Nonnull Func1<float[], T> fromVectorFunc, int clusterCount, @Nonnull TravellingSalesmanSolver<T> solver) {
		this(toVectorFunc, fromVectorFunc, EuclideanDistanceAlgorithm.instance, clusterCount, solver);
	}

	public TravellingSalesmanKMeansClustering(@Nonnull Func1<T, float[]> toVectorFunc, @Nonnull Func1<float[], T> fromVectorFunc, @Nonnull DistanceAlgorithm distanceAlgorithm, int clusterCount) {
		this(toVectorFunc, fromVectorFunc, distanceAlgorithm, clusterCount, new NearestNeighborTravellingSalesmanSolver<>(toVectorFunc, distanceAlgorithm));
	}

	public TravellingSalesmanKMeansClustering(@Nonnull Func1<T, float[]> toVectorFunc, @Nonnull Func1<float[], T> fromVectorFunc, @Nonnull DistanceAlgorithm distanceAlgorithm, int clusterCount, @Nonnull TravellingSalesmanSolver<T> solver) {
		super(toVectorFunc, fromVectorFunc, distanceAlgorithm, clusterCount);
		this.solver = solver;
	}

	@SuppressWarnings("unchecked")
	@Override
	@Nonnull protected T[] getInitialSeeds(@Nonnull List<T> vectors) {
		setProgress(0f, "Clustering: K-Means: Getting initial seeds: Travelling Salesman...");
		List<T> route = solver.run(new HashSet<>(vectors)).getNodes();
		return IntStreams.range(0, clusterCount).mapToObj(index -> {
			float f = 1f * index / clusterCount;
			return route.get((int)(f * route.size()));
		}).toArray(size -> (T[])new Object[size]);
	}
}