package pl.shockah.godwit.fx.object;

import javax.annotation.Nonnull;

import java8.util.stream.Collectors;
import java8.util.stream.IntStreams;
import pl.shockah.godwit.fx.Fx;
import pl.shockah.godwit.fx.FxInstance;
import pl.shockah.unicorn.ease.Easing;

public interface ObjectFx<T> extends Fx {
	void update(@Nonnull T object, float f, float previous);

	default void finish(@Nonnull T object, float f, float previous) {
		update(object, f, previous);
	}

	default ObjectFx<T> repeat(int count) {
		return new SequenceObjectFx<>(IntStreams.range(0, count).mapToObj(i -> this).collect(Collectors.toList()));
	}

	@Nonnull
	default ObjectFx<T> withMethod(@Nonnull Easing method) {
		setMethod(method);
		return this;
	}

	default ObjectFx<T> additive() {
		return new AdditiveObjectFx<>(this);
	}

	default FxInstance<T> instance() {
		return instance(FxInstance.EndAction.End);
	}

	default FxInstance<T> instance(@Nonnull FxInstance.EndAction endAction) {
		return new FxInstance<>(this, endAction);
	}
}