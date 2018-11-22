package pl.shockah.godwit.geom

class Rectangle(
		var position: MutableVec2,
		var size: MutableVec2
) : Shape.Filled, Shape.Outline {
	override val boundingBox: Rectangle
		get() = copy()

	val center: IVec2<*>
		get() = position + size * 0.5f

	override fun copy(): Rectangle = Rectangle(position.copy(), size.copy())

	override fun translate(vector: IVec2<*>) {
		position.x += vector.x
		position.y += vector.y
	}

	override fun mirror(horizontal: Boolean, vertical: Boolean) {
		if (horizontal)
			position.x = -position.x - size.x;
		if (vertical)
			position.y = -position.y - size.y;
	}

	override fun scale(scale: Float) {
		position.x *= scale
		position.y *= scale
		size.x *= scale
		size.y *= scale
	}

}