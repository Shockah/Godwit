package pl.shockah.godwit.algo.cluster;

import java.util.List;

import javax.annotation.Nonnull;

import java8.util.stream.RefStreams;
import pl.shockah.func.Func1;
import pl.shockah.godwit.algo.DistanceAlgorithm;
import pl.shockah.godwit.algo.EuclideanDistanceAlgorithm;

public class NearestNeighborKMeansClustering<T> extends KMeansClustering<T> {
	public final float initialThreshold;
	public final float thresholdMultiplier;

	public NearestNeighborKMeansClustering(@Nonnull Func1<T, float[]> toVectorFunc, @Nonnull Func1<float[], T> fromVectorFunc, int clusterCount) {
		this(toVectorFunc, fromVectorFunc, EuclideanDistanceAlgorithm.instance, clusterCount);
	}

	public NearestNeighborKMeansClustering(@Nonnull Func1<T, float[]> toVectorFunc, @Nonnull Func1<float[], T> fromVectorFunc, int clusterCount, float initialThreshold, float thresholdMultiplier) {
		this(toVectorFunc, fromVectorFunc, EuclideanDistanceAlgorithm.instance, clusterCount, initialThreshold, thresholdMultiplier);
	}

	public NearestNeighborKMeansClustering(@Nonnull Func1<T, float[]> toVectorFunc, @Nonnull Func1<float[], T> fromVectorFunc, @Nonnull DistanceAlgorithm distanceAlgorithm, int clusterCount) {
		this(toVectorFunc, fromVectorFunc, distanceAlgorithm, clusterCount, 0.9f, 0.95f);
	}

	public NearestNeighborKMeansClustering(@Nonnull Func1<T, float[]> toVectorFunc, @Nonnull Func1<float[], T> fromVectorFunc, @Nonnull DistanceAlgorithm distanceAlgorithm, int clusterCount, float initialThreshold, float thresholdMultiplier) {
		super(toVectorFunc, fromVectorFunc, distanceAlgorithm, clusterCount);
		this.initialThreshold = initialThreshold;
		this.thresholdMultiplier = thresholdMultiplier;
	}

	@Nonnull protected final T getAverageVector(@Nonnull List<T> vectors) {
		float[] average = new float[toVectorFunc.call(vectors[0]).length];
		for (T vector : vectors) {
			float[] vectorf = toVectorFunc.call(vector);
			for (int n = 0; n < average.length; n++) {
				average[n] += vectorf[n];
			}
		}
		for (int n = 0; n < average.length; n++) {
			average[n] /= vectors.size();
		}
		return fromVectorFunc.call(average);
	}

	@SuppressWarnings("unchecked")
	@Override
	@Nonnull protected T[] getInitialSeeds(@Nonnull List<T> vectors) {
		float threshold = initialThreshold;
		float inversePercentage = 1f;
		while (true) {
			inversePercentage *= 0.5f;
			float percentage = (1f - inversePercentage) * 0.5f;
			setProgress(percentage, String.format("Clustering: K-Means: Getting initial seeds: Nearest Neighbor: %.0f%%", percentage * 100f));
			List<T>[] clusters = new NearestNeighborClustering<>(toVectorFunc, fromVectorFunc, threshold).run(vectors);
			if (clusters.length >= clusterCount) {
				return RefStreams.of(clusters)
						.limit(clusterCount)
						.map(this::getAverageVector)
						.toArray(size -> (T[])new Object[size]);
			}
			threshold *= thresholdMultiplier;
		}
	}
}