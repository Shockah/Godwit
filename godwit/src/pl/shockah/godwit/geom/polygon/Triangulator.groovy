package pl.shockah.godwit.geom.polygon

import groovy.transform.CompileStatic
import pl.shockah.godwit.geom.Vec2

import javax.annotation.Nonnull

@CompileStatic
interface Triangulator {
	int triangleCount()

	@Nonnull Vec2 trianglePoint(int tri, int i)

	void addPolyPoint(@Nonnull Vec2 point)

	boolean triangulate()
}