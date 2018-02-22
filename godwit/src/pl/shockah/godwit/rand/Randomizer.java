package pl.shockah.godwit.rand;

import java.util.List;
import java.util.Random;

import javax.annotation.Nonnull;

import lombok.experimental.Delegate;

public class Randomizer {
	@Delegate
	@Nonnull public final Random source;

	public Randomizer() {
		this(new Random());
	}

	public Randomizer(@Nonnull Random source) {
		this.source = source;
	}

	@Nonnull public IntGenerator getIntGenerator() {
		return this::nextInt;
	}

	@Nonnull public IntGenerator getIntRangeGenerator(int minimumInclusive, int maximumInclusive) {
		return () -> nextInt(maximumInclusive - minimumInclusive + 1) + minimumInclusive;
	}

	@Nonnull public FloatGenerator getFloatGenerator() {
		return this::nextFloat;
	}

	@Nonnull public FloatGenerator getFloatRangeGenerator(float minimum, float maximum) {
		return () -> nextFloat() * (maximum - minimum) + minimum;
	}

	@Nonnull public FloatGenerator getSquareFloatGenerator() {
		return () -> {
			float baseF = nextFloat() * 2f - 1f;
			float sign = Math.signum(baseF);
			return baseF * baseF * sign;
		};
	}

	@Nonnull public FloatGenerator getSquareFloatRangeGenerator(float minimum, float maximum) {
		final FloatGenerator generator = getSquareFloatGenerator();
		return () -> (generator.generate() + 1f) * 0.5f * (maximum - minimum) + minimum;
	}

	@Nonnull public FloatGenerator getGaussianFloatGenerator() {
		return () -> (float)nextGaussian();
	}

	@Nonnull public FloatGenerator getGaussianFloatRangeGenerator(float minimum, float maximum) {
		final FloatGenerator generator = getGaussianFloatGenerator();
		return () -> (generator.generate() + 1f) * 0.5f * (maximum - minimum) + minimum;
	}

	@Nonnull public <T> Generator<T> getGenerator(List<T> list) {
		return () -> getRandom(list);
	}

	@SafeVarargs
	@Nonnull public final <T> Generator<T> getGenerator(@Nonnull T... array) {
		return () -> getRandom(array);
	}

	@Nonnull public final <T> T getRandom(List<T> list) {
		return list.get(source.nextInt(list.size()));
	}

	@SafeVarargs
	@Nonnull public final <T> T getRandom(@Nonnull T... array) {
		return array[source.nextInt(array.length)];
	}
}