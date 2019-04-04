package pl.shockah.godwit.geom

import pl.shockah.godwit.ease.ease
import pl.shockah.godwit.inCycle

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

		fun of(value: Double): Degrees {
			@Suppress("DEPRECATION")
			return Degrees(value.inCycle(-180.0, 180.0).toFloat())
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
			Degrees.of(f.ease(value, other.degrees.value))
		else
			Degrees.of(f.ease(value + 360, other.degrees.value))
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