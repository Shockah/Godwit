package pl.shockah.godwit.cluster;

import java.util.List;

import javax.annotation.Nonnull;

import java8.util.stream.RefStreams;

public class NearestNeighborKMeansClustering extends KMeansClustering {
	public final float initialThreshold;
	public final float thresholdMultiplier;

	public NearestNeighborKMeansClustering(int clusterCount) {
		this(clusterCount, 0.9f, 0.95f);
	}

	public NearestNeighborKMeansClustering(int clusterCount, float initialThreshold, float thresholdMultiplier) {
		super(clusterCount);
		this.initialThreshold = initialThreshold;
		this.thresholdMultiplier = thresholdMultiplier;
	}

	@Nonnull protected final float[] getAverageVector(@Nonnull List<float[]> vectors) {
		float[] average = new float[vectors[0].length];
		for (float[] vector : vectors) {
			for (int n = 0; n < average.length; n++) {
				average[n] += vector[n];
			}
		}
		for (int n = 0; n < average.length; n++) {
			average[n] /= vectors.size();
		}
		return average;
	}

	@Override
	@Nonnull protected float[][] getInitialSeeds(@Nonnull List<float[]> vectors, @Nonnull DistanceAlgorithm algorithm) {
		float threshold = initialThreshold;
		while (true) {
			List<float[]>[] clusters = new NearestNeighborClustering(threshold).getClusters(vectors, algorithm);
			if (clusters.length >= clusterCount) {
				return RefStreams.of(clusters)
						.limit(clusterCount)
						.map(this::getAverageVector)
						.toArray(float[][]::new);
			}
			threshold *= thresholdMultiplier;
		}
	}
}