package pl.shockah.godwit.cluster;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Nonnull;

import java8.util.stream.StreamSupport;
import pl.shockah.func.Func1;

public class NearestNeighborClustering<T> extends Clustering<T> {
	public final float threshold;

	public NearestNeighborClustering(@Nonnull Func1<T, float[]> toVectorFunc, @Nonnull Func1<float[], T> fromVectorFunc, float threshold) {
		super(toVectorFunc, fromVectorFunc);
		this.threshold = threshold;
	}

	@SuppressWarnings("unchecked")
	@Override
	@Nonnull public List<T>[] getClusters(@Nonnull List<T> vectors, @Nonnull DistanceAlgorithm algorithm) {
		List<Cluster> clusters = new ArrayList<>();

		for (T vector : vectors) {
			float smallestDistance = Float.MAX_VALUE;
			Cluster closestCluster = null;
			for (Cluster cluster : clusters) {
				for (T vector2 : cluster.distinct) {
					float distance = algorithm.getDistance(toVectorFunc.call(vector), toVectorFunc.call(vector2));
					if (closestCluster == null || distance < smallestDistance) {
						closestCluster = cluster;
						smallestDistance = distance;
					}
				}
			}

			if (closestCluster == null || smallestDistance >= threshold) {
				closestCluster = new Cluster();
				clusters.add(closestCluster);
			}
			closestCluster.vectors.add(vector);
			closestCluster.distinct.add(vector);
		}

		return StreamSupport.stream(clusters)
				.map(cluster -> cluster.vectors)
				.toArray(size -> (List<T>[])Array.newInstance(List.class, size));
	}

	protected class Cluster {
		@Nonnull public final List<T> vectors = new ArrayList<>();
		@Nonnull public final Set<T> distinct = new HashSet<>();
	}
}