package pl.shockah.godwit.geom.polygon

import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import pl.shockah.godwit.LazyDirty
import pl.shockah.godwit.ObservableList
import pl.shockah.godwit.geom.*

class ClosedPolygon(
		points: List<Vec2>
) : Polygon(points), Shape.Filled {
	var triangulator: Triangulator = BasicTriangulator()

	private val dirtyTriangles = LazyDirty {
		triangulator.triangulate(points) ?: throw IllegalStateException("Cannot triangulate polygon.")
	}
	val triangles: List<Triangle> by dirtyTriangles

	override val lines: List<Line>
		get() {
			val result = mutableListOf<Line>()
			for (i in 0 until points.size) {
				result += Line(points[i], points[(i + 1) % points.size])
			}
			return result
		}

	constructor(vararg points: Vec2) : this(points.toList())

	init {
		super.points.listeners += object : ObservableList.ChangeListener<MutableVec2> {
			override fun onAddedToList(element: MutableVec2) {
				dirtyTriangles.invalidate()
			}

			override fun onRemovedFromList(element: MutableVec2) {
				dirtyTriangles.invalidate()
			}
		}
	}

	companion object {
		init {
			Shape.registerCollisionHandler { a: ClosedPolygon, b: ClosedPolygon ->
				for (aTriangle in a.triangles) {
					for (bTriangle in b.triangles) {
						if (aTriangle collides bTriangle)
							return@registerCollisionHandler true
					}
				}
				return@registerCollisionHandler false
			}
			Shape.registerCollisionHandler { polygon: ClosedPolygon, triangle: Triangle ->
				for (polygonTriangle in polygon.triangles) {
					if (triangle collides polygonTriangle)
						return@registerCollisionHandler true
				}
				return@registerCollisionHandler false
			}
			Shape.registerCollisionHandler { polygon: ClosedPolygon, line: Line ->
				for (polygonTriangle in polygon.triangles) {
					if (line collides polygonTriangle)
						return@registerCollisionHandler true
				}
				return@registerCollisionHandler false
			}
		}
	}

	override fun copy(): ClosedPolygon {
		return ClosedPolygon(points).apply {
			triangulator = this@ClosedPolygon.triangulator
		}
	}

	override fun contains(point: Vec2): Boolean {
		for (triangle in triangles) {
			if (point in triangle)
				return true
		}
		return false
	}

	override fun ease(other: Polygon, f: Float): ClosedPolygon {
		if (other !is ClosedPolygon)
			throw IllegalArgumentException()
		if (points.size != other.points.size)
			throw IllegalArgumentException()
		return ClosedPolygon(points.mapIndexed { index, point -> point.ease(other.points[index], f) })
	}

	private fun draw(shapes: ShapeRenderer) {
		val vertices = FloatArray(points.size * 2)
		for (i in 0 until points.size) {
			vertices[i * 2] = points[i].x
			vertices[i * 2 + 1] = points[i].y
		}
		shapes.polygon(vertices)
	}

	override fun drawFilled(shapes: ShapeRenderer) {
		draw(shapes)
	}

	override fun drawOutline(shapes: ShapeRenderer) {
		draw(shapes)
	}
}