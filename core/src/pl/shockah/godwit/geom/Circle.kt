package pl.shockah.godwit.geom

class Circle(
		position: IVec2<*>,
		var radius: Float
) : Shape.Filled, Shape.Outline {
	var position: MutableVec2 = position.mutableCopy()

	override val boundingBox: Rectangle
		get() = Rectangle(position - Vec2(radius, radius), Vec2(radius * 2, radius * 2))

	override val center: IVec2<*>
		get() = position

	override fun copy(): Circle = Circle(position, radius)

	override fun translate(vector: IVec2<*>) {
		position.x += vector.x
		position.y += vector.y
	}

	override fun mirror(horizontal: Boolean, vertical: Boolean) {
		if (horizontal)
			position.x = -position.x
		if (vertical)
			position.y = -position.y
	}

	override fun scale(scale: Float) {
		position.x *= scale
		position.y *= scale
	}

	override fun contains(point: IVec2<*>): Boolean = (position - point).length <= radius
}