package pl.shockah.godwit.cluster;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Nonnull;

import java8.util.stream.RefStreams;

public abstract class KMeansClustering extends Clustering {
	public final int clusterCount;

	public KMeansClustering(int clusterCount) {
		this.clusterCount = clusterCount;
	}

	@Nonnull protected abstract float[][] getInitialSeeds(@Nonnull List<float[]> vectors, @Nonnull DistanceAlgorithm algorithm);

	@SuppressWarnings("unchecked")
	@Override
	@Nonnull public List<float[]>[] getClusters(@Nonnull List<float[]> vectors, @Nonnull DistanceAlgorithm algorithm) {
		List<float[]>[] clusters = (List<float[]>[])Array.newInstance(List.class, clusterCount);
		for (int i = 0; i < clusters.length; i++) {
			clusters[i] = new ArrayList<>();
		}

		float[][] seeds = getInitialSeeds(vectors, algorithm);
		for (int i = 0; i < clusters.length; i++) {
			clusters[i].add(seeds[i]);
		}

		List<float[]>[] previous = null;
		while (true) {
			for (int i = 0; i < clusters.length; i++) {
				seeds[i] = newSeed(seeds[i], clusters[i]);
				clusters[i].clear();
			}
			for (float[] vector : vectors) {
				float smallestDistance = Float.MAX_VALUE;
				int closestClusterIndex = -1;
				for (int i = 0; i < clusters.length; i++) {
					float distance = algorithm.getDistance(vector, seeds[i]);
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
					.toArray(size -> (List<float[]>[])Array.newInstance(List.class, size));
		}

		return clusters;
	}

	private boolean areEqual(@Nonnull List<float[]>[] clusters1, @Nonnull List<float[]>[] clusters2) {
		if (clusters1.length != clusters2.length)
			return false;
		for (int i = 0; i < clusters1.length; i++) {
			if (clusters1[i].size() != clusters2[i].size())
				return false;
			for (int j = 0; j < clusters1[i].size(); j++) {
				if (!Arrays.equals(clusters1[i][j], clusters2[i][j]))
					return false;
			}
		}
		return true;
	}

	@Nonnull private float[] newSeed(@Nonnull float[] oldSeed, @Nonnull List<float[]> vectors) {
		float[] newSeed = new float[oldSeed.length];
		for (float[] vector : vectors) {
			for (int n = 0; n < newSeed.length; n++) {
				newSeed[n] += vector[n];
			}
		}
		for (int n = 0; n < newSeed.length; n++) {
			newSeed[n] /= vectors.size();
		}
		return newSeed;
	}
}