package pl.shockah.godwit.algo.cluster;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

import java8.util.stream.RefStreams;
import pl.shockah.func.Func1;
import pl.shockah.godwit.algo.DistanceAlgorithm;
import pl.shockah.godwit.algo.EuclideanDistanceAlgorithm;

public abstract class KMeansClustering<T> extends Clustering<T> {
	public final int clusterCount;

	public KMeansClustering(@Nonnull Func1<T, float[]> toVectorFunc, @Nonnull Func1<float[], T> fromVectorFunc, int clusterCount) {
		this(toVectorFunc, fromVectorFunc, EuclideanDistanceAlgorithm.instance, clusterCount);
	}

	public KMeansClustering(@Nonnull Func1<T, float[]> toVectorFunc, @Nonnull Func1<float[], T> fromVectorFunc, @Nonnull DistanceAlgorithm distanceAlgorithm, int clusterCount) {
		super(toVectorFunc, fromVectorFunc, distanceAlgorithm);
		this.clusterCount = clusterCount;
	}

	@Nonnull protected abstract T[] getInitialSeeds(@Nonnull List<T> vectors);

	@SuppressWarnings("unchecked")
	@Override
	@Nonnull protected List<T>[] execute(@Nonnull List<T> vectors) {
		List<T>[] clusters = (List<T>[])Array.newInstance(List.class, clusterCount);
		for (int i = 0; i < clusters.length; i++) {
			clusters[i] = new ArrayList<>();
		}

		T[] seeds = getInitialSeeds(vectors);
		for (int i = 0; i < clusters.length; i++) {
			clusters[i].add(seeds[i]);
		}

		List<T>[] previous = null;
		while (true) {
			for (int i = 0; i < clusters.length; i++) {
				seeds[i] = newSeed(seeds[i], clusters[i]);
				clusters[i].clear();
			}
			for (T vector : vectors) {
				float smallestDistance = Float.MAX_VALUE;
				int closestClusterIndex = -1;
				for (int i = 0; i < clusters.length; i++) {
					float distance = distanceAlgorithm.getDistance(toVectorFunc.call(vector), toVectorFunc.call(seeds[i]));
					if (closestClusterIndex == -1 || distance < smallestDistance) {
						smallestDistance = distance;
						closestClusterIndex = i;
					}
				}
				clusters[closestClusterIndex].add(vector);
			}

			if (previous != null && areEqual(previous, clusters))
				break;
			previous = RefStreams.of(clusters)
					.map(ArrayList::new)
					.toArray(size -> (List<T>[])Array.newInstance(List.class, size));
		}

		return clusters;
	}

	private boolean areEqual(@Nonnull List<T>[] clusters1, @Nonnull List<T>[] clusters2) {
		if (clusters1.length != clusters2.length)
			return false;
		for (int i = 0; i < clusters1.length; i++) {
			if (clusters1[i].size() != clusters2[i].size())
				return false;
			for (int j = 0; j < clusters1[i].size(); j++) {
				if (!clusters1[i][j].equals(clusters2[i][j]))
					return false;
			}
		}
		return true;
	}

	@Nonnull private T newSeed(@Nonnull T oldSeed, @Nonnull List<T> vectors) {
		float[] newSeed = new float[toVectorFunc.call(oldSeed).length];
		for (T vector : vectors) {
			float[] vectorf = toVectorFunc.call(vector);
			for (int n = 0; n < newSeed.length; n++) {
				newSeed[n] += vectorf[n];
			}
		}
		for (int n = 0; n < newSeed.length; n++) {
			newSeed[n] /= vectors.size();
		}
		return fromVectorFunc.call(newSeed);
	}
}