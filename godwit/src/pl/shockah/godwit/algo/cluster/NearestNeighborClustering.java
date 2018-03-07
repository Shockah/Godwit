package pl.shockah.godwit.algo.cluster;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Nonnull;

import java8.util.Maps;
import pl.shockah.func.Func1;
import pl.shockah.godwit.algo.DistanceAlgorithm;
import pl.shockah.godwit.algo.EuclideanDistanceAlgorithm;

public class NearestNeighborClustering<T> extends Clustering<T> {
	public final float threshold;

	public NearestNeighborClustering(@Nonnull Func1<T, float[]> toVectorFunc, @Nonnull Func1<float[], T> fromVectorFunc, float threshold) {
		this(toVectorFunc, fromVectorFunc, EuclideanDistanceAlgorithm.instance, threshold);
	}

	public NearestNeighborClustering(@Nonnull Func1<T, float[]> toVectorFunc, @Nonnull Func1<float[], T> fromVectorFunc, @Nonnull DistanceAlgorithm distanceAlgorithm, float threshold) {
		super(toVectorFunc, fromVectorFunc, distanceAlgorithm);
		this.threshold = threshold;
	}

	@SuppressWarnings("unchecked")
	@Override
	@Nonnull public List<T>[] getClusters(@Nonnull List<T> vectors) {
		List<List<T>> clusters = new ArrayList<>();
		Map<List<T>, Set<T>> distinct = new HashMap<>();

		for (T vector : vectors) {
			float smallestDistance = Float.MAX_VALUE;
			List<T> closestCluster = null;
			for (List<T> cluster : clusters) {
				for (T vector2 : Maps.computeIfAbsent(distinct, cluster, key -> new HashSet<>())) {
					float distance = distanceAlgorithm.getDistance(toVectorFunc.call(vector), toVectorFunc.call(vector2));
					if (closestCluster == null || distance < smallestDistance) {
						closestCluster = cluster;
						smallestDistance = distance;
					}
				}
			}

			if (closestCluster == null || smallestDistance >= threshold) {
				closestCluster = new ArrayList<>();
				clusters.add(closestCluster);
			}
			closestCluster.add(vector);
			Maps.computeIfAbsent(distinct, closestCluster, key -> new HashSet<>()).add(vector);
		}

		return clusters.toArray((List<T>[])Array.newInstance(List.class, clusters.size()));
	}
}