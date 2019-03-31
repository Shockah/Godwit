package pl.shockah.godwit.ease

fun <T : Easable<T>> T.ease(other: T, easing: Easing, f: Float): T {
	return easing.ease(this, other, f)
}

fun <T : Easable<T>> Float.ease(a: T, b: T): T {
	return a.ease(b, this)
}

fun Float.ease(other: Float, f: Float): Float {
	return this + f * (other - this)
}

fun Float.ease(other: Float, easing: Easing, f: Float): Float {
	return easing.ease(this, other, f)
}

interface Easable<T> {
	fun ease(other: T, f: Float): T

	class Wrapper<T>(
			val wrapped: T,
			private val function: (a: T, b: T, f: Float) -> T
	) : Easable<Wrapper<T>> {
		override fun ease(other: Wrapper<T>, f: Float): Wrapper<T> {
			return Wrapper(function(wrapped, other.wrapped, f), function)
		}
	}
}