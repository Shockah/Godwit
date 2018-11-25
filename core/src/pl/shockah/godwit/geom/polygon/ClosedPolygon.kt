package pl.shockah.godwit.geom.polygon

import pl.shockah.godwit.ObservableList
import pl.shockah.godwit.geom.*

class ClosedPolygon(
		points: List<IVec2<*>>
) : Polygon(points), Shape.Filled {
	var triangulator: Triangulator = BasicTriangulator()
	private var dirty: Boolean = false

	private var _triangles: List<Triangle> = listOf()
	val triangles: List<Triangle>
		get() {
			triangulate()
			return _triangles
		}

	override val lines: List<Line>
		get() {
			val result: MutableList<Line> = mutableListOf()
			for (i in 0 until points.size) {
				result += Line(points[i], points[(i + 1) % points.size])
			}
			return result
		}

	constructor(vararg points: IVec2<*>) : this(points.toList())

	init {
		super.points += object : ObservableList.ChangeListener<MutableVec2> {
			override fun onAddedToList(element: MutableVec2) {
				dirty = true
			}

			override fun onRemovedFromList(element: MutableVec2) {
				dirty = true
			}
		}
	}

	private fun triangulate() {
		if (!dirty)
			return
		_triangles = triangulator.triangulate(points) ?: throw IllegalStateException("Cannot triangulate polygon.")
	}

	override fun contains(point: IVec2<*>): Boolean {
		for (triangle in triangles) {
			if (point in triangle)
				return true
		}
		return false
	}

	private companion object Collisions {
		init {
			Shape.registerCollisionHandler(ClosedPolygon::class, ClosedPolygon::class) { a, b ->
				for (aTriangle in a.triangles) {
					for (bTriangle in b.triangles) {
						if (aTriangle collides bTriangle)
							return@registerCollisionHandler true
					}
				}
				return@registerCollisionHandler false
			}
			Shape.registerCollisionHandler(ClosedPolygon::class, Triangle::class) { polygon, triangle ->
				for (polygonTriangle in polygon.triangles) {
					if (triangle collides polygonTriangle)
						return@registerCollisionHandler true
				}
				return@registerCollisionHandler false
			}
			Shape.registerCollisionHandler(ClosedPolygon::class, Line::class) { polygon, line ->
				for (polygonTriangle in polygon.triangles) {
					if (line collides polygonTriangle)
						return@registerCollisionHandler true
				}
				return@registerCollisionHandler false
			}
		}
	}
}