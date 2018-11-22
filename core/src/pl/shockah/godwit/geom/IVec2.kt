package pl.shockah.godwit.geom

abstract class IVec2<T : IVec2<T>> : Comparable<IVec2<T>> {
	abstract val x: Float
	abstract val y: Float

	val lengthSquared: Float
		get() = x * x + y * y

	val length: Float
		get() = Math.sqrt(lengthSquared.toDouble()).toFloat()

	val angle: Degrees
		get() = Vec2.zero.getAngle(this)

	@Suppress("UNCHECKED_CAST")
	val normalized: T
		get() {
			val length = length
			return when (length) {
				0f -> throw IllegalStateException()
				1f -> this as T
				else -> this / length
			}
		}

	operator fun get(index: Int): Float = when (index) {
		0 -> x
		1 -> y
		else -> throw IllegalArgumentException()
	}

	fun immutableCopy(): Vec2 = Vec2(x, y)
	fun mutableCopy(): MutableVec2 = MutableVec2(x, y)

	abstract operator fun unaryMinus(): T

	abstract operator fun plus(vector: IVec2<*>): T
	abstract operator fun minus(vector: IVec2<*>): T
	abstract operator fun times(vector: IVec2<*>): T
	abstract operator fun div(vector: IVec2<*>): T

	abstract operator fun plus(scalar: Float): T
	abstract operator fun minus(scalar: Float): T
	abstract operator fun times(scalar: Float): T
	abstract operator fun div(scalar: Float): T

	fun getAngle(vector: IVec2<*>): Degrees {
		return Degrees(Math.toDegrees(Math.atan2((y - vector.y).toDouble(), (vector.x - x).toDouble())).toFloat())
	}

	override fun equals(other: Any?): Boolean {
		return other is IVec2<*> && other.x == x && other.y == y
	}

	override fun hashCode(): Int = x.hashCode() * 31 + y.hashCode()

	override fun toString(): String = "[IVec2(x: $x, y: $y)]"

	override fun compareTo(other: IVec2<T>): Int {
		return length.compareTo(other.length)
	}
}