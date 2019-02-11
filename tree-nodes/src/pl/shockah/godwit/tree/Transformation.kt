package pl.shockah.godwit.tree

import com.badlogic.gdx.math.Affine2
import com.badlogic.gdx.math.Matrix4
import pl.shockah.godwit.geom.ImmutableVec2
import pl.shockah.godwit.geom.MutableVec2
import pl.shockah.godwit.geom.degrees

class Transformation {
	var position = MutableVec2()
	var origin = MutableVec2()
	var scale = MutableVec2(1f, 1f)
	var rotation = 0f.degrees

	val matrix: Matrix4
		get() {
			val worldTransform = Affine2()
			worldTransform.setToTrnRotScl(position.x + origin.x, position.y + origin.y, rotation.value, scale.x, scale.y)
			if (origin notEquals ImmutableVec2.ZERO)
				worldTransform.translate(-origin.x, -origin.y)
			return Matrix4().set(worldTransform)
		}

	override fun equals(other: Any?): Boolean {
		return other is Transformation && origin equals other.origin && scale equals other.scale && rotation == other.rotation
	}

	override fun hashCode(): Int {
		var result = origin.hashCode()
		result = 31 * result + scale.hashCode()
		result = 31 * result + rotation.hashCode()
		return result
	}
}