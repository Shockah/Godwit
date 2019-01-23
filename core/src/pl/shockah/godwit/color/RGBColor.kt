package pl.shockah.godwit.color

import com.badlogic.gdx.graphics.Color
import kotlin.math.pow
import kotlin.math.sqrt

data class RGBColor(
		val r: Float,
		val g: Float,
		val b: Float
) : GColor<RGBColor> {
	override val rgb: RGBColor
		get() = copy()

	override fun copy(): RGBColor = RGBColor(r, g, b)

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

	val Color.godwit: RGBColor
		get() = RGBColor(r, g, b)
}