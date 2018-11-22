package pl.shockah.godwit.geom

class MutableVec2(
		override var x: Float,
		override var y: Float
) : IVec2<MutableVec2>() {
	constructor() : this(0f, 0f)

	companion object {
		fun angled(degrees: Degrees, length: Float): MutableVec2 = MutableVec2(
				(-Math.cos(Math.toRadians((degrees.value + 180f).toDouble())) * length).toFloat(),
				(-Math.sin(Math.toRadians((degrees.value + 180f).toDouble())) * length).toFloat()
		)
	}

	operator fun set(index: Int, value: Float) = when (index) {
		0 -> x = value
		1 -> y = value
		else -> throw IllegalArgumentException()
	}

	override operator fun unaryMinus(): MutableVec2 = MutableVec2(-x, -y)

	override operator fun plus(vector: IVec2<*>): MutableVec2 = MutableVec2(x + vector.x, y + vector.y)
	override operator fun minus(vector: IVec2<*>): MutableVec2 = MutableVec2(x - vector.x, y - vector.y)
	override operator fun times(vector: IVec2<*>): MutableVec2 = MutableVec2(x * vector.x, y * vector.y)
	override operator fun div(vector: IVec2<*>): MutableVec2 = MutableVec2(x / vector.x, y / vector.y)

	override operator fun plus(scalar: Float): MutableVec2 = normalized * (length + scalar)
	override operator fun minus(scalar: Float): MutableVec2 = normalized * (length - scalar)
	override operator fun times(scalar: Float): MutableVec2 = MutableVec2(x * scalar, y * scalar)
	override operator fun div(scalar: Float): MutableVec2 = MutableVec2(x / scalar, y / scalar)
}