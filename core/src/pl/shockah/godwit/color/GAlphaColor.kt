package pl.shockah.godwit.color

import com.badlogic.gdx.graphics.Color

data class GAlphaColor(
		val color: GColor,
		val alpha: Float
) {
	val gdx: Color by lazy {
		val rgb = color.rgb
		return@lazy Color(rgb.r, rgb.g, rgb.b, alpha)
	}

	val Color.godwit: GAlphaColor
		get() = GAlphaColor(RGBColor(r, g, b), a)
}