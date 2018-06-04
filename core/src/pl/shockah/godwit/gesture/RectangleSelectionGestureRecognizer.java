package pl.shockah.godwit.gesture;

import javax.annotation.Nonnull;

import pl.shockah.godwit.geom.Rectangle;

public class RectangleSelectionGestureRecognizer extends PanGestureRecognizer {
	@Nonnull
	public final Delegate delegate;

	public RectangleSelectionGestureRecognizer(@Nonnull GestureHandler handler, @Nonnull SimpleDelegate delegate) {
		this(handler, (recognizer, touch, rectangle) -> delegate.onRectangleSelection(recognizer, rectangle));
	}

	public RectangleSelectionGestureRecognizer(@Nonnull GestureHandler handler, @Nonnull Delegate delegate) {
		super(handler, (recognizer, touch, initialPoint, currentPoint, delta) -> {
			float xMin = Math.min(initialPoint.x, currentPoint.x);
			float yMin = Math.min(initialPoint.y, currentPoint.y);
			float xMax = Math.max(initialPoint.x, currentPoint.x);
			float yMax = Math.max(initialPoint.y, currentPoint.y);

			Rectangle rectangle = new Rectangle(xMin, yMin, xMax - xMin, yMax - yMin);
			delegate.onRectangleSelection((RectangleSelectionGestureRecognizer)recognizer, touch, rectangle);
		});
		this.delegate = delegate;
	}

	public interface Delegate {
		void onRectangleSelection(@Nonnull RectangleSelectionGestureRecognizer recognizer, @Nonnull Touch touch, @Nonnull Rectangle rectangle);
	}

	public interface SimpleDelegate {
		void onRectangleSelection(@Nonnull RectangleSelectionGestureRecognizer recognizer, @Nonnull Rectangle rectangle);
	}
}