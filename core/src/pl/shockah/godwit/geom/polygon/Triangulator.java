package pl.shockah.godwit.geom.polygon;

import javax.annotation.Nonnull;

import pl.shockah.godwit.geom.IVec2;

public interface Triangulator {
	int getTriangleCount();

	@Nonnull
	IVec2 getTrianglePoint(int tri, int i);

	void addPoint(@Nonnull IVec2 point);
	
	boolean triangulate();
}