package pl.shockah.godwit

import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

class LateInitAwaitable<T> : ReadWriteProperty<Any?, T> {
	private var value: T? = null

	private val awaitees = mutableListOf<Awaitee<T>>()

	fun await(awaitee: Awaitee<T>) {
		val value = this.value
		if (value == null)
			awaitees += awaitee
		else
			awaitee.onLateInit(this, value)
	}

	override fun getValue(thisRef: Any?, property: KProperty<*>): T {
		return value!!
	}

	override fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
		if (value === this.value)
			return

		this.value = value
		awaitees.forEach { it.onLateInit(this, value) }
		awaitees.clear()
	}

	interface Awaitee<T> {
		fun onLateInit(property: LateInitAwaitable<T>, value: T)
	}
}