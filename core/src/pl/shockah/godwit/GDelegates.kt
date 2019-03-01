package pl.shockah.godwit

import kotlin.properties.ObservableProperty
import kotlin.properties.ReadOnlyProperty
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

	fun <T : Any> lazyDirty(init: () -> T): ReadOnlyProperty<Any?, T> {
		return object : ReadOnlyProperty<Any?, T> {
			private var value: T? = null

			override fun getValue(thisRef: Any?, property: KProperty<*>): T {
				var value = this.value
				if (value == null) {
					value = init()
					this.value = value
				}
				return value
			}
		}
	}
}