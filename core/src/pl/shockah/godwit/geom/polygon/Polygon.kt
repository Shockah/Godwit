package pl.shockah.godwit.geom.polygon

import pl.shockah.godwit.ObservableList
import pl.shockah.godwit.geom.*

open class Polygon(
		points: List<IVec2<*>>
) : Shape.Outline {
	val points: ObservableList<MutableVec2> = ObservableList(points.map { it.mutableCopy() }.toMutableList())

	constructor(vararg points: IVec2<*>) : this(points.toMutableList())

	override val boundingBox: Rectangle
		get() {
			if (points.isEmpty())
				return Rectangle(Vec2.zero, Vec2.zero)
			val minX = points.map { it.x }.min()!!
			val minY = points.map { it.y }.min()!!
			val maxX = points.map { it.x }.max()!!
			val maxY = points.map { it.y }.max()!!
			return Rectangle(Vec2(minX, minY), Vec2(maxX - minX, maxY - minY))
		}

	override fun copy(): Polygon = Polygon(points)

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
}