package pl.shockah.godwit.tree

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
			stageLayer = new.stageLayer
		old?.children?.remove(this)
		new?.children?.add(this)
	}

	private var backingStageLayer: StageLayer? = null

	var stageLayer: StageLayer
		get() = backingStageLayer!!
		protected set(value) {
			backingStageLayer = value
			gestureRecognizers.forEach { value.awaitableStage.await(it) }
		}

	val stage: Stage
		get() = stageLayer.stage

	var visible = true
	var zLayer: Float? = null

	open var touchShape: Shape.Filled = Shape.none
	var clipsChildrenTouches = false

	val gestureRecognizers = ObservableList<GestureRecognizer>().apply {
		listeners += object : ObservableList.ChangeListener<GestureRecognizer> {
			override fun onAddedToList(element: GestureRecognizer) {
				backingStageLayer?.awaitableStage?.await(element)
			}

			override fun onRemovedFromList(element: GestureRecognizer) {
			}
		}
	}

	private var transformation = Transformation()

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

	open fun draw(renderers: TreeNodeRenderers) {
		if (!visible)
			return

		val oldTransformMatrix = renderers.transformMatrix
		val matrixWithOrigin = oldTransformMatrix.cpy().mul(transformation.matrixWithOrigin)
		renderers.transformationMatrixCache[this] = matrixWithOrigin

		val zLayer = this.zLayer
		if (zLayer == null) {
			renderers.transformMatrix = matrixWithOrigin
			drawSelf(renderers)
		} else {
			renderers.zOrderedNodes += Triple(zLayer, this, matrixWithOrigin)
		}

		renderers.transformMatrix = oldTransformMatrix.cpy().mul(transformation.matrix)
		drawChildren(renderers)

		renderers.transformMatrix = oldTransformMatrix
	}

	open fun drawSelf(renderers: TreeNodeRenderers) {
	}

	open fun drawChildren(renderers: TreeNodeRenderers) {
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