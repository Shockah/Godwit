package pl.shockah.godwit.cluster;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import javax.annotation.Nonnull;

import pl.shockah.func.Func1;

public class RandomKMeansClustering<T> extends KMeansClustering<T> {
	public RandomKMeansClustering(@Nonnull Func1<T, float[]> toVectorFunc, @Nonnull Func1<float[], T> fromVectorFunc, int clusterCount) {
		super(toVectorFunc, fromVectorFunc, clusterCount);
	}

	@SuppressWarnings("unchecked")
	@Override
	@Nonnull protected T[] getInitialSeeds(@Nonnull List<T> vectors, @Nonnull DistanceAlgorithm algorithm) {
		List<T> shuffled = new ArrayList<>(new HashSet<>(vectors));
		Collections.shuffle(shuffled);
		return shuffled.subList(0, clusterCount).toArray((T[])new Object[clusterCount]);
	}
}