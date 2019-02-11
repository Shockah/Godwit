package pl.shockah.godwit.tree

import pl.shockah.godwit.SnapshotList
import pl.shockah.godwit.applyEach
import java.util.*

open class GroupNode : Node(), Iterable<Node> {
	val children = SnapshotList(LinkedList<Node>())

	operator fun plusAssign(element: Node) {
		children += element
	}

	operator fun minusAssign(element: Node) {
		children -= element
	}

	override fun iterator(): Iterator<Node> {
		return children.iterator()
	}

	override fun draw(renderers: Renderers) {
		children.applyEach {
			if (visible)
				applyTransformationsAndDraw(renderers)
		}
	}
}