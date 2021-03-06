package pl.shockah.godwit.node.gesture

abstract class ContinuousGestureRecognizer : GestureRecognizer() {
	override var state: State
		get() = super.state
		set(value) {
			super.state = value

			if (inProgress || state == State.Detecting) {
				stage.activeContinuousGestureRecognizers += this
				if (inProgress) {
					stage.gestureRecognizers.forEach {
						if (it != this && it !is ContinuousGestureRecognizer)
							it.state = State.Failed
					}
				}
			} else if (finished) {
				stage.activeContinuousGestureRecognizers -= this
			}
		}

	internal open fun onTouchUsedByRecognizer(touch: Touch) {
	}
}