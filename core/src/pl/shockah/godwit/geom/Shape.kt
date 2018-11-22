package pl.shockah.godwit.geom

interface Shape {
	val boundingBox: Rectangle
	val center: IVec2<*>

	fun copy(): Shape

	fun translate(vector: IVec2<*>)
	fun mirror(horizontal: Boolean, vertical: Boolean)
	fun scale(scale: Float)

	interface Filled : Shape {
		fun contains(point: IVec2<*>): Boolean
	}

	interface Outline : Shape {
	}
}