package pl.shockah.godwit.geom

class Rectangle(
		position: IVec2<*>,
		size: IVec2<*>
) : Shape.Filled, Shape.Outline {
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

	override fun copy(): Rectangle = Rectangle(position, size)

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
}