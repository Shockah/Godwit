package pl.shockah.godwit.geom.polygon

import groovy.transform.CompileStatic
import pl.shockah.godwit.geom.Vec2

import javax.annotation.Nonnull

@CompileStatic
class Extensions {
	@Nonnull static Triangulator leftShift(@Nonnull Triangulator self, @Nonnull Vec2 point) {
		self.addPolyPoint(point)
		return self
	}
}