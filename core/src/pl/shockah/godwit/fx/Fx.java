package pl.shockah.godwit.fx;

import javax.annotation.Nonnull;

import java8.util.stream.Collectors;
import java8.util.stream.IntStreams;
import pl.shockah.godwit.fx.ease.Easing;

public interface Fx {
	float getDuration();

	@Nonnull Easing getMethod();

	void setMethod(@Nonnull Easing method);

	void update(float f, float previous);

	default void finish(float f, float previous) {
		update(f, previous);
	}

	default Fx repeat(int count) {
		return new SequenceFx(IntStreams.range(0, count).mapToObj(i -> this).collect(Collectors.toList()));
	}

	@Nonnull default Fx withMethod(@Nonnull Easing method) {
		setMethod(method);
		return this;
	}

	default Fx additive() {
		return new AdditiveFx(this);
	}

	default FxInstance instance() {
		return instance(FxInstance.EndAction.End);
	}

	default FxInstance instance(@Nonnull FxInstance.EndAction endAction) {
		return new FxInstance(this, endAction);
	}
}