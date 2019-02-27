package pl.shockah.godwit

inline infix fun <reified T : Comparable<T>> T.compare(other: T): Int {
	return compareTo(other)
}