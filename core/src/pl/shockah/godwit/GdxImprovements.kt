package pl.shockah.godwit

import com.badlogic.gdx.Graphics
import com.badlogic.gdx.math.Matrix4
import pl.shockah.godwit.geom.ImmutableVec2

infix fun Matrix4.equals(other: Matrix4): Boolean {
	return values!!.contentEquals(other.values)
}

infix fun Matrix4.notEquals(other: Matrix4): Boolean {
	return !(this equals other)
}

val Graphics.size: ImmutableVec2
	get() = ImmutableVec2(width.toFloat(), height.toFloat())