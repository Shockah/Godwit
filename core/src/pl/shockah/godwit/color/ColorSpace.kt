package pl.shockah.godwit.color

import pl.shockah.godwit.ease.Easable

interface ColorSpace<CS : ColorSpace<CS>> : Easable<CS> {
	val rgb: RGBColorSpace

	fun copy(): CS

	fun getDistance(other: CS): Float
}