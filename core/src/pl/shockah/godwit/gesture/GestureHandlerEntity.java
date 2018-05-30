package pl.shockah.godwit.gesture;

import java.util.HashSet;
import java.util.Set;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import lombok.Getter;
import pl.shockah.godwit.Entity;
import pl.shockah.godwit.geom.Shape;

public class GestureHandlerEntity<T extends Shape & Shape.Filled> extends Entity implements GestureHandler {
	@Nonnull
	protected final Set<GestureRecognizer> gestureRecognizers = new HashSet<>();

	@Getter
	@Nullable
	public T gestureShape;

	@Override
	public void onAddedToHierarchy() {
		super.onAddedToHierarchy();

		for (GestureRecognizer recognizer : gestureRecognizers) {
			recognizer.register();
		}
	}

	@Override
	public void onRemovedFromHierarchy() {
		super.onRemovedFromHierarchy();

		for (GestureRecognizer recognizer : gestureRecognizers) {
			recognizer.unregister();
		}
	}
}