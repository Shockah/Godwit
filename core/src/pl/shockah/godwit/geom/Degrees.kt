package pl.shockah.godwit.geom

import pl.shockah.godwit.ease.ease
import kotlin.math.ceil

private fun Float.inCycle(min: Float, max: Float): Float {
	val cycle = max - min
	var new = this - min

	if (new >= cycle)
		new %= cycle
	else if (new < 0)
		new += ceil(-new / cycle) * cycle

	return new + min
}

inline class Degrees @Deprecated("Use Degrees.Companion.of(value: Float) or Float.degrees instead") constructor(
		val value: Float
) : IAngle<Degrees> {
	companion object {
		val ZERO = Degrees()
		val HALF = Degrees.of(180f)

		operator fun invoke(): Degrees {
			return of(0f)
		}

		fun of(value: Float): Degrees {
			@Suppress("DEPRECATION")
			return Degrees(value.inCycle(-180f, 180f))
		}
	}

	override val degrees: Degrees
		get() = this

	override val radians: Radians
		get() = Radians.of(Math.toRadians(value.toDouble()).toFloat())

	override val sin: Float
		get() = radians.sin

	override val cos: Float
		get() = radians.cos

	override val tan: Float
		get() = radians.tan

	override infix fun delta(angle: Angle): Degrees {
		val r = angle.degrees.value - value
		return Degrees.of(r + if (r > 180) -360 else if (r < -180) 360 else 0)
	}

	override fun ease(other: Angle, f: Float): Degrees {
		val delta = this delta other
		return if (delta.value > 0)
			Degrees.of(value.ease(other.degrees.value, f))
		else
			Degrees.of((value + 360).ease(other.degrees.value, f))
	}

	override operator fun plus(other: Angle): Degrees {
		return Degrees.of(value + other.degrees.value)
	}

	override operator fun minus(other: Angle): Degrees {
		return Degrees.of(value - other.degrees.value)
	}

	operator fun plus(degrees: Float): Degrees {
		return Degrees.of(value + degrees)
	}

	operator fun minus(degrees: Float): Degrees {
		return Degrees.of(value - degrees)
	}

	override fun rotated(fullRotations: Float): Degrees {
		return Degrees.of(value + fullRotations * 360f)
	}
}