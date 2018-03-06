package pl.shockah.godwit.cluster;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Nonnull;

public class NearestNeighborClustering extends Clustering {
	public final float threshold;

	public NearestNeighborClustering(float threshold) {
		this.threshold = threshold;
	}

	@SuppressWarnings("unchecked")
	@Override
	@Nonnull public List<float[]>[] getClusters(@Nonnull List<float[]> vectors, @Nonnull DistanceAlgorithm algorithm) {
		List<List<float[]>> clusters = new ArrayList<>();

		for (float[] vector : vectors) {
			float smallestDistance = Float.MAX_VALUE;
			List<float[]> closestCluster = null;
			for (List<float[]> cluster : clusters) {
				for (float[] vector2 : cluster) {
					float distance = algorithm.getDistance(vector, vector2);
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

		return clusters.toArray((List<float[]>[])Array.newInstance(List.class, clusters.size()));
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