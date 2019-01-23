package pl.shockah.godwit.geom

import pl.shockah.godwit.ease.Easable
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
) : Easable<Degrees> {
	companion object {
		@Suppress("DEPRECATION")
		fun of(value: Float): Degrees {
			return Degrees(value.inCycle(-180f, 180f))
		}
	}

	infix fun delta(angle: Degrees): Degrees {
		val r = angle.value - value
		return Degrees.of(r + if (r > 180) -360 else if (r < -180) 360 else 0)
	}

	override fun ease(other: Degrees, f: Float): Degrees {
		val delta = this delta other
		return if (delta.value > 0)
			value.ease(other.value, f).degrees
		else
			(value + 360).ease(other.value, f).degrees
	}

	operator fun plus(other: Degrees): Degrees {
		return (value + other.value).degrees
	}

	operator fun minus(other: Degrees): Degrees {
		return (value - other.value).degrees
	}
}