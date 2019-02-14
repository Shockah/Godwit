package pl.shockah.godwit.geom

import pl.shockah.godwit.ease.Easable
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
) : Easable<Radians> {
	companion object {
		val ZERO = Radians.of(0f)

		fun of(value: Float): Radians {
			@Suppress("DEPRECATION")
			return Radians(value.inCycle(-floatPI, floatPI))
		}
	}

	val degrees: Degrees
		get() = Math.toDegrees(value.toDouble()).toFloat().degrees

	val sin: Float
		get() = sin(value)

	val cos: Float
		get() = cos(value)

	val tan: Float
		get() = tan(value)

	infix fun delta(angle: Radians): Radians {
		val r = angle.value - value
		return (r + (if (r > floatPI) -2 * floatPI else if (r < -floatPI) 2 * floatPI else 0f)).radians
	}

	override fun ease(other: Radians, f: Float): Radians {
		val delta = this delta other
		return if (delta.value > 0)
			value.ease(other.value, f).radians
		else
			(value + 360).ease(other.value, f).radians
	}

	operator fun plus(other: Radians): Radians {
		return (value + other.value).radians
	}

	operator fun minus(other: Radians): Radians {
		return (value - other.value).radians
	}
}