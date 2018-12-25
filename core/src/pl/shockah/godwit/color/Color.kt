package pl.shockah.godwit.color

import pl.shockah.godwit.ease.Easable

interface Color<C : Color<C>> : Easable<C> {
	val rgb: RGBColor

	fun copy(): C

	fun getDistance(other: C): Float
}