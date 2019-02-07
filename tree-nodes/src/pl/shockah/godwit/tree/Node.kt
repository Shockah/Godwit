package pl.shockah.godwit.tree

import pl.shockah.godwit.geom.Degrees
import pl.shockah.godwit.geom.MutableVec2
import pl.shockah.godwit.geom.Rectangle

open class Node {
	var parent: GroupNode? = null
	var visible = true

	var position = MutableVec2()
	var origin = MutableVec2()
	var size = MutableVec2()
	var scale = MutableVec2(1f, 1f)
	var rotation = Degrees.of(0f)

	var x: Float
		get() = position.x
		set(new) { position.x = new }

	var y: Float
		get() = position.y
		set(new) { position.y = new }

	var bounds: Rectangle
		get() = Rectangle(position, size)
		set(new) {
			position = new.position * scale
			size = new.size * scale
		}

	open fun draw(renderers: Renderers) {
	}
}