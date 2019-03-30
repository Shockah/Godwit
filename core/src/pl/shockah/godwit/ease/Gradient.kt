package pl.shockah.godwit.ease

class Gradient<T : Easable<T>>(
		points: Map<Float, T>,
		val easing: Easing = Easing.linear
) {
	val points: Map<Float, T> = LinkedHashMap(points)

	constructor(vararg points: Pair<Float, T>, easing: Easing = Easing.linear) : this(points.toMap(), easing)

	constructor(points: List<T>, easing: Easing = Easing.linear) : this(points.mapIndexed { index, t -> 1f * index / (points.size - 1) to t }.toMap(), easing)

	constructor(vararg points: T, easing: Easing = Easing.linear) : this(points.toList(), easing)

	init {
		if (points.size < 2)
			throw IllegalArgumentException()

		var last: Float? = null
		for (key in points.keys) {
			if (last != null && key < last)
				throw IllegalArgumentException()
			if (key !in 0f..1f)
				throw IllegalArgumentException()
			last = key
		}
	}

	operator fun get(f: Float): T {
		if (f <= 0f)
			return points.values.first()
		if (f >= 1f)
			return points.values.last()

		var lastF: Float? = null
		var lastT: T? = null
		for ((mapF, mapT) in points) {
			if (lastF != null && f >= lastF && f <= mapF)
				return ((f - lastF) / (mapF - lastF)).ease(lastT!!, mapT)
			lastF = mapF
			lastT = mapT
		}

		return points.values.last()
	}
}