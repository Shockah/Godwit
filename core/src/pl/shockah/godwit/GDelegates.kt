package pl.shockah.godwit

import kotlin.properties.ObservableProperty
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

object GDelegates {
	fun <T> anyChangeObservable(
			initialValue: T,
			listeners: Collection<(property: KProperty<*>) -> Unit>
	): ReadWriteProperty<Any?, T> =
			object : ObservableProperty<T>(initialValue) {
				override fun afterChange(property: KProperty<*>, oldValue: T, newValue: T) {
					if (newValue != oldValue) {
						listeners.forEach {
							it(property)
						}
					}
				}
			}
}