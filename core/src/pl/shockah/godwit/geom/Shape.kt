package pl.shockah.godwit.geom

import pl.shockah.godwit.geom.polygon.Polygonable

interface Shape {
	val boundingBox: Rectangle

	val center: IVec2<*>
		get() = boundingBox.center

	fun copy(): Shape

	fun translate(vector: IVec2<*>)
	fun mirror(horizontal: Boolean, vertical: Boolean)
	fun scale(scale: Float)

	infix fun collides(other: Shape): Boolean = collides(other, false)
	fun collides(other: Shape, isSecondTry: Boolean): Boolean {
		return if (isSecondTry) {
			if (this is Polygonable.Closed && other is Polygonable.Closed)
				asClosedPolygon() collides other.asClosedPolygon()
			else if (this is Polygonable.Open && other is Polygonable.Open)
				asPolygon() collides other.asPolygon()
			else
				throw UnsupportedOperationException("${this::class.simpleName} --><-- ${other::class.simpleName} collision isn't implemented.")
		} else {
			other.collides(this, true)
		}
	}

	interface Filled : Shape {
		operator fun contains(point: IVec2<*>): Boolean
	}

	interface Outline : Shape {
	}
}