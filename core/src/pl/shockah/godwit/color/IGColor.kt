package pl.shockah.godwit.color

import com.badlogic.gdx.graphics.Color
import pl.shockah.godwit.ease.Easable

typealias GColor = IGColor<*>

abstract class IGColor<CS : IGColor<CS>> : Easable<CS> {
	abstract val rgb: RGBColor

	val gdx: Color by lazy {
		val rgb = this.rgb
		return@lazy Color(rgb.r, rgb.g, rgb.b, 1f)
	}

	abstract fun getDistance(other: CS): Float

	fun alpha(alpha: Float = 1.0f): GAlphaColor {
		return GAlphaColor(this, alpha)
	}
}