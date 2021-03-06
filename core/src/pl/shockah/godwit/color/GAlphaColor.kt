package pl.shockah.godwit.color

import com.badlogic.gdx.graphics.Color

val Color.godwit: GAlphaColor
	get() = GAlphaColor(RGBColor(r, g, b), a)

data class GAlphaColor(
		val color: GColor,
		val alpha: Float = 1f
) {
	val gdx: Color by lazy {
		val rgb = color.rgb
		return@lazy Color(rgb.r, rgb.g, rgb.b, alpha)
	}

	fun with(color: GColor = this.color, alpha: Float = this.alpha): GAlphaColor {
		return GAlphaColor(color, alpha)
	}
}