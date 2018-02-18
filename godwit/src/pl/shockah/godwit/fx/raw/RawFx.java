package pl.shockah.godwit.fx.raw;

import javax.annotation.Nonnull;

import java8.util.stream.Collectors;
import java8.util.stream.IntStreams;
import pl.shockah.godwit.fx.Fx;
import pl.shockah.godwit.fx.FxInstance;
import pl.shockah.godwit.fx.RawToObjectBridgeFx;
import pl.shockah.godwit.fx.ease.Easing;
import pl.shockah.godwit.fx.object.ObjectFx;

public interface RawFx extends Fx {
	void update(float f, float previous);

	default void finish(float f, float previous) {
		update(f, previous);
	}

	default RawFx repeat(int count) {
		return new SequenceRawFx(IntStreams.range(0, count).mapToObj(i -> this).collect(Collectors.toList()));
	}

	default <T> ObjectFx<T> asObject(Class<T> clazz) {
		return new RawToObjectBridgeFx<>(this);
	}

	@Nonnull default RawFx withMethod(@Nonnull Easing method) {
		setMethod(method);
		return this;
	}

	default RawFx additive() {
		return new AdditiveRawFx(this);
	}

	default <T> FxInstance<T> instance() {
		return instance(FxInstance.EndAction.End);
	}

	default <T> FxInstance<T> instance(@Nonnull FxInstance.EndAction endAction) {
		return new FxInstance<>(this, endAction);
	}
}