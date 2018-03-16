package pl.shockah.godwit.algo.cluster;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import javax.annotation.Nonnull;

import pl.shockah.func.Func1;
import pl.shockah.godwit.algo.DistanceAlgorithm;
import pl.shockah.godwit.algo.EuclideanDistanceAlgorithm;

public class RandomKMeansClustering<T> extends KMeansClustering<T> {
	public RandomKMeansClustering(@Nonnull Func1<T, float[]> toVectorFunc, @Nonnull Func1<float[], T> fromVectorFunc, int clusterCount) {
		this(toVectorFunc, fromVectorFunc, EuclideanDistanceAlgorithm.instance, clusterCount);
	}

	public RandomKMeansClustering(@Nonnull Func1<T, float[]> toVectorFunc, @Nonnull Func1<float[], T> fromVectorFunc, @Nonnull DistanceAlgorithm distanceAlgorithm, int clusterCount) {
		super(toVectorFunc, fromVectorFunc, distanceAlgorithm, clusterCount);
	}

	@SuppressWarnings("unchecked")
	@Override
	@Nonnull protected T[] getInitialSeeds(@Nonnull List<T> vectors) {
		setProgress(0f, "Clustering: K-Means: Getting initial seeds: Random...");
		List<T> shuffled = new ArrayList<>(new HashSet<>(vectors));
		Collections.shuffle(shuffled);
		return shuffled.subList(0, clusterCount).toArray((T[])new Object[clusterCount]);
	}
}