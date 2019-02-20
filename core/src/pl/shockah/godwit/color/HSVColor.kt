package pl.shockah.godwit.color

import pl.shockah.godwit.geom.Angle
import pl.shockah.godwit.geom.degrees
import kotlin.math.pow
import kotlin.math.sqrt

data class HSVColor(
		val h: Angle,
		val s: Float,
		val v: Float
) : IGColor<HSVColor>() {
	companion object {
		fun from(rgb: RGBColor): HSVColor {
			val max = maxOf(rgb.r, rgb.g, rgb.b)
			val min = minOf(rgb.r, rgb.g, rgb.b)
			val range = max - min

			val h: Float
			val s: Float
			val v: Float
			h = when {
				range == 0f -> 0f
				max == rgb.r -> (60 * (rgb.g - rgb.b) / range + 360) % 360
				max == rgb.g -> 60 * (rgb.b - rgb.r) / range + 120
				else -> 60 * (rgb.r - rgb.g) / range + 240
			}

			s = if (max > 0) 1 - min / max else 0f
			v = max

			return HSVColor(h.degrees, s, v)
		}
	}

	override val rgb by lazy {
		var h = this.h.degrees.value
		if (h < 0f)
			h += 360f
		val x = (h / 60f + 6) % 6
		val i = x.toInt()
		val f = x - i
		val p = v * (1 - s)
		val q = v * (1 - s * f)
		val t = v * (1 - s * (1 - f))
		return@lazy when (i) {
			0 -> RGBColor(v, t, p)
			1 -> RGBColor(q, v, p)
			2 -> RGBColor(p, v, t)
			3 -> RGBColor(p, q, v)
			4 -> RGBColor(t, p, v)
			else -> RGBColor(v, p, q)
		}
	}

	override fun getDistance(other: HSVColor): Float {
		val delta = h delta other.h
		return sqrt((delta.degrees.value / 180f).pow(2) + (s - other.s).pow(2) + (v - other.v).pow(2))
	}

	override fun ease(other: HSVColor, f: Float): HSVColor {
		return HSVColor(
				h.ease(other.h, f),
				s.ease(other.s, f),
				v.ease(other.v, f)
		)
	}

	fun with(h: Angle = this.h, s: Float = this.s, v: Float = this.v): HSVColor {
		return HSVColor(h, s, v)
	}
}