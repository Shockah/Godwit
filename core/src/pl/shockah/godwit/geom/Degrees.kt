package pl.shockah.godwit.geom

inline class Degrees(val value: Float) {
	fun getDelta(angle: Degrees): Degrees {
		var angle1 = value
		var angle2 = angle.value
		while (angle2 <= -180)
			angle2 += 360f
		while (angle2 > 180)
			angle2 -= 360f
		while (angle1 <= -180)
			angle1 += 360f
		while (angle1 > 180)
			angle1 -= 360f

		val r = angle2 - angle1
		return Degrees(r + if (r > 180) -360 else if (r < -180) 360 else 0)
	}
}