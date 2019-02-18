package pl.shockah.godwit.tree

import com.badlogic.gdx.math.Matrix4
import pl.shockah.godwit.SnapshotList
import pl.shockah.godwit.applyEach
import pl.shockah.godwit.geom.Degrees
import pl.shockah.godwit.geom.ImmutableVec2
import pl.shockah.godwit.geom.MutableVec2
import pl.shockah.godwit.geom.Rectangle
import pl.shockah.godwit.render.Renderers
import java.util.*
import kotlin.properties.Delegates

open class Node {
	val children = SnapshotList(LinkedList<Node>())
	var parent: Node? by Delegates.observable(null) { _, old: Node?, new: Node? ->
		old?.children?.remove(this)
		new?.children?.add(this)
	}
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

	open fun draw(renderers: Renderers) {
		if (!visible)
			return

		val oldTransformMatrix = renderers.transformMatrix
		renderers.transformMatrix = renderers.transformMatrix.cpy().mul(transformationMatrix)
		drawSelf(renderers)
		drawChildren(renderers)
		renderers.transformMatrix = oldTransformMatrix
	}

	open fun drawSelf(renderers: Renderers) {
	}

	open fun drawChildren(renderers: Renderers) {
		children.applyEach { draw(renderers) }
	}

	open fun update(delta: Float) {
		updateSelf(delta)
		updateChildren(delta)
	}

	open fun updateSelf(delta: Float) {
	}

	open fun updateChildren(delta: Float) {
		children.applyEach { update(delta) }
	}

	operator fun plusAssign(node: Node) {
		node.parent = this
	}

	operator fun minusAssign(node: Node) {
		if (node.parent != this)
			throw IllegalStateException()
		node.parent = null
	}
}