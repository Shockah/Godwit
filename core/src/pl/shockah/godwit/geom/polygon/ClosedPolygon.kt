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

	override fun collides(other: Shape, isSecondTry: Boolean): Boolean {
		for (triangle in triangles) {
			try {
				if (triangle.collides(other, false))
					return true
			} catch (_: Exception) {
			}
		}
		return super<Shape.Filled>.collides(other, isSecondTry)
	}

	infix fun collides(triangle: Triangle): Boolean {
		triangulate()
		for (myTriangle in triangles) {
			if (triangle.collides(myTriangle))
				return true
		}
		return false
	}

	override infix fun collides(line: Line): Boolean {
		triangulate()
		for (myTriangle in triangles) {
			if (line.collides(myTriangle))
				return true
		}
		return false
	}
}