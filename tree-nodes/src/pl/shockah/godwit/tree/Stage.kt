package pl.shockah.godwit.tree

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.InputProcessor
import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.utils.viewport.ScreenViewport
import com.badlogic.gdx.utils.viewport.Viewport
import pl.shockah.godwit.applyEach
import pl.shockah.godwit.geom.ImmutableVec2
import pl.shockah.godwit.geom.Shape
import pl.shockah.godwit.geom.Vec2
import pl.shockah.godwit.geom.godwit
import pl.shockah.godwit.toVector2
import pl.shockah.godwit.toVector3
import pl.shockah.godwit.tree.gesture.ContinuousGestureRecognizer
import pl.shockah.godwit.tree.gesture.GestureRecognizer
import pl.shockah.godwit.tree.gesture.Touch

open class Stage(
		val viewport: Viewport = ScreenViewport()
) : InputProcessor {
	val root = object : Node() {
		init {
			stage = this@Stage
		}
	}

	private val renderers = TreeNodeRenderers()

	private val touches = mutableMapOf<Int, Touch>()
	internal val gestureRecognizers = mutableSetOf<GestureRecognizer>()
	internal val activeContinuousGestureRecognizers = mutableSetOf<ContinuousGestureRecognizer>()

	init {
		viewport.update(Gdx.graphics.width, Gdx.graphics.height, true)
	}

	open fun draw() {
		if (!root.visible)
			return

		val camera = viewport.camera
		camera.update()
		renderers.projectionMatrix = camera.combined

		root.draw(renderers, null)
		renderers.currentPassZLayers.forEach { zLayer, nodes ->
			nodes.forEach { it.draw(renderers, zLayer) }
		}
		renderers.currentPassZLayers.clear()

		renderers.flush()
	}

	open fun update(delta: Float) {
		val recognizers = gestureRecognizers.toList()

		recognizers.applyEach {
			if (state == GestureRecognizer.State.Ended)
				state = GestureRecognizer.State.Possible
		}

		if (touches.isEmpty() && activeContinuousGestureRecognizers.isEmpty()) {
			recognizers.applyEach {
				if (state == GestureRecognizer.State.Detecting)
					state = GestureRecognizer.State.Failed
				else if (inProgress)
					state = GestureRecognizer.State.Ended

				if (finished)
					state = GestureRecognizer.State.Possible
			}
		}

		root.update(delta)
	}

	private fun screenToWorldCoordinates(screenX: Int, screenY: Int): ImmutableVec2 {
		return viewport.camera.unproject(Vector3(screenX.toFloat(), screenY.toFloat(), 0f)).toVector2().godwit
	}

	override fun touchDown(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
		val touch = Touch(pointer, button)
		val point = screenToWorldCoordinates(screenX, screenY)
		touch += point
		touches[pointer] = touch
		handle(GestureRecognizer::handleTouchDown, true, touch, point, root)
		return true
	}

	override fun touchDragged(screenX: Int, screenY: Int, pointer: Int): Boolean {
		val touch = touches[pointer] ?: return false
		if (touch.finished)
			return true

		val point = screenToWorldCoordinates(screenX, screenY)
		touch += point
		handle(GestureRecognizer::handleTouchDragged, false, touch, point, root)
		return true
	}

	override fun touchUp(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
		val touch = touches[pointer] ?: return false
		if (touch.finished)
			return true

		val point = screenToWorldCoordinates(screenX, screenY)
		touch += point
		touch.finish()
		touches.remove(pointer)
		handle(GestureRecognizer::handleTouchUp, false, touch, point, root)
		return true
	}

	private fun handle(method: (recognizer: GestureRecognizer, touch: Touch, point: Vec2) -> Unit, clipping: Boolean, touch: Touch, point: Vec2, node: Node) {
		fun testInShape(): Boolean {
			return when (node.touchShape) {
				Shape.infinitePlane -> true
				Shape.none -> false
				else -> {
					val transformed = point.gdx.toVector3().mul(node.cachedMatrixWithOrigin.cpy().inv()).toVector2().godwit
					return transformed in node.touchShape
				}
			}
		}

		val inShape = !clipping || testInShape()
		if (!node.clipsChildrenTouches || inShape) {
			node.children.snapshot { children ->
				children.forEach { handle(method, clipping, touch, point, it) }
			}
		}

		if (inShape)
			node.gestureRecognizers.forEach { method(it, touch, point) }
	}

	override fun keyDown(keycode: Int): Boolean {
		return false
	}

	override fun keyUp(keycode: Int): Boolean {
		return false
	}

	override fun keyTyped(character: Char): Boolean {
		return false
	}

	override fun mouseMoved(screenX: Int, screenY: Int): Boolean {
		return false
	}

	override fun scrolled(amount: Int): Boolean {
		return false
	}
}