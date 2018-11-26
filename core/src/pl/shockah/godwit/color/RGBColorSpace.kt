package pl.shockah.godwit.color

import kotlin.math.pow
import kotlin.math.sqrt

data class RGBColorSpace(
		var r: Float,
		var g: Float,
		var b: Float
) : ColorSpace<RGBColorSpace> {
	override val rgb: RGBColorSpace
		get() = copy()

	override fun copy(): RGBColorSpace = RGBColorSpace(r, g, b)

	override fun getDistance(other: RGBColorSpace): Float {
		return sqrt((r - other.r).pow(2) + (g - other.g).pow(2) + (b - other.b).pow(2))
	}

	override fun ease(other: RGBColorSpace, f: Float): RGBColorSpace {
		return RGBColorSpace(
				r.ease(other.r, f),
				g.ease(other.g, f),
				b.ease(other.b, f)
		)
	}
}