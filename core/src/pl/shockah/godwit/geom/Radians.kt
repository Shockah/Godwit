package pl.shockah.godwit.geom

import kotlin.math.*

private fun Float.inCycle(min: Float, max: Float): Float {
	val cycle = max - min
	var new = this - min

	if (new >= cycle)
		new %= cycle
	else if (new < 0)
		new += ceil(-new / cycle) * cycle

	return new + min
}

private const val floatPI = PI.toFloat()

val Float.radians: Radians
	get() = Radians.of(this)

inline class Radians @Deprecated("Use Radians.Companion.of(value: Float) or Float.radians instead") constructor(
		val value: Float
) : IAngle<Radians> {
	companion object {
		val ZERO = Radians.of(0f)

		fun of(value: Float): Radians {
			@Suppress("DEPRECATION")
			return Radians(value.inCycle(-floatPI, floatPI))
		}
	}

	override val radians: Radians
		get() = this

	override val degrees: Degrees
		get() = Math.toDegrees(value.toDouble()).toFloat().degrees

	override val sin: Float
		get() = sin(value)

	override val cos: Float
		get() = cos(value)

	override val tan: Float
		get() = tan(value)

	override infix fun delta(angle: Angle): Radians {
		val r = angle.radians.value - value
		return (r + (if (r > floatPI) -2 * floatPI else if (r < -floatPI) 2 * floatPI else 0f)).radians
	}

	override fun ease(other: Angle, f: Float): Radians {
		val delta = this delta other
		return if (delta.value > 0)
			value.ease(other.radians.value, f).radians
		else
			(value + 360).ease(other.radians.value, f).radians
	}

	override operator fun plus(other: Angle): Radians {
		return (value + other.radians.value).radians
	}

	override operator fun minus(other: Angle): Radians {
		return (value - other.radians.value).radians
	}
}