package pl.shockah.godwit.geom

interface Shape {
	val boundingBox: Rectangle

	fun copy(): Shape

	fun translate(vector: IVec2<*>)
	fun mirror(horizontal: Boolean, vertical: Boolean)
	fun scale(scale: Float)

	interface Filled : Shape {
	}

	interface Outline : Shape {
	}
}