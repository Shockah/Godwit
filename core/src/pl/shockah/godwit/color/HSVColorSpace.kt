package pl.shockah.godwit.color

import pl.shockah.godwit.geom.Degrees
import kotlin.math.pow
import kotlin.math.sqrt

data class HSVColorSpace(
		var h: Float,
		var s: Float,
		var v: Float
) : ColorSpace<HSVColorSpace> {
	companion object {
		fun from(rgb: RGBColorSpace): HSVColorSpace {
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

			return HSVColorSpace(h / 360f, s, v)
		}
	}

	override fun copy(): HSVColorSpace = HSVColorSpace(h, s, v)

	override val rgb: RGBColorSpace
		get() {
			val h = this.h * 360f
			val x = (h / 60f + 6) % 6
			val i = x.toInt()
			val f = x - i
			val p = v * (1 - s)
			val q = v * (1 - s * f)
			val t = v * (1 - s * (1 - f))
			return when (i) {
				0 -> RGBColorSpace(v, t, p)
				1 -> RGBColorSpace(q, v, p)
				2 -> RGBColorSpace(p, v, t)
				3 -> RGBColorSpace(p, q, v)
				4 -> RGBColorSpace(t, p, v)
				else -> RGBColorSpace(v, p, q)
			}
		}

	override fun getDistance(other: HSVColorSpace): Float {
		var delta = Degrees(h * 360f) delta Degrees(other.h * 360f)
		if (delta.value < 0)
			delta = Degrees(delta.value + 360f)
		return sqrt((delta.value / 360f).pow(2) + (s - other.s).pow(2) + (v - other.v).pow(2))
	}

	override fun ease(other: HSVColorSpace, f: Float): HSVColorSpace {
		val delta = Degrees(h * 360f) delta Degrees(other.h * 360f)
		val h2 = if (delta.value >= 0) other.h else other.h - 1f
		var h = this.h.ease(h2, f)
		if (h < 0)
			h += 1f
		if (h > 0)
			h -= 1f

		return HSVColorSpace(
				h,
				s.ease(other.s, f),
				v.ease(other.v, f)
		)
	}
}