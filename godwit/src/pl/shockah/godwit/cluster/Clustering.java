package pl.shockah.godwit.cluster;

import java.util.List;

import javax.annotation.Nonnull;

import pl.shockah.func.Func1;

public abstract class Clustering<T> {
	@Nonnull protected final Func1<T, float[]> toVectorFunc;
	@Nonnull protected final Func1<float[], T> fromVectorFunc;

	public Clustering(@Nonnull Func1<T, float[]> toVectorFunc, @Nonnull Func1<float[], T> fromVectorFunc) {
		this.toVectorFunc = toVectorFunc;
		this.fromVectorFunc = fromVectorFunc;
	}

	@Nonnull public abstract List<T>[] getClusters(@Nonnull List<T> vectors, @Nonnull DistanceAlgorithm algorithm);

	@Nonnull public final List<T>[] getClusters(@Nonnull List<T> vectors) {
		return getClusters(vectors, EuclideanDistanceAlgorithm.instance);
	}
}