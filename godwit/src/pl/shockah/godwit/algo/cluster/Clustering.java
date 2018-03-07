package pl.shockah.godwit.algo.cluster;

import java.util.List;

import javax.annotation.Nonnull;

import pl.shockah.func.Func1;
import pl.shockah.godwit.algo.DistanceAlgorithm;
import pl.shockah.godwit.algo.EuclideanDistanceAlgorithm;

public abstract class Clustering<T> {
	@Nonnull protected final Func1<T, float[]> toVectorFunc;
	@Nonnull protected final Func1<float[], T> fromVectorFunc;
	@Nonnull protected final DistanceAlgorithm distanceAlgorithm;

	public Clustering(@Nonnull Func1<T, float[]> toVectorFunc, @Nonnull Func1<float[], T> fromVectorFunc) {
		this(toVectorFunc, fromVectorFunc, EuclideanDistanceAlgorithm.instance);
	}

	public Clustering(@Nonnull Func1<T, float[]> toVectorFunc, @Nonnull Func1<float[], T> fromVectorFunc, @Nonnull DistanceAlgorithm distanceAlgorithm) {
		this.toVectorFunc = toVectorFunc;
		this.fromVectorFunc = fromVectorFunc;
		this.distanceAlgorithm = distanceAlgorithm;
	}

	@Nonnull public abstract List<T>[] getClusters(@Nonnull List<T> vectors);
}