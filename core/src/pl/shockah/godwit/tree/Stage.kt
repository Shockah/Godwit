package pl.shockah.godwit.tree

import com.badlogic.gdx.InputProcessor
import pl.shockah.godwit.applyEach
import pl.shockah.godwit.geom.ImmutableVec2
import pl.shockah.godwit.tree.gesture.ContinuousGestureRecognizer
import pl.shockah.godwit.tree.gesture.GestureRecognizer
import pl.shockah.godwit.tree.gesture.Touch

open class Stage(
		vararg stageLayers: StageLayer
) : InputProcessor {
	val stageLayers = stageLayers.toList()

	internal val renderers = TreeNodeRenderers()

	private val touches = mutableMapOf<Pair<Int, Int>, Touch>()
	internal val gestureRecognizers = mutableSetOf<GestureRecognizer>()
	internal val activeContinuousGestureRecognizers = mutableSetOf<ContinuousGestureRecognizer>()

	init {
		stageLayers.forEach { it.stage = this }
	}

	open fun resize(screenWidth: Int, screenHeight: Int) {
		stageLayers.forEach {
			it.resize(screenWidth, screenHeight)
		}
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

		stageLayers.forEach { it.update(delta) }
	}

	open fun draw() {
		renderers.transformationMatrixCache.clear()
		stageLayers.forEach { it.draw(renderers) }
		renderers.flush()
	}

	override fun touchDown(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
		val touch = Touch(pointer, button)
		touch += ImmutableVec2(screenX.toFloat(), screenY.toFloat())
		touches[pointer to button] = touch
		stageLayers.asReversed().forEach { it.touchDown(screenX, screenY, touch) }
		return true
	}

	override fun touchDragged(screenX: Int, screenY: Int, pointer: Int): Boolean {
		val point = ImmutableVec2(screenX.toFloat(), screenY.toFloat())
		var handled = false
		touches.forEach { (touchPointer, _), touch ->
			if (touchPointer == pointer) {
				touch += point
				stageLayers.asReversed().forEach { it.touchDragged(screenX, screenY, touch) }
				handled = true
			}
		}
		return handled
	}

	override fun touchUp(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
		val touch = touches[pointer to button] ?: return false
		if (touch.finished)
			return true

		touch += ImmutableVec2(screenX.toFloat(), screenY.toFloat())
		touch.finish()
		touches.remove(pointer to button)
		stageLayers.asReversed().forEach { it.touchUp(screenX, screenY, touch) }
		return true
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