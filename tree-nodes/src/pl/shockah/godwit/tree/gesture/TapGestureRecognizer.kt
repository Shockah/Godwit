package pl.shockah.godwit.tree.gesture

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.utils.Timer
import pl.shockah.godwit.geom.Vec2

class TapGestureRecognizer(
		val tapsRequired: Int = 1,
		val delay: Float = 0f,
		val delegate: (recognizer: TapGestureRecognizer, touch: Touch) -> Unit
) : GestureRecognizer() {
	var touch: Touch? = null

	private var timerTask: Timer.Task? = null

	private var taps = 0

	override var state: State
		get() = super.state
		set(value) {
			super.state = value

			if (finished) {
				touch = null
				taps = 0
				timerTask?.cancel()
				timerTask = null
			}
		}

	override fun toString(): String {
		return "[${this::class.simpleName}:$tapsRequired]"
	}

	override fun onRequiredFailFailed(recognizer: GestureRecognizer) {
		val touch = touch

		if (touch != null && taps >= tapsRequired) {
			if (!requiredRecognizersFailed)
				return
			if (touch.recognizer != null) {
				state = State.Failed
				return
			}
			touch.recognizer = this
			state = State.Ended
			delegate(this, touch)
		}
	}

	override fun handleTouchDown(touch: Touch, point: Vec2) {
		if (state == State.Possible || inProgress) {
			if (inProgress) {
				state = State.Changed
				taps++
			} else {
				state = State.Began
				taps = 1
			}
			this.touch = touch

			timerTask?.cancel()
			if (delay > 0f) {
				timerTask = Timer.schedule(object : Timer.Task() {
					override fun run() {
						Gdx.app.postRunnable {
							state = State.Failed
							this@TapGestureRecognizer.touch = null
							taps = 0
							timerTask = null
						}
					}
				}, delay)
			} else {
				timerTask = null
			}
		}
	}

	override fun handleTouchUp(touch: Touch, point: Vec2) {
		if (inProgress && this.touch == touch && taps >= tapsRequired) {
			if (!requiredRecognizersFailed)
				return
			if (touch.recognizer != null) {
				state = State.Failed
				return
			}
			touch.recognizer = this
			state = State.Ended
			delegate(this, touch)
		}
	}
}