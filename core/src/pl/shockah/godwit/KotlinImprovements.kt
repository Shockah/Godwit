package pl.shockah.godwit

import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KMutableProperty0
import kotlin.reflect.KProperty

inline infix fun <reified T : Comparable<T>> T.compare(other: T): Int {
	return compareTo(other)
}

fun <R> KMutableProperty0<R>.delegate(): ReadWriteProperty<Any?, R> {
	return object : ReadWriteProperty<Any?, R> {
		override fun getValue(thisRef: Any?, property: KProperty<*>): R {
			return get()
		}

		override fun setValue(thisRef: Any?, property: KProperty<*>, value: R) {
			set(value)
		}
	}
}