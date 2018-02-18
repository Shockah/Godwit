package pl.shockah.godwit.geom.polygon;

import javax.annotation.Nonnull;

public interface Polygonable {
	@Nonnull Polygon asPolygon();
}