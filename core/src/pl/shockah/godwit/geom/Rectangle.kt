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

	override fun copy(): Rectangle = Rectangle(position, size)

	override fun translate(vector: IVec2<*>) {
		position.x += vector.x
		position.y += vector.y
	}

	override fun mirror(horizontal: Boolean, vertical: Boolean) {
		if (horizontal)
			position.x = -position.x - size.x
		if (vertical)
			position.y = -position.y - size.y
	}

	override fun scale(scale: Float) {
		position.x *= scale
		position.y *= scale
		size.x *= scale
		size.y *= scale
	}

	override fun contains(point: IVec2<*>): Boolean {
		return point.x in position.x..(position.x + size.x) && point.y in position.y..(position.y + size.y)
	}
}