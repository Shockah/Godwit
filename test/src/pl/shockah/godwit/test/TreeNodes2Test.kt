package pl.shockah.godwit.test

import com.badlogic.gdx.Gdx
import pl.shockah.godwit.geom.ImmutableVec2
import pl.shockah.godwit.geom.Line
import pl.shockah.godwit.geom.degrees
import pl.shockah.godwit.size
import pl.shockah.godwit.tree.Node
import pl.shockah.godwit.tree.Stage
import pl.shockah.godwit.tree.TreeNodeGame
import pl.shockah.godwit.tree.asOutlineNode

class TreeNodes2Test : TreeNodeGame({ object : Stage() {
	val lines = Array(5) { Line(ImmutableVec2(0f, 0f), ImmutableVec2(48f, 0f)).asOutlineNode() }

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
			lines[i].rotation += ((30f + i.toFloat() * 5f) * delta).degrees
		}
		super.update(delta)
	}
}})