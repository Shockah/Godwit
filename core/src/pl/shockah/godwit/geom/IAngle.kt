package pl.shockah.godwit.geom

import pl.shockah.godwit.ease.Easable

typealias Angle = IAngle<*>

interface IAngle<T : IAngle<T>> : Easable<IAngle<*>> {
	val degrees: Degrees

	val radians: Radians

	val sin: Float

	val cos: Float

	val tan: Float

	infix fun delta(angle: Angle): T

	operator fun plus(other: Angle): T

	operator fun minus(other: Angle): T

	fun rotated(fullRotations: Float): T
}