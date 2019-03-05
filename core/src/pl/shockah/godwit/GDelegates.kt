package pl.shockah.godwit

import kotlin.properties.ObservableProperty
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

object GDelegates {
	fun <T> changeObservable(initialValue: T, onChange: () -> Unit): ReadWriteProperty<Any?, T> {
		return GDelegates.changeObservable(initialValue) { _, _, _ -> onChange() }
	}

	fun <T> changeObservable(initialValue: T, onChange: (property: KProperty<*>, oldValue: T, newValue: T) -> Unit): ReadWriteProperty<Any?, T> {
		return object : ObservableProperty<T>(initialValue) {
			override fun afterChange(property: KProperty<*>, oldValue: T, newValue: T) {
				if (newValue != oldValue)
					onChange(property, oldValue, newValue)
			}
		}
	}

	fun <T> anyChangeObservable(initialValue: T, listeners: Collection<(property: KProperty<*>) -> Unit>): ReadWriteProperty<Any?, T> {
		return object : ObservableProperty<T>(initialValue) {
			override fun afterChange(property: KProperty<*>, oldValue: T, newValue: T) {
				if (newValue != oldValue) {
					listeners.forEach {
						it(property)
					}
				}
			}
		}
	}
}