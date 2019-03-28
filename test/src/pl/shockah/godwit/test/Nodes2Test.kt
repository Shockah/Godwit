package pl.shockah.godwit.test

import com.badlogic.gdx.Gdx
import pl.shockah.godwit.geom.ImmutableVec2
import pl.shockah.godwit.geom.Line
import pl.shockah.godwit.geom.vec2
import pl.shockah.godwit.node.*
import pl.shockah.godwit.size

class Nodes2Test : NodeGame({ Stage(object : StageLayer() {
	val lines = Array(5) { Line(vec2(0f, 0f), vec2(48f, 0f)).asOutlineNode() }

	init {
		var current: Node = root
		for (i in 0 until lines.size) {
			lines[i].parent = current
			if (i != 0)
				lines[i].position.x = 48f
			current = lines[i]
		}
	}

	override fun update(delta: Float) {
		lines[0].position.set(Gdx.graphics.size * 0.5f)
		for (i in 0 until lines.size) {
			lines[i].rotation += (30f + i.toFloat() * 5f) * delta
		}
		super.update(delta)
	}
}) })