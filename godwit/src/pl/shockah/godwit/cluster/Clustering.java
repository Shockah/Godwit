package pl.shockah.godwit.cluster;

import java.util.List;

import javax.annotation.Nonnull;

public interface Clustering {
	@Nonnull List<float[]>[] getClusters(@Nonnull List<float[]> vectors, int clusterCount, @Nonnull DistanceAlgorithm algorithm);

	@Nonnull default List<float[]>[] getClusters(@Nonnull List<float[]> vectors, int clusterCount) {
		return getClusters(vectors, clusterCount, EuclideanDistanceAlgorithm.instance);
	}
}