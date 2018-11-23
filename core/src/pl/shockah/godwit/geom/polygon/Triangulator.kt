package pl.shockah.godwit.geom.polygon

import pl.shockah.godwit.geom.IVec2
import pl.shockah.godwit.geom.Triangle

interface Triangulator {
	fun triangulate(points: List<IVec2<*>>): List<Triangle>?
}