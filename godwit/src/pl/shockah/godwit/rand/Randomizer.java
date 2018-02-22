package pl.shockah.godwit.rand;

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

	public IntGenerator getIntGenerator() {
		return this::nextInt;
	}

	public IntGenerator getIntRangeGenerator(int minimumInclusive, int maximumInclusive) {
		return () -> nextInt(maximumInclusive - minimumInclusive + 1) + minimumInclusive;
	}

	public FloatGenerator getFloatGenerator() {
		return this::nextFloat;
	}

	public FloatGenerator getFloatRangeGenerator(float minimum, float maximum) {
		return () -> nextFloat() * (maximum - minimum) + minimum;
	}

	public FloatGenerator getSquareFloatGenerator() {
		return () -> {
			float baseF = nextFloat() * 2f - 1f;
			float sign = Math.signum(baseF);
			return baseF * baseF * sign;
		};
	}

	public FloatGenerator getSquareFloatRangeGenerator(float minimum, float maximum) {
		final FloatGenerator generator = getSquareFloatGenerator();
		return () -> (generator.generate() + 1f) * 0.5f * (maximum - minimum) + minimum;
	}

	public FloatGenerator getGaussianFloatGenerator() {
		return () -> (float)nextGaussian();
	}

	public FloatGenerator getGaussianFloatRangeGenerator(float minimum, float maximum) {
		final FloatGenerator generator = getGaussianFloatGenerator();
		return () -> (generator.generate() + 1f) * 0.5f * (maximum - minimum) + minimum;
	}
}