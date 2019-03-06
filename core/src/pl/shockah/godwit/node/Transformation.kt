package pl.shockah.godwit.node

import com.badlogic.gdx.math.Affine2
import com.badlogic.gdx.math.Matrix4
import pl.shockah.godwit.GDelegates
import pl.shockah.godwit.geom.Degrees
import pl.shockah.godwit.geom.ImmutableVec2
import pl.shockah.godwit.geom.ObservableVec2

class Transformation {
	val position = ObservableVec2 { markDirty() }
	val origin = ObservableVec2 { markDirty() }
	val scale = ObservableVec2(1f, 1f) { markDirty() }
	var rotation: Degrees by GDelegates.changeObservable(Degrees()) { -> markDirty() }

	private var backingMatrix: Matrix4? = null
	private var backingMatrixWithOrigin: Matrix4? = null

	val matrix: Matrix4
		get() {
			if (backingMatrix == null)
				setupMatrices()
			return backingMatrix!!
		}

	val matrixWithOrigin: Matrix4
		get() {
			if (backingMatrixWithOrigin == null)
				setupMatrices()
			return backingMatrixWithOrigin!!
		}

	private fun markDirty() {
		backingMatrix = null
		backingMatrixWithOrigin = null
	}

	private fun setupMatrices() {
		val transform = Affine2()
		transform.setToTrnRotScl(position.x, position.y, rotation.value, scale.x, scale.y)
		backingMatrix = Matrix4().set(transform)
		if (origin notEquals ImmutableVec2.ZERO)
			transform.translate(-origin.x, -origin.y)
		backingMatrixWithOrigin = Matrix4().set(transform)
	}

	override fun equals(other: Any?): Boolean {
		return other is Transformation
				&& position equals other.position
				&& origin equals other.origin
				&& scale equals other.scale
				&& rotation == other.rotation
	}

	override fun hashCode(): Int {
		var result = origin.hashCode()
		result = 31 * result + scale.hashCode()
		result = 31 * result + rotation.hashCode()
		return result
	}
}