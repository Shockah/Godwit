package pl.shockah.godwit.tree

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.utils.viewport.ScreenViewport
import com.badlogic.gdx.utils.viewport.Viewport
import pl.shockah.godwit.geom.Shape
import pl.shockah.godwit.geom.Vec2
import pl.shockah.godwit.geom.godwit
import pl.shockah.godwit.toVector2
import pl.shockah.godwit.toVector3
import pl.shockah.godwit.tree.gesture.GestureRecognizer
import pl.shockah.godwit.tree.gesture.Touch

open class StageLayer(
		vararg viewports: Viewport = arrayOf(ScreenViewport())
) {
	private var backingStage: Stage? = null

	var stage: Stage
		get() = backingStage!!
		set(value) {
			backingStage = value
			stageAwaiters.forEach { it.onStageSet(value) }
			stageAwaiters.clear()
		}

	internal val stageAwaiters = mutableListOf<StageAwaiter>()

	val viewports = viewports.toList()

	val root = object : Node() {
		init {
			stageLayer = this@StageLayer
		}
	}

	init {
		viewports.forEach {
			it.update(Gdx.graphics.width, Gdx.graphics.height, true)
		}
	}

	open fun update(delta: Float) {
		root.update(delta)
	}

	open fun draw(renderers: TreeNodeRenderers) {
		if (!root.visible)
			return

		viewports.forEach { viewport ->
			renderers.currentViewport = viewport
			viewport.apply()
			viewport.camera.update()
			renderers.projectionMatrix = viewport.camera.combined

			root.draw(renderers, null)
			renderers.currentPassZLayers.forEach { zLayer, nodes ->
				nodes.forEach { it.draw(renderers, zLayer) }
			}
			renderers.currentPassZLayers.clear()
		}
	}

	internal fun touchDown(x: Int, y: Int, touch: Touch) {
		viewports.forEach { viewport ->
			val point = viewport.camera.unproject(Vector3(x.toFloat(), y.toFloat(), 0f)).toVector2().godwit
			handle(GestureRecognizer::handleTouchDown, true, viewport, touch, point, root)
		}
	}

	internal fun touchDragged(x: Int, y: Int, touch: Touch) {
		viewports.forEach { viewport ->
			val point = viewport.camera.unproject(Vector3(x.toFloat(), y.toFloat(), 0f)).toVector2().godwit
			handle(GestureRecognizer::handleTouchDragged, true, viewport, touch, point, root)
		}
	}

	internal fun touchUp(x: Int, y: Int, touch: Touch) {
		viewports.forEach { viewport ->
			val point = viewport.camera.unproject(Vector3(x.toFloat(), y.toFloat(), 0f)).toVector2().godwit
			handle(GestureRecognizer::handleTouchUp, true, viewport, touch, point, root)
		}
	}

	private fun handle(method: (recognizer: GestureRecognizer, touch: Touch, point: Vec2) -> Unit, clipping: Boolean, viewport: Viewport, touch: Touch, point: Vec2, node: Node) {
		fun testInShape(): Boolean {
			if (!node.visible)
				return false

			return when (node.touchShape) {
				Shape.infinitePlane -> true
				Shape.none -> false
				else -> {
					val cachedMatrix = stage.renderers.transformationMatrixCache[viewport, node] ?: return false
					val transformed = point.gdx.toVector3().mul(cachedMatrix.cpy().inv()).toVector2().godwit
					return transformed in node.touchShape
				}
			}
		}

		val inShape = !clipping || testInShape()
		if (!node.clipsChildrenTouches || inShape) {
			node.children.snapshot { children ->
				children.forEach { handle(method, clipping, viewport, touch, point, it) }
			}
		}

		if (inShape)
			node.gestureRecognizers.forEach { method(it, touch, point) }
	}

	internal fun awaitStage(awaiter: StageAwaiter) {
		val backingStage = this.backingStage
		if (backingStage == null)
			stageAwaiters += awaiter
		else
			awaiter.onStageSet(backingStage)
	}

	interface StageAwaiter {
		fun onStageSet(stage: Stage)
	}
}