package pl.shockah.godwit.color

import kotlin.math.pow
import kotlin.math.sqrt

data class RGBColor(
		val r: Float,
		val g: Float,
		val b: Float
) : IGColor<RGBColor>() {
	companion object {
		val white = RGBColor(1f, 1f, 1f)
		val black = RGBColor(0f, 0f, 0f)
	}

	override val rgb = this

	val hsl: HSLColor by lazy { HSLColor.from(this) }

	val hsv: HSVColor by lazy { HSVColor.from(this) }

	override fun getDistance(other: RGBColor): Float {
		return sqrt((r - other.r).pow(2) + (g - other.g).pow(2) + (b - other.b).pow(2))
	}

	override fun ease(other: RGBColor, f: Float): RGBColor {
		return RGBColor(
				r.ease(other.r, f),
				g.ease(other.g, f),
				b.ease(other.b, f)
		)
	}

	operator fun times(rgb: RGBColor): RGBColor {
		return RGBColor(r * rgb.r, g * rgb.g, b * rgb.b)
	}
}