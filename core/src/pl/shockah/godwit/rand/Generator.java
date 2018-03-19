package pl.shockah.godwit.rand;

import javax.annotation.Nonnull;

public interface Generator<T> {
	@Nonnull T generate();
}