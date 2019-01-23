package pl.shockah.godwit.color

import kotlin.math.pow
import kotlin.math.sqrt

data class RGBColor(
		val r: Float,
		val g: Float,
		val b: Float
) : IGColor<RGBColor>() {
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
}