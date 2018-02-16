package pl.shockah.godwit.geom.polygon

import groovy.transform.CompileStatic
import pl.shockah.godwit.geom.IVec2

import javax.annotation.Nonnull

@CompileStatic
interface Triangulator {
	int triangleCount()

	@Nonnull IVec2 trianglePoint(int tri, int i)

	void addPolyPoint(@Nonnull IVec2 point)

	boolean triangulate()
}