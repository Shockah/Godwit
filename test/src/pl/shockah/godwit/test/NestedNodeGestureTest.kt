package pl.shockah.godwit.test

import com.badlogic.gdx.Gdx
import pl.shockah.godwit.color.HSLColor
import pl.shockah.godwit.geom.ImmutableVec2
import pl.shockah.godwit.geom.Rectangle
import pl.shockah.godwit.geom.degrees
import pl.shockah.godwit.size
import pl.shockah.godwit.node.*
import pl.shockah.godwit.node.gesture.TapGestureRecognizer

class NestedNodeGestureTest : NodeGame({ Stage(object : StageLayer() {
	val nodes = Array(3) { Rectangle(size = ImmutableVec2(64f, 64f)).asFilledNode() }

	init {
		var current: Node = root
		for (i in 0 until nodes.size) {
			nodes[i].gestureRecognizers += TapGestureRecognizer { _, _ -> println("Node $i") }
			nodes[i].parent = current
			nodes[i].color = HSLColor((360f / nodes.size * i).degrees, 0.7f, 0.7f).alpha()
			if (i != 0)
				nodes[i].position = nodes[i].shape.boundingBox.size
			current = nodes[i]
		}
	}

	override fun update(delta: Float) {
		nodes[0].position.set(Gdx.graphics.size * 0.5f - ImmutableVec2(64f, 64f))
		for (i in 1 until nodes.size) {
			nodes[i].rotation += (delta * 100f).degrees
		}
		super.update(delta)
	}
}) })