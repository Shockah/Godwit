package pl.shockah.godwit.tree.gesture

import pl.shockah.godwit.geom.Vec2
import pl.shockah.godwit.tree.Stage
import pl.shockah.godwit.tree.StageLayer
import kotlin.properties.Delegates

abstract class GestureRecognizer : StageLayer.StageAwaiter {
	private var backingStage: Stage? by Delegates.observable(null) { _, old: Stage?, new: Stage? ->
		if (old != null)
			old.gestureRecognizers -= this
		if (new != null)
			new.gestureRecognizers += this
	}

	var stage: Stage
		get() = backingStage!!
		set(value) { backingStage = value }

	open var state: State by Delegates.observable(State.Possible) { _, old, state ->
		if (state == old)
			return@observable

		if (state == State.Ended) {
			failListeners.forEach { it.onRequiredFailEnded(this) }
		} else if (state == State.Failed) {
			failListeners.forEach { it.onRequiredFailFailed(this) }
		}
	}

	val inProgress: Boolean
		get() = state == State.Began || state == State.Changed

	val finished: Boolean
		get() = state == State.Ended || state == State.Failed || state == State.Cancelled

	val requiredRecognizersFailed: Boolean
		get() {
			for (recognizer in requireToFail) {
				if (recognizer.state != State.Failed)
					return false
			}
			return true
		}

	protected val requireToFail = mutableSetOf<GestureRecognizer>()
	protected val failListeners = mutableSetOf<GestureRecognizer>()

	override fun toString(): String {
		return "[${this::class.simpleName}]"
	}

	fun cancel() {
		if (state != State.Possible)
			state = State.Cancelled
	}

	fun requireToFail(recognizer: GestureRecognizer) {
		if (recognizer === this)
			throw IllegalArgumentException("Cannot require itself to fail.")
		if (recognizer.requireToFail.contains(this))
			throw IllegalArgumentException("Detected circular failure GestureRecognizer dependency.")

		requireToFail += recognizer
		recognizer.failListeners += this
	}

	protected open fun onRequiredFailEnded(recognizer: GestureRecognizer) {
		state = State.Failed
	}

	protected open fun onRequiredFailFailed(recognizer: GestureRecognizer) {
	}

	internal open fun handleTouchDown(touch: Touch, point: Vec2) {
	}

	internal open fun handleTouchDragged(touch: Touch, point: Vec2) {
	}

	internal open fun handleTouchUp(touch: Touch, point: Vec2) {
	}

	override fun onStageSet(stage: Stage) {
		this.stage = stage
	}

	enum class State {
		Possible, Detecting, Began, Changed, Failed, Cancelled, Ended
	}
}