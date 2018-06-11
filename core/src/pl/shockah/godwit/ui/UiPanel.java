package pl.shockah.godwit.ui;

import javax.annotation.Nullable;

import pl.shockah.godwit.geom.Rectangle;

public class UiPanel extends UiControl<Rectangle> {
	@Nullable
	@Override
	public Rectangle getGestureShape() {
		return getBounds();
	}
}