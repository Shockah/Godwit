package pl.shockah.godwit.geom

import kotlin.math.PI

open class MutableVec2(
		override var x: Float,
		override var y: Float
) : IVec2<MutableVec2>() {
	constructor() : this(0f, 0f)

	companion object {
		operator fun invoke(angle: Angle, length: Float): MutableVec2 {
			val radians = angle.radians + PI.toFloat().radians
			return MutableVec2(-radians.cos * length, -radians.sin * length)
		}
	}

	override var length: Float
		get() = super.length
		set(value) {
			val angle = (degrees + 180f.degrees).radians
			x = -angle.cos * value
			y = -angle.sin * value
		}

	override var degrees: Degrees
		get() = super.degrees
		set(value) {
			val length = length
			val angle = (value + 180f.degrees).radians
			x = -angle.cos * length
			y = -angle.sin * length
		}

	override var radians: Radians
		get() = super.radians
		set(value) {
			val length = length
			val angle = value + PI.toFloat().radians
			x = -angle.cos * length
			y = -angle.sin * length
		}

	val xy: Mutator by lazy { Mutator() }

	operator fun set(index: Int, value: Float) = when (index) {
		0 -> x = value
		1 -> y = value
		else -> throw IllegalArgumentException()
	}

	override operator fun unaryMinus(): MutableVec2 = MutableVec2(-x, -y)

	override operator fun plus(vector: Vec2): MutableVec2 = MutableVec2(x + vector.x, y + vector.y)
	override operator fun minus(vector: Vec2): MutableVec2 = MutableVec2(x - vector.x, y - vector.y)
	override operator fun times(vector: Vec2): MutableVec2 = MutableVec2(x * vector.x, y * vector.y)
	override operator fun div(vector: Vec2): MutableVec2 = MutableVec2(x / vector.x, y / vector.y)

	override operator fun plus(scalar: Float): MutableVec2 = normalized * (length + scalar)
	override operator fun minus(scalar: Float): MutableVec2 = normalized * (length - scalar)
	override operator fun times(scalar: Float): MutableVec2 = MutableVec2(x * scalar, y * scalar)
	override operator fun div(scalar: Float): MutableVec2 = MutableVec2(x / scalar, y / scalar)

	fun set(vector: Vec2) {
		x = vector.x
		y = vector.y
	}

	override fun rotated(angle: Angle): MutableVec2 {
		return MutableVec2(radians + angle.radians, length)
	}

	inner class Mutator {
		operator fun plusAssign(vector: Vec2) {
			x += vector.x
			y += vector.y
		}

		operator fun minusAssign(vector: Vec2) {
			x -= vector.x
			y -= vector.y
		}

		operator fun timesAssign(vector: Vec2) {
			x *= vector.x
			y *= vector.y
		}

		operator fun divAssign(vector: Vec2) {
			x /= vector.x
			y /= vector.y
		}

		operator fun plusAssign(scalar: Float) {
			val result = normalized * (length + scalar)
			x = result.x
			y = result.y
		}

		operator fun minusAssign(scalar: Float) {
			val result = normalized * (length - scalar)
			x = result.x
			y = result.y
		}

		operator fun timesAssign(scalar: Float) {
			x *= scalar
			y *= scalar
		}

		operator fun divAssign(scalar: Float) {
			x /= scalar
			y /= scalar
		}
	}
}