package pl.shockah.godwit.geom.polygon

import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import pl.shockah.godwit.ObservableList
import pl.shockah.godwit.ease.Easable
import pl.shockah.godwit.geom.*
import pl.shockah.godwit.swift.guard

open class Polygon(
		points: List<Vec2>
) : Shape.Outline, Easable<Polygon> {
	val points: ObservableList<MutableVec2> = ObservableList(points.map { it.mutableCopy() }.toMutableList())

	constructor(vararg points: Vec2) : this(points.toMutableList())

	override val boundingBox: Rectangle
		get() {
			val (minX, minY, maxX, maxY) = guard(
					points.map { it.x }::min,
					points.map { it.y }::min,
					points.map { it.x }::max,
					points.map { it.y }::max
			) {
				return Rectangle(size = ImmutableVec2.ZERO)
			}
			return Rectangle(vec2(minX, minY), vec2(maxX - minX, maxY - minY))
		}

	open val lines: List<Line>
		get() {
			val result: MutableList<Line> = mutableListOf()
			for (i in 0 until (points.size - 1)) {
				result += Line(points[i], points[i + 1])
			}
			return result
		}

	companion object {
		init {
			Shape.registerCollisionHandler { a: Polygon, b: Polygon ->
				for (aLine in a.lines) {
					for (bLine in b.lines) {
						if (aLine collides bLine)
							return@registerCollisionHandler true
					}
				}
				return@registerCollisionHandler false
			}
			Shape.registerCollisionHandler { polygon: Polygon, line: Line ->
				for (polygonLine in polygon.lines) {
					if (polygonLine collides line)
						return@registerCollisionHandler true
				}
				return@registerCollisionHandler false
			}
		}
	}

	override fun copy(): Polygon {
		return Polygon(points)
	}

	override fun translate(vector: Vec2) {
		points.forEach { it.xy += vector }
	}

	override fun mirror(horizontal: Boolean, vertical: Boolean) {
		if (horizontal)
			points.forEach { it.x *= -1f }
		if (vertical)
			points.forEach { it.y *= -1f }
	}

	override fun scale(scale: Float) {
		points.forEach { it.xy *= scale }
	}

	override fun ease(other: Polygon, f: Float): Polygon {
		if (points.size != other.points.size)
			throw IllegalArgumentException()
		return Polygon(points.mapIndexed { index, point -> point.ease(other.points[index], f) })
	}

	override fun drawOutline(shapes: ShapeRenderer) {
		val vertices = FloatArray(points.size * 2)
		for (i in 0 until points.size) {
			vertices[i * 2] = points[i].x
			vertices[i * 2 + 1] = points[i].y
		}
		shapes.polygon(vertices)
	}
}