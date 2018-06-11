package pl.shockah.godwit.ui;

import java.util.HashSet;
import java.util.Set;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import pl.shockah.godwit.ConstrainableRenderGroup;
import pl.shockah.godwit.geom.Shape;
import pl.shockah.godwit.gesture.GestureHandler;
import pl.shockah.godwit.gesture.GestureRecognizer;

public abstract class UiControl<S extends Shape.Filled> extends ConstrainableRenderGroup implements GestureHandler {
	@Nonnull
	protected final Set<GestureRecognizer> gestureRecognizers = new HashSet<>();

	@Nullable
	@Override
	public abstract S getGestureShape();

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