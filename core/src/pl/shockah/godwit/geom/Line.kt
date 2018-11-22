package pl.shockah.godwit.geom

import kotlin.math.max
import kotlin.math.min

class Line(
		point1: IVec2<*>,
		point2: IVec2<*>
) : Shape.Outline {
	var point1: MutableVec2 = point1.mutableCopy()
	var point2: MutableVec2 = point2.mutableCopy()

	override val boundingBox: Rectangle
		get() {
			val minX = min(point1.x, point2.x)
			val minY = min(point1.y, point2.y)
			val maxX = max(point1.x, point2.x)
			val maxY = max(point1.y, point2.y)
			return Rectangle(Vec2(minX, minY), Vec2(maxX - minX, maxY - minY))
		}

	override val center: IVec2<*>
		get() = (point1 + point2) * 0.5f

	override fun copy(): Line = Line(point1, point2)

	override fun translate(vector: IVec2<*>) {
		point1.x += vector.x
		point1.y += vector.y
		point2.x += vector.x
		point2.y += vector.y
	}

	override fun mirror(horizontal: Boolean, vertical: Boolean) {
		if (horizontal) {
			point1.x = -point1.x
			point2.x = -point2.x
		}
		if (vertical) {
			point1.y = -point1.y
			point2.y = -point2.y
		}
	}

	override fun scale(scale: Float) {
		point1.x *= scale
		point1.y *= scale
		point2.x *= scale
		point2.y *= scale
	}
}