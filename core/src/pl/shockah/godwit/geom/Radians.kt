package pl.shockah.godwit.geom

import pl.shockah.godwit.ease.ease
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

inline class Radians @Deprecated("Use Radians.Companion.of(value: Float) or Float.radians instead") constructor(
		val value: Float
) : IAngle<Radians> {
	companion object {
		val ZERO = Radians()
		val HALF = Radians.of(floatPI)

		operator fun invoke(): Radians {
			return of(0f)
		}

		fun of(value: Float): Radians {
			@Suppress("DEPRECATION")
			return Radians(value.inCycle(-floatPI, floatPI))
		}
	}

	override val radians: Radians
		get() = this

	override val degrees: Degrees
		get() = Degrees.of(Math.toDegrees(value.toDouble()).toFloat())

	override val sin: Float
		get() = sin(value)

	override val cos: Float
		get() = cos(value)

	override val tan: Float
		get() = tan(value)

	override infix fun delta(angle: Angle): Radians {
		val r = angle.radians.value - value
		return Radians.of(r + (if (r > floatPI) -2 * floatPI else if (r < -floatPI) 2 * floatPI else 0f))
	}

	override fun ease(other: Angle, f: Float): Radians {
		val delta = this delta other
		return if (delta.value > 0)
			Radians.of(value.ease(other.radians.value, f))
		else
			Radians.of((value + 360).ease(other.radians.value, f))
	}

	override operator fun plus(other: Angle): Radians {
		return Radians.of(value + other.radians.value)
	}

	override operator fun minus(other: Angle): Radians {
		return Radians.of(value - other.radians.value)
	}

	operator fun plus(radians: Float): Radians {
		return Radians.of(value + radians)
	}

	operator fun minus(radians: Float): Radians {
		return Radians.of(value - radians)
	}

	override fun rotated(fullRotations: Float): Radians {
		return Radians.of(value + fullRotations * floatPI * 2f)
	}
}