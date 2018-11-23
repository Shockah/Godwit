package pl.shockah.godwit.geom

import pl.shockah.godwit.geom.polygon.ClosedPolygon
import pl.shockah.godwit.geom.polygon.Polygonable

class Rectangle(
		position: IVec2<*>,
		size: IVec2<*>
) : Polygonable.Closed {
	var position: MutableVec2 = position.mutableCopy()
	var size: MutableVec2 = size.mutableCopy()

	override val boundingBox: Rectangle
		get() = copy()

	override val center: IVec2<*>
		get() = position + size * 0.5f

	val left: Float
		get() = position.x

	val right: Float
		get() = position.x + size.x

	val top: Float
		get() = position.y

	val bottom: Float
		get() = position.y + size.y

	val topLeft: IVec2<*>
		get() = position

	val topRight: IVec2<*>
		get() = Vec2(position.x + size.x, position.y)

	val bottomLeft: IVec2<*>
		get() = Vec2(position.x, position.y + size.y)

	val bottomRight: IVec2<*>
		get() = position + size

	val lines: List<Line>
		get() = listOf(
				Line(topLeft, topRight),
				Line(topRight, bottomRight),
				Line(bottomRight, bottomLeft),
				Line(bottomLeft, topLeft)
		)

	override fun copy(): Rectangle = Rectangle(position, size)

	override fun equals(other: Any?): Boolean {
		return other is Rectangle && other.position == position && other.size == size
	}

	override fun hashCode(): Int {
		return position.hashCode() * 31 + size.hashCode()
	}

	override fun translate(vector: IVec2<*>) {
		position.xy += vector
	}

	override fun mirror(horizontal: Boolean, vertical: Boolean) {
		if (horizontal)
			position.x = -position.x - size.x
		if (vertical)
			position.y = -position.y - size.y
	}

	override fun scale(scale: Float) {
		position.xy *= scale
		size.xy *= scale
	}

	override operator fun contains(point: IVec2<*>): Boolean {
		return point.x in left..right && point.y in top..bottom
	}

	override fun collides(other: Shape, isSecondTry: Boolean): Boolean {
		return when (other) {
			is Rectangle -> collides(other)
			is Line -> collides(other)
			else -> super.collides(other, isSecondTry)
		}
	}

	fun collides(rectangle: Rectangle): Boolean {
		return position.x < rectangle.position.x + rectangle.size.x
				&& position.x + size.x > rectangle.position.x
				&& position.y < rectangle.position.y + rectangle.size.y
				&& position.y + size.y > rectangle.position.y
	}

	fun collides(line: Line): Boolean {
		if (line.point1 in this || line.point2 in this)
			return true
		for (rectangleLine in lines) {
			if (line collides rectangleLine)
				return true
		}
		return false
	}

	override fun asClosedPolygon(): ClosedPolygon {
		return ClosedPolygon(topLeft, topRight, bottomRight, bottomLeft)
	}
}