package pl.shockah.godwit.gesture;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import pl.shockah.godwit.collection.UnorderedPair;

public class PinchGestureRecognizer extends GestureRecognizer {
	@Nullable public UnorderedPair<Touch> touches = null;

	public PinchGestureRecognizer(@Nonnull GestureHandler handler) {
		super(handler);
	}
}