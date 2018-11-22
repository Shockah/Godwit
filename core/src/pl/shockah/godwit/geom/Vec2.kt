package pl.shockah.godwit.geom

class Vec2(
		override val x: Float,
		override val y: Float
) : IVec2<Vec2>() {
	constructor() : this(0f, 0f)

	companion object {
		val zero: Vec2 = Vec2()

		fun angled(degrees: Degrees, length: Float): Vec2 = Vec2(
				(-Math.cos(Math.toRadians((degrees.value + 180f).toDouble())) * length).toFloat(),
				(-Math.sin(Math.toRadians((degrees.value + 180f).toDouble())) * length).toFloat()
		)
	}

	override fun unaryMinus(): Vec2 = Vec2(-x, -y)

	override fun plus(vector: IVec2<*>): Vec2 = Vec2(x + vector.x, y + vector.y)
	override fun minus(vector: IVec2<*>): Vec2 = Vec2(x - vector.x, y - vector.y)
	override fun times(vector: IVec2<*>): Vec2 = Vec2(x * vector.x, y * vector.y)
	override fun div(vector: IVec2<*>): Vec2 = Vec2(x / vector.x, y / vector.y)

	override fun plus(scalar: Float): Vec2 = normalized * (length + scalar)
	override fun minus(scalar: Float): Vec2 = normalized * (length - scalar)
	override fun times(scalar: Float): Vec2 = Vec2(x * scalar, y * scalar)
	override fun div(scalar: Float): Vec2 = Vec2(x / scalar, y / scalar)
}