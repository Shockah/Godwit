package pl.shockah.godwit.node

import pl.shockah.godwit.LateInitAwaitable
import pl.shockah.godwit.ObservableList
import pl.shockah.godwit.SnapshotList
import pl.shockah.godwit.applyEach
import pl.shockah.godwit.geom.*
import pl.shockah.godwit.node.gesture.GestureRecognizer
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

	private val awaitableStageLayer = LateInitAwaitable<StageLayer>()
	var stageLayer: StageLayer by awaitableStageLayer

	val stage: Stage
		get() = stageLayer.stage

	init {
		awaitableStageLayer.listeners += { _, _, new ->
			gestureRecognizers.forEach { new.awaitableStage.await { _, value ->
				it.stage = value
			} }
		}
	}

	var visible = true
	open var zLayer: Float? = null

	open var touchShape: Shape.Filled = Shape.none
	var clipsChildrenTouches = false

	val gestureRecognizers = ObservableList<GestureRecognizer>().apply {
		listeners += object : ObservableList.ChangeListener<GestureRecognizer> {
			override fun onAddedToList(element: GestureRecognizer) {
				if (awaitableStageLayer.initialized) {
					stageLayer.awaitableStage.await { _, value ->
						element.stage = value
					}
				}
			}

			override fun onRemovedFromList(element: GestureRecognizer) {
			}
		}
	}

	val transformation = Transformation()

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

	open fun draw(renderers: NodeRenderers) {
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

	open fun drawSelf(renderers: NodeRenderers) {
	}

	open fun drawChildren(renderers: NodeRenderers) {
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