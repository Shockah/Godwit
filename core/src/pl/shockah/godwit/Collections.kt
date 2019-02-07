package pl.shockah.godwit

inline fun <T> Iterable<T>.applyEach(action: T.() -> Unit) {
	for (element in this) {
		action(element)
	}
}