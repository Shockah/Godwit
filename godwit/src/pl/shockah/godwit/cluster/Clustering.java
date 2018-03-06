package pl.shockah.godwit.cluster;

import java.util.List;

import javax.annotation.Nonnull;

public abstract class Clustering {
	@Nonnull public abstract List<float[]>[] getClusters(@Nonnull List<float[]> vectors, @Nonnull DistanceAlgorithm algorithm);

	@Nonnull public final List<float[]>[] getClusters(@Nonnull List<float[]> vectors) {
		return getClusters(vectors, EuclideanDistanceAlgorithm.instance);
	}
}