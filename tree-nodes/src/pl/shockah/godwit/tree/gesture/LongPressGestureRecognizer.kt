package pl.shockah.godwit.tree.gesture

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.utils.Timer
import pl.shockah.godwit.geom.Circle
import pl.shockah.godwit.geom.Vec2
import pl.shockah.godwit.ppi

class LongPressGestureRecognizer(
		val delay: Float = 0f,
		val delegate: (recognizer: LongPressGestureRecognizer, touch: Touch) -> Unit
) : GestureRecognizer() {
	companion object {
		private val stationaryRadius: Float by lazy {
			val ppi = Gdx.graphics.ppi
//				val ppi = Godwit.getInstance().getPpi()
			val average = (ppi.x + ppi.y) * 0.5f
			return@lazy average * 0.1f
		}
	}

	var touch: Touch? = null

	private var timerTask: Timer.Task? = null

	private var shouldEnd = false

	override var state: State
		get() = super.state
		set(value) {
			super.state = value

			if (finished) {
				touch = null
				shouldEnd = true
				timerTask?.cancel()
				timerTask = null
			}
		}

	override fun onRequiredFailFailed(recognizer: GestureRecognizer) {
		val touch = touch
		if (shouldEnd && touch != null) {
			touch.recognizer = this
			state = State.Ended
			delegate(this, touch)
		}
	}

	override fun handleTouchDown(touch: Touch, point: Vec2) {
		if (state == State.Possible) {
			state = State.Began
			this.touch = touch
			timerTask = Timer.schedule(object : Timer.Task() {
				override fun run() {
					Gdx.app.postRunnable {
						timerTask = null
						if (requiredRecognizersFailed) {
							touch.recognizer = this@LongPressGestureRecognizer
							state = State.Ended
							delegate(this@LongPressGestureRecognizer, touch)
						} else {
							shouldEnd = true
						}
					}
				}
			}, delay)
		}
	}

	override fun handleTouchDragged(touch: Touch, point: Vec2) {
		if (inProgress) {
			state = if (point !in Circle(touch.points.first().position, stationaryRadius))
				State.Failed
			else
				State.Changed
		}
	}

	override fun handleTouchUp(touch: Touch, point: Vec2) {
		if (inProgress && this.touch == touch)
			state = State.Failed
	}
}