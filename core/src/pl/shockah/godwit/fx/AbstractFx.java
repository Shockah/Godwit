package pl.shockah.godwit.fx;

import javax.annotation.Nonnull;

import java8.util.stream.Collectors;
import java8.util.stream.IntStreams;
import pl.shockah.unicorn.ease.Easing;

public abstract class AbstractFx implements Fx {
	@Override
	public void finish(float f, float previous) {
		update(f, previous);
	}

	@Nonnull
	@Override
	public Fx repeat(int count) {
		return new SequenceFx(IntStreams.range(0, count)
				.mapToObj(i -> this)
				.collect(Collectors.toList()));
	}

	@Nonnull
	@Override
	public Fx withMethod(@Nonnull Easing method) {
		setMethod(method);
		return this;
	}

	@Nonnull
	@Override
	public Fx additive() {
		return new AdditiveFx(this);
	}

	@Nonnull
	@Override
	public FxInstance instance() {
		return instance(FxInstance.EndAction.End);
	}

	@Nonnull
	@Override
	public FxInstance instance(@Nonnull FxInstance.EndAction endAction) {
		return new FxInstance(this, endAction);
	}
}