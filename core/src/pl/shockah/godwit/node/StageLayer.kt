package pl.shockah.godwit.node

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.glutils.HdpiUtils
import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.utils.viewport.ScreenViewport
import com.badlogic.gdx.utils.viewport.Viewport
import pl.shockah.godwit.LateInitAwaitable
import pl.shockah.godwit.boundingBox
import pl.shockah.godwit.geom.Shape
import pl.shockah.godwit.geom.Vec2
import pl.shockah.godwit.geom.godwit
import pl.shockah.godwit.toVector2
import pl.shockah.godwit.toVector3
import pl.shockah.godwit.node.gesture.GestureRecognizer
import pl.shockah.godwit.node.gesture.Touch

open class StageLayer(
		vararg viewports: Viewport = arrayOf(ScreenViewport())
) {
	val awaitableStage = LateInitAwaitable<Stage> { _, _, _ ->
		resize(Gdx.graphics.width, Gdx.graphics.height)
	}
	var stage by awaitableStage

	val viewports = viewports.toList()

	val root = object : Node() {
		init {
			stageLayer = this@StageLayer
		}
	}

	var clipping: Boolean = true

	open fun resize(screenWidth: Int, screenHeight: Int) {
		viewports.forEach {
			it.update(screenWidth, screenHeight, true)
		}
	}

	open fun update(delta: Float) {
		root.update(delta)
	}

	open fun draw(renderers: NodeRenderers) {
		if (!root.visible)
			return

		val clipping = this.clipping
		if (clipping)
			Gdx.gl.glEnable(GL20.GL_SCISSOR_TEST)

		viewports.forEach { viewport ->
			renderers.currentViewport = viewport
			viewport.apply()
			renderers.projectionMatrix = viewport.camera.combined

			if (clipping)
				HdpiUtils.glScissor(viewport.screenX, viewport.screenY, viewport.screenWidth, viewport.screenHeight)

			root.draw(renderers)
			val oldTransformMatrix = renderers.transformMatrix
			renderers.zOrderedNodes.forEach { (_, node, matrix) ->
				renderers.transformMatrix = matrix
				node.drawSelf(renderers)
			}
			renderers.transformMatrix = oldTransformMatrix
			renderers.zOrderedNodes.clear()
		}

		if (clipping)
			Gdx.gl.glDisable(GL20.GL_SCISSOR_TEST)
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
			handle(GestureRecognizer::handleTouchDragged, false, viewport, touch, point, root)
		}
	}

	internal fun touchUp(x: Int, y: Int, touch: Touch) {
		viewports.forEach { viewport ->
			val point = viewport.camera.unproject(Vector3(x.toFloat(), y.toFloat(), 0f)).toVector2().godwit
			handle(GestureRecognizer::handleTouchUp, false, viewport, touch, point, root)
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

		val inShape = !clipping || (touch.points.last().position in viewport.boundingBox && testInShape())
		if (!node.clipsChildrenTouches || inShape) {
			node.children.snapshot { children ->
				children.forEach { handle(method, clipping, viewport, touch, point, it) }
			}
		}

		if (inShape)
			node.gestureRecognizers.forEach { method(it, touch, point) }
	}
}