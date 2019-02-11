package pl.shockah.godwit.geom

import pl.shockah.godwit.GDelegates
import kotlin.math.cos
import kotlin.math.sin
import kotlin.reflect.KProperty

class ObservableVec2(
		initialX: Float,
		initialY: Float
) : MutableVec2(initialX, initialY) {
	constructor() : this(0f, 0f)

	val listeners = mutableListOf<(KProperty<*>) -> Unit>()

	override var x: Float by GDelegates.anyChangeObservable(initialX, listeners)
	override var y: Float by GDelegates.anyChangeObservable(initialY, listeners)

	companion object {
		operator fun invoke(degrees: Degrees, length: Float): MutableVec2 {
			val angle = Math.toRadians((degrees.value + 180f).toDouble())
			return MutableVec2(
					(-cos(angle) * length).toFloat(),
					(-sin(angle) * length).toFloat()
			)
		}
	}
}

fun Vec2.observableCopy(): ObservableVec2 {
	return ObservableVec2(x, y)
}