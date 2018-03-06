package pl.shockah.godwit.cluster;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.annotation.Nonnull;

public class RandomKMeansClustering extends KMeansClustering {
	public RandomKMeansClustering(int clusterCount) {
		super(clusterCount);
	}

	@Override
	@Nonnull protected float[][] getInitialSeeds(@Nonnull List<float[]> vectors, @Nonnull DistanceAlgorithm algorithm) {
		List<float[]> shuffled = new ArrayList<>(vectors);
		Collections.shuffle(shuffled);
		return shuffled.subList(0, clusterCount).toArray(new float[clusterCount][]);
	}
}