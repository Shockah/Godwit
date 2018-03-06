package pl.shockah.godwit.cluster;

import javax.annotation.Nonnull;

public interface DistanceAlgorithm {
	float getDistance(@Nonnull float[] v1, @Nonnull float[] v2);
}