package pl.shockah.godwit.geom

import com.badlogic.gdx.math.Vector2
import pl.shockah.godwit.ease.Easable
import kotlin.math.atan2
import kotlin.math.sqrt

typealias Vec2 = IVec2<*>

abstract class IVec2<T : IVec2<T>> : Comparable<IVec2<*>>, Easable<IVec2<*>> {
	abstract val x: Float
	abstract val y: Float

	open val lengthSquared: Float
		get() = x * x + y * y

	open val length: Float
		get() = sqrt(lengthSquared)

	open val degrees: Degrees
		get() = ImmutableVec2.ZERO degrees this

	open val radians: Radians
		get() = ImmutableVec2.ZERO radians this

	open val gdx: Vector2
		get() = Vector2(x, y)

	open val normalized: T
		get() {
			val length = length
			@Suppress("UNCHECKED_CAST")
			return when (length) {
				0f -> throw IllegalStateException()
				1f -> this as T
				else -> this / length
			}
		}

	operator fun get(index: Int): Float {
		return when (index) {
			0 -> x
			1 -> y
			else -> throw IllegalArgumentException()
		}
	}

	operator fun component1(): Float {
		return x
	}

	operator fun component2(): Float {
		return y
	}

	open fun immutable(): ImmutableVec2 {
		return ImmutableVec2(x, y)
	}

	fun mutableCopy(): MutableVec2 {
		return MutableVec2(x, y)
	}

	abstract operator fun unaryMinus(): T

	abstract operator fun plus(vector: Vec2): T

	abstract operator fun minus(vector: Vec2): T

	abstract operator fun times(vector: Vec2): T

	abstract operator fun div(vector: Vec2): T

	abstract operator fun plus(scalar: Float): T

	abstract operator fun minus(scalar: Float): T

	abstract operator fun times(scalar: Float): T

	abstract operator fun div(scalar: Float): T

	infix fun degrees(vector: Vec2): Degrees {
		return (this radians vector).degrees
	}

	infix fun radians(vector: Vec2): Radians {
		return atan2((y - vector.y).toDouble(), (vector.x - x).toDouble()).toFloat().radians
	}

	infix fun dot(vector: Vec2): Float {
		return x * vector.x + y * vector.y
	}

	infix fun cross(vector: Vec2): Float {
		return x * vector.y - y * vector.x
	}

	abstract infix fun rotated(angle: Angle): T

	infix fun equals(other: Vec2): Boolean {
		return other.x == x && other.y == y
	}

	infix fun notEquals(other: Vec2): Boolean {
		return other.x != x || other.y != y
	}

	override fun equals(other: Any?): Boolean {
		return other is Vec2 && other.x == x && other.y == y
	}

	override fun hashCode(): Int {
		return x.hashCode() * 31 + y.hashCode()
	}

	override fun toString(): String = "[$x, $y]"

	override fun compareTo(other: IVec2<*>): Int {
		return length.compareTo(other.length)
	}

	override fun ease(other: IVec2<*>, f: Float): IVec2<*> {
		return ImmutableVec2(
				x.ease(other.x, f),
				y.ease(other.y, f)
		)
	}
}