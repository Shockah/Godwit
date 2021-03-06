package pl.shockah.godwit.test

import com.badlogic.gdx.Gdx
import pl.shockah.godwit.color.HSLColor
import pl.shockah.godwit.geom.Degrees
import pl.shockah.godwit.geom.ImmutableVec2
import pl.shockah.godwit.geom.Rectangle
import pl.shockah.godwit.geom.vec2
import pl.shockah.godwit.node.*
import pl.shockah.godwit.node.gesture.TapGestureRecognizer
import pl.shockah.godwit.size

class NestedNodeGestureTest : NodeGame({ Stage(object : StageLayer() {
	val nodes = Array(3) { Rectangle(size = vec2(64f, 64f)).asFilledNode() }

	init {
		var current: Node = root
		for (i in 0 until nodes.size) {
			nodes[i].gestureRecognizers += TapGestureRecognizer { _, _ -> println("Node $i") }
			nodes[i].parent = current
			nodes[i].color = HSLColor(Degrees.of(360f / nodes.size * i), 0.7f, 0.7f).alpha()
			if (i != 0)
				nodes[i].position = nodes[i].shape.boundingBox.size
			current = nodes[i]
		}
	}

	override fun update(delta: Float) {
		nodes[0].position.set(Gdx.graphics.size * 0.5f - vec2(64f, 64f))
		for (i in 1 until nodes.size) {
			nodes[i].rotation += delta * 100f
		}
		super.update(delta)
	}
}) })