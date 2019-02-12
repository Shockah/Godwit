package pl.shockah.godwit.test

import com.badlogic.gdx.Gdx
import pl.shockah.godwit.geom.ImmutableVec2
import pl.shockah.godwit.geom.Line
import pl.shockah.godwit.geom.degrees
import pl.shockah.godwit.size
import pl.shockah.godwit.tree.GroupNode
import pl.shockah.godwit.tree.ShapeNode
import pl.shockah.godwit.tree.Stage
import pl.shockah.godwit.tree.TreeNodeGame

class TreeNodes2Test : TreeNodeGame({
	object : Stage() {
		val groups = Array(5) { GroupNode() }
		val lines = Array(5) { ShapeNode.Outline(Line(ImmutableVec2(0f, 0f), ImmutableVec2(48f, 0f))) }

		init {
			var current: GroupNode = root
			for (i in 0 until groups.size) {
				current.children.add(groups[i])
				groups[i].children.add(lines[i])

				if (i != 0)
					groups[i].position.x = 48f

				current = groups[i]
			}
		}

		override fun update(delta: Float) {
			groups[0].position.set(Gdx.graphics.size * 0.5f)
			for (i in 0 until groups.size) {
				groups[i].rotation += ((30f + i.toFloat() * 5f) * delta).degrees
			}
			super.update(delta)
		}
	}
})