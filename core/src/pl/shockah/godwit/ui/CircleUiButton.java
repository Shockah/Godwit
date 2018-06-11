package pl.shockah.godwit.ui;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import pl.shockah.godwit.geom.Circle;
import pl.shockah.godwit.geom.Rectangle;

public abstract class CircleUiButton extends AbstractUiButton<Circle> {
	public float padding = 0f;

	public CircleUiButton(@Nullable Listener listener) {
		super(listener);
	}

	@Nonnull
	public CircleUiButton setPadding(float padding) {
		this.padding = padding;
		return this;
	}

	@Override
	public void updateGestureShape() {
		Rectangle bounds = getBounds();
		gestureShape = new Circle(
				bounds.getCenter(),
				Math.max(bounds.size.x, bounds.size.y) + padding * 2f
		);
	}
}