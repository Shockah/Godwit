package pl.shockah.godwit.color

import com.badlogic.gdx.graphics.Color
import pl.shockah.godwit.ease.Easable

interface GColor<CS : GColor<CS>> : Easable<CS> {
	val rgb: RGBColor

	val gdx: Color
		get() {
			val rgb = this.rgb
			return Color(rgb.r, rgb.g, rgb.b, 1f)
		}

	fun copy(): CS

	fun getDistance(other: CS): Float
}