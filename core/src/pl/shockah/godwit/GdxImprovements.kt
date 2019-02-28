package pl.shockah.godwit

import com.badlogic.gdx.Graphics
import com.badlogic.gdx.assets.AssetDescriptor
import com.badlogic.gdx.math.Matrix4
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.utils.viewport.Viewport
import pl.shockah.godwit.geom.ImmutableVec2
import pl.shockah.godwit.geom.Rectangle

infix fun Matrix4.equals(other: Matrix4): Boolean {
	return values!!.contentEquals(other.values)
}

infix fun Matrix4.notEquals(other: Matrix4): Boolean {
	return !(this equals other)
}

fun Vector2.toVector3(z: Float = 0f): Vector3 {
	return Vector3(x, y, z)
}

fun Vector3.toVector2(): Vector2 {
	return Vector2(x, y)
}

operator fun Matrix4.get(index: Int): Float {
	return `val`[index]
}

operator fun Matrix4.set(index: Int, value: Float) {
	`val`[index] = value
}

val Graphics.size: ImmutableVec2
	get() = ImmutableVec2(width.toFloat(), height.toFloat())

val Graphics.ppi: ImmutableVec2
	get() = ImmutableVec2(ppiX, ppiY)

inline fun <reified T> assetDescriptor(path: String): AssetDescriptor<T> {
	return AssetDescriptor<T>(path, T::class.java)
}

val Viewport.boundingBox: Rectangle
	get() = Rectangle(ImmutableVec2(screenX.toFloat(), screenY.toFloat()), ImmutableVec2(screenWidth.toFloat(), screenHeight.toFloat()))