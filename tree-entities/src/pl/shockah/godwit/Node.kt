package pl.shockah.godwit

import pl.shockah.godwit.geom.ImmutableVec2
import pl.shockah.godwit.geom.MutableVec2
import pl.shockah.godwit.geom.Vec2

open class Node : GameEntity {
	var position: MutableVec2 = MutableVec2()

	open fun applyToContext(context: RenderContext): RenderContext {
		if (position equals ImmutableVec2.ZERO)
			return context
		return RenderContext(context.position + position)
	}

	open fun render(context: RenderContext) {
	}

	data class RenderContext(
			val position: Vec2 = ImmutableVec2.ZERO
	)
}