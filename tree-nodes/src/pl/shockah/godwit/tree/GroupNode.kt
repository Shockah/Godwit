package pl.shockah.godwit.tree

import com.badlogic.gdx.math.Affine2
import com.badlogic.gdx.math.Matrix4
import pl.shockah.godwit.SnapshotList
import pl.shockah.godwit.applyEach
import pl.shockah.godwit.geom.ImmutableVec2
import java.util.*

open class GroupNode : Node(), Iterable<Node> {
	private val worldTransform = Affine2()
	private val computedTransform = Matrix4()

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
		val oldTransformMatrix = renderers.transformMatrix
		renderers.transformMatrix = computeTransform()
		children.applyEach {
			if (visible)
				draw(renderers)
		}
		renderers.transformMatrix = oldTransformMatrix
	}

	protected fun computeTransform(): Matrix4 {
		val worldTransform = this.worldTransform
		worldTransform.setToTrnRotScl(x + origin.x, y + origin.y, rotation.value, scale.x, scale.y)
		if (origin notEquals ImmutableVec2.ZERO)
			worldTransform.translate(-origin.x, -origin.y)
		computedTransform.set(worldTransform)
		return computedTransform
	}
}