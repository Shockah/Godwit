package pl.shockah.godwit.geom

import pl.shockah.godwit.GDelegates
import kotlin.reflect.KProperty

class ObservableVec2(
		initialX: Float,
		initialY: Float
) : MutableVec2(initialX, initialY) {
	constructor() : this(0f, 0f)

	constructor(listener: (property: KProperty<*>) -> Unit) : this() {
		listeners += listener
	}

	constructor(initialX: Float, initialY: Float, listener: (property: KProperty<*>) -> Unit) : this(initialX, initialY) {
		listeners += listener
	}

	val listeners = mutableListOf<(property: KProperty<*>) -> Unit>()

	override var x: Float by GDelegates.anyChangeObservable(initialX, listeners)
	override var y: Float by GDelegates.anyChangeObservable(initialY, listeners)

	companion object {
		operator fun invoke(angle: Angle, length: Float): ObservableVec2 {
			val radians = angle.radians
			return ObservableVec2(radians.cos * length, radians.sin * length)
		}
	}
}

fun Vec2.observableCopy(): ObservableVec2 {
	return ObservableVec2(x, y)
}