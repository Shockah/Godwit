package pl.shockah.godwit.tree

import com.badlogic.gdx.math.Matrix4
import pl.shockah.godwit.geom.Degrees
import pl.shockah.godwit.geom.ImmutableVec2
import pl.shockah.godwit.geom.MutableVec2
import pl.shockah.godwit.geom.Rectangle

open class Node {
	var parent: GroupNode? = null
	var visible = true

	private var transformation = Transformation()
	private var lastTransformation = transformation
	private var actualTransformationMatrix: Matrix4? = null

	val transformationMatrix: Matrix4
		get() {
			val actual = actualTransformationMatrix ?: transformation.matrix
			actualTransformationMatrix = actual
			return actual
		}

	var position: MutableVec2
		get() = transformation.position
		set(new) {
			transformation.position = new
			actualTransformationMatrix = null
		}

	var origin: MutableVec2
		get() = transformation.origin
		set(new) {
			transformation.origin = new
			actualTransformationMatrix = null
		}

	var scale: MutableVec2
		get() = transformation.scale
		set(new) {
			transformation.scale = new
			actualTransformationMatrix = null
		}

	var rotation: Degrees
		get() = transformation.rotation
		set(new) {
			transformation.rotation = new
			actualTransformationMatrix = null
		}

	var x: Float
		get() = position.x
		set(new) {
			position.x = new
			actualTransformationMatrix = null
		}

	var y: Float
		get() = position.y
		set(new) {
			position.y = new
			actualTransformationMatrix = null
		}

	open val bounds: Rectangle
		get() = Rectangle(position, ImmutableVec2.ZERO)

	internal fun applyTransformationsAndDraw(renderers: Renderers) {
		val oldTransformMatrix = renderers.transformMatrix
		renderers.transformMatrix = renderers.transformMatrix.mul(transformationMatrix)
		draw(renderers)
		renderers.transformMatrix = oldTransformMatrix
	}

	open fun draw(renderers: Renderers) {
	}
}