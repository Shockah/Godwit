package pl.shockah.godwit.geom

import pl.shockah.godwit.GDelegates
import kotlin.math.PI
import kotlin.properties.ObservableProperty
import kotlin.properties.ReadWriteProperty
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
		fun property(x: Float = 0f, y: Float = 0f, onChange: () -> Unit): ReadWriteProperty<Any?, ObservableVec2> {
			val initial = ObservableVec2(x, y)
			initial.listeners += { onChange() }
			return object : ObservableProperty<ObservableVec2>(initial) {
				override fun afterChange(property: KProperty<*>, oldValue: ObservableVec2, newValue: ObservableVec2) {
					if (newValue != oldValue) {
						newValue.listeners += { onChange() }
						onChange()
					}
				}
			}
		}

		operator fun invoke(angle: Angle, length: Float): ObservableVec2 {
			val radians = angle.radians + PI.toFloat().radians
			return ObservableVec2(-radians.cos * length, -radians.sin * length)
		}
	}
}

fun Vec2.observableCopy(): ObservableVec2 {
	return ObservableVec2(x, y)
}