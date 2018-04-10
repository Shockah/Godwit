package pl.shockah.godwit.gesture;

import javax.annotation.Nullable;

import pl.shockah.godwit.geom.Shape;

public interface GestureHandler {
	@Nullable Shape.Filled getGestureShape();
}