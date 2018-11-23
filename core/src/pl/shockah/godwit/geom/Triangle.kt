package pl.shockah.godwit.geom

import pl.shockah.godwit.geom.polygon.ClosedPolygon
import pl.shockah.godwit.geom.polygon.Polygonable

class Triangle(
	point1: IVec2<*>,
	point2: IVec2<*>,
	point3: IVec2<*>
) : Polygonable.Closed {
	val points: List<MutableVec2> = listOf(point1.mutableCopy(), point2.mutableCopy(), point3.mutableCopy())

	override val boundingBox: Rectangle
		get() {
			val minX = minOf(points[0].x, points[1].x, points[2].x)
			val minY = minOf(points[0].y, points[1].y, points[2].y)
			val maxX = maxOf(points[0].x, points[1].x, points[2].x)
			val maxY = maxOf(points[0].y, points[1].y, points[2].y)
			return Rectangle(Vec2(minX, minY), Vec2(maxX - minX, maxY - minY))
		}

	override fun copy(): Triangle = Triangle(points[0], points[1], points[2])

	override fun equals(other: Any?): Boolean {
		return other is Triangle && points == other.points
	}

	override fun hashCode(): Int {
		return points.hashCode()
	}

	override fun translate(vector: IVec2<*>) {
		points.forEach { it.xy += vector }
	}

	override fun mirror(horizontal: Boolean, vertical: Boolean) {
		if (horizontal)
			points.forEach { it.x = -it.x }
		if (vertical)
			points.forEach { it.y = -it.y }
	}

	override fun scale(scale: Float) {
		points.forEach { it.xy *= scale }
	}

	override operator fun contains(point: IVec2<*>): Boolean {
		fun sign(testedPoint: IVec2<*>, point1: IVec2<*>, point2: IVec2<*>): Float {
			return (testedPoint.x - point2.x) * (point1.y - point2.y) - (point1.x - point2.x) * (testedPoint.y - point2.y)
		}

		val b1 = sign(point, points[0], points[1]) < 0f
		val b2 = sign(point, points[1], points[2]) < 0f
		val b3 = sign(point, points[2], points[0]) < 0f
		return b1 == b2 && b2 == b3
	}

	override fun asClosedPolygon(): ClosedPolygon {
		return ClosedPolygon(points)
	}
}