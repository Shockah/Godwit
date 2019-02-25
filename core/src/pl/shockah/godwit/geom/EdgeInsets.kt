package pl.shockah.godwit.geom

data class EdgeInsets(
		val top: Float = 0f,
		val right: Float = 0f,
		val bottom: Float = 0f,
		val left: Float = 0f
) {
	constructor(horizontal: Float, vertical: Float) : this(vertical, horizontal, vertical, horizontal)
}