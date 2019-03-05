package pl.shockah.godwit.node

import pl.shockah.godwit.*
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

	var position: MutableVec2 by transformation.position.asMutableProperty()
	var origin: MutableVec2 by transformation.origin.asMutableProperty()
	var scale: MutableVec2 by transformation.scale.asMutableProperty()
	var rotation: Degrees by transformation::rotation.delegate()
	var x: Float by transformation.position::x.delegate()
	var y: Float by transformation.position::y.delegate()

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