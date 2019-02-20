package pl.shockah.godwit.tree.gesture

import pl.shockah.godwit.geom.ImmutableVec2
import pl.shockah.godwit.geom.Vec2
import java.util.*
import kotlin.properties.Delegates

class Touch(
		val pointer: Int,
		val button: Int
) {
	private val mutablePoints = mutableListOf<Point>()

	var recognizer: GestureRecognizer? by Delegates.observable(null) { _, old: GestureRecognizer?, new: GestureRecognizer? ->
		if (new == old)
			return@observable

		if (new != null) {
//			for (continuousRecognizer in ArrayList(Godwit.getInstance().inputManager.gestureManager.currentContinuousRecognizers)) {
//				if (continuousRecognizer === recognizer)
//					continue
//				continuousRecognizer.onTouchUsedByRecognizer(this)
//			}
		}
	}

	var finished = false
		private set

	val points: List<Point>
		get() = mutablePoints

	fun finish() {
		finished = true
	}

	fun addPoint(point: Vec2) {
		mutablePoints += Point(point.immutable(), Date())
	}

	operator fun plusAssign(point: Vec2) {
		addPoint(point)
	}

	data class Point(
			val position: ImmutableVec2,
			val date: Date
	)
}