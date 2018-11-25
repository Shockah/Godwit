package pl.shockah.godwit.geom

import pl.shockah.godwit.geom.polygon.Polygon
import pl.shockah.godwit.geom.polygon.Polygonable
import kotlin.math.sqrt

class Circle(
		position: IVec2<*>,
		var radius: Float
) : Shape.Filled, Shape.Outline {
	var position: MutableVec2 = position.mutableCopy()

	override val boundingBox: Rectangle
		get() = Rectangle(position - Vec2(radius, radius), Vec2(radius * 2, radius * 2))

	override val center: IVec2<*>
		get() = position

	override fun copy(): Circle = Circle(position, radius)

	override fun equals(other: Any?): Boolean {
		return other is Circle && position == other.position && radius == other.radius
	}

	override fun hashCode(): Int {
		return position.hashCode() * 31 + radius.hashCode()
	}

	override fun translate(vector: IVec2<*>) {
		position.xy += vector
	}

	override fun mirror(horizontal: Boolean, vertical: Boolean) {
		if (horizontal)
			position.x = -position.x
		if (vertical)
			position.y = -position.y
	}

	override fun scale(scale: Float) {
		position.xy *= scale
		radius *= scale
	}

	override operator fun contains(point: IVec2<*>): Boolean {
		return (position - point).length <= radius
	}

	override fun collides(other: Shape, isSecondTry: Boolean): Boolean {
		return when (other) {
			is Circle -> collides(other)
			is Line -> collides(other)
			is Polygon -> collides(other)
			is Polygonable.Open -> collides(other.asPolygon())
			else -> super<Shape.Filled>.collides(other, isSecondTry)
		}
	}

	infix fun collides(circle: Circle): Boolean {
		return (circle.position - position).length < radius + circle.radius
	}

	infix fun collides(line: Line): Boolean {
		return line.point1 in this || line.point2 in this || !(this intersect line).isEmpty()
	}

	infix fun collides(polygon: Polygon): Boolean {
		for (line in polygon.lines) {
			if (this collides line)
				return true
		}
		return false
	}

	infix fun intersect(line: Line): List<Vec2> {
		val baX = line.point2.x - line.point1.x
		val baY = line.point2.y - line.point1.y
		val caX = position.x - line.point1.x
		val caY = position.y - line.point1.y

		val a = baX * baX + baY * baY
		val bBy2 = baX * caX + baY * caY
		val c = caX * caX + caY * caY - radius * radius

		val pBy2 = bBy2 / a
		val q = c / a

		val disc = pBy2 * pBy2 - q
		if (disc < 0)
			return emptyList()

		val tmpSqrt = sqrt(disc)
		val abScalingFactor1 = -pBy2 + tmpSqrt
		val abScalingFactor2 = -pBy2 - tmpSqrt

		val p1 = Vec2(line.point1.x - baX * abScalingFactor1, line.point1.y - baY * abScalingFactor1)
		if (disc == 0f)
			return listOf(p1)

		val p2 = Vec2(line.point1.x - baX * abScalingFactor2, line.point1.y - baY * abScalingFactor2)
		return listOf(p1, p2)
	}
}