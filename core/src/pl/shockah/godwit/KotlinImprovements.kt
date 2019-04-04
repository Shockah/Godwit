package pl.shockah.godwit

import kotlin.math.ceil
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

fun Double.inCycle(min: Double, max: Double): Double {
	val cycle = max - min
	var new = this - min

	if (new >= cycle)
		new %= cycle
	else if (new < 0)
		new += ceil(-new / cycle) * cycle

	return new + min
}

fun Float.inCycle(min: Float, max: Float): Float {
	val cycle = max - min
	var new = this - min

	if (new >= cycle)
		new %= cycle
	else if (new < 0)
		new += ceil(-new / cycle) * cycle

	return new + min
}

fun Int.inCycle(min: Int, max: Int): Int {
	val cycle = max - min
	var new = this - min

	if (new >= cycle)
		new %= cycle
	else if (new < 0)
		new += ceil(-new.toDouble() / cycle.toDouble()).toInt() * cycle

	return new + min
}