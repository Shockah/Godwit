package pl.shockah.godwit

import com.badlogic.gdx.math.Matrix4

infix fun Matrix4.equals(other: Matrix4): Boolean {
	return values!!.contentEquals(other.values)
}

infix fun Matrix4.notEquals(other: Matrix4): Boolean {
	return !(this equals other)
}