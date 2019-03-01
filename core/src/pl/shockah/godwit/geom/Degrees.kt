package pl.shockah.godwit.geom

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

val Float.degrees: Degrees
	get() = Degrees.of(this)

inline class Degrees @Deprecated("Use Degrees.Companion.of(value: Float) or Float.degrees instead") constructor(
		val value: Float
) : IAngle<Degrees> {
	companion object {
		val ZERO = Degrees.of(0f)
		val HALF = Degrees.of(180f)

		fun of(value: Float): Degrees {
			@Suppress("DEPRECATION")
			return Degrees(value.inCycle(-180f, 180f))
		}
	}

	override val degrees: Degrees
		get() = this

	override val radians: Radians
		get() = Math.toRadians(value.toDouble()).toFloat().radians

	override val sin: Float
		get() = radians.sin

	override val cos: Float
		get() = radians.cos

	override val tan: Float
		get() = radians.tan

	override infix fun delta(angle: Angle): Degrees {
		val r = angle.degrees.value - value
		return (r + if (r > 180) -360 else if (r < -180) 360 else 0).degrees
	}

	override fun ease(other: Angle, f: Float): Degrees {
		val delta = this delta other
		return if (delta.value > 0)
			value.ease(other.degrees.value, f).degrees
		else
			(value + 360).ease(other.degrees.value, f).degrees
	}

	override operator fun plus(other: Angle): Degrees {
		return (value + other.degrees.value).degrees
	}

	override operator fun minus(other: Angle): Degrees {
		return (value - other.degrees.value).degrees
	}

	override fun rotated(fullRotations: Float): Degrees {
		return (value + fullRotations * 360f).degrees
	}
}