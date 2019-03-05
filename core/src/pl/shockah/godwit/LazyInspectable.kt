package pl.shockah.godwit

import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

class LazyInspectable<T>(
		private val initializer: () -> T
) : ReadOnlyProperty<Any?, T> {
	private var value: T? = null

	val initialized: Boolean
		get() = value != null

	override fun getValue(thisRef: Any?, property: KProperty<*>): T {
		if (!initialized)
			value = initializer()
		return value!!
	}
}