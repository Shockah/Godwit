package pl.shockah.godwit.cluster;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

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
		List<List<T>> clusters = new ArrayList<>();

		for (T vector : vectors) {
			float smallestDistance = Float.MAX_VALUE;
			List<T> closestCluster = null;
			for (List<T> cluster : clusters) {
				for (T vector2 : cluster) {
					float distance = algorithm.getDistance(toVectorFunc.call(vector), toVectorFunc.call(vector2));
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
		}

		return clusters.toArray((List<T>[])Array.newInstance(List.class, clusters.size()));
	}
}