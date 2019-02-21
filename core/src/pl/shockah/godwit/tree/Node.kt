package pl.shockah.godwit.tree

import com.badlogic.gdx.math.Matrix4
import pl.shockah.godwit.ObservableList
import pl.shockah.godwit.SnapshotList
import pl.shockah.godwit.applyEach
import pl.shockah.godwit.geom.*
import pl.shockah.godwit.tree.gesture.GestureRecognizer
import java.util.*
import kotlin.properties.Delegates

open class Node {
	val children = SnapshotList(LinkedList<Node>())
	var parent: Node? by Delegates.observable(null) { _, old: Node?, new: Node? ->
		if (new != null)
			stage = new.stage
		old?.children?.remove(this)
		new?.children?.add(this)
	}

	private var backingStage: Stage? = null

	var stage: Stage
		get() = backingStage!!
		protected set(value) {
			backingStage = value
			gestureRecognizers.forEach { it.stage = backingStage!! }
		}

	var visible = true
	var zLayer: Float? = null

	open var touchShape: Shape.Filled = Shape.none
	var clipsChildrenTouches = false

	val gestureRecognizers = ObservableList(mutableListOf<GestureRecognizer>()).apply {
		listeners += object : ObservableList.ChangeListener<GestureRecognizer> {
			override fun onAddedToList(element: GestureRecognizer) {
				backingStage?.let { stage ->
					element.stage = stage
				}
			}

			override fun onRemovedFromList(element: GestureRecognizer) {
			}
		}
	}

	private var transformation = Transformation()
	internal var cachedMatrix = Matrix4()
	internal var cachedMatrixWithOrigin = Matrix4()

	var position: MutableVec2
		get() = transformation.position
		set(new) { transformation.position.set(new) }

	var origin: MutableVec2
		get() = transformation.origin
		set(new) { transformation.origin.set(new) }

	var scale: MutableVec2
		get() = transformation.scale
		set(new) { transformation.scale.set(new) }

	var rotation: Degrees
		get() = transformation.rotation
		set(new) { transformation.rotation = new }

	var x: Float
		get() = position.x
		set(new) { position.x = new }

	var y: Float
		get() = position.y
		set(new) { position.y = new }

	open val bounds: Rectangle
		get() = Rectangle(position, ImmutableVec2.ZERO)

	open fun draw(renderers: TreeNodeRenderers, zLayer: Float?) {
		if (!visible)
			return

		val oldTransformMatrix = renderers.transformMatrix
		if (zLayer == null) {
			cachedMatrixWithOrigin.set(renderers.transformMatrix).mul(transformation.matrixWithOrigin)
			cachedMatrix.set(renderers.transformMatrix).mul(transformation.matrix)
		}
		val myZLayer = this.zLayer

		if (myZLayer == null || (zLayer != null && myZLayer == zLayer)) {
			renderers.transformMatrix = cachedMatrixWithOrigin
			drawSelf(renderers)
		}

		if (zLayer == null && myZLayer != null)
			renderers.currentPassZLayers.computeIfAbsent(myZLayer) { mutableListOf() } += this

		renderers.transformMatrix = cachedMatrix
		drawChildren(renderers, zLayer)

		renderers.transformMatrix = oldTransformMatrix
	}

	open fun drawSelf(renderers: TreeNodeRenderers) {
	}

	open fun drawChildren(renderers: TreeNodeRenderers, zLayer: Float?) {
		children.applyEach { draw(renderers, zLayer) }
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