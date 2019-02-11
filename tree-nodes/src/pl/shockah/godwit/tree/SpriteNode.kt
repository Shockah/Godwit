package pl.shockah.godwit.tree

import com.badlogic.gdx.graphics.g2d.SpriteBatch.*
import com.badlogic.gdx.graphics.g2d.TextureRegion
import pl.shockah.godwit.color.GAlphaColor
import pl.shockah.godwit.geom.ObservableVec2
import kotlin.properties.Delegates

class SpriteNode(
		private val region: TextureRegion
) : Node() {
	companion object {
		const val VERTEX_SIZE = 2 + 1 + 2
		const val SPRITE_SIZE = 4 * VERTEX_SIZE
	}

	var size: ObservableVec2 by Delegates.observable(ObservableVec2()) { _, old, new ->
		if (new !== old) {
			old.listeners.clear()
			new.listeners += { _ -> setupPoints() }
			setupPoints()
		}
	}

	var color: GAlphaColor by Delegates.observable(GAlphaColor.white) { _, old, new ->
		if (old != new)
			setupColor()
	}

	private val vertices = FloatArray(SPRITE_SIZE)

	init {
		size.listeners += { _ -> setupPoints() }

		setupPoints()
		setupUV()
		setupColor()
	}

	private fun setupUV() {
		vertices[U1] = region.u
		vertices[V1] = region.v2

		vertices[U2] = region.u
		vertices[V2] = region.v

		vertices[U3] = region.u2
		vertices[V3] = region.v

		vertices[U4] = region.u2
		vertices[V4] = region.v2
	}

	private fun setupPoints() {
		val x1 = 0f
		val y1 = 0f
		val x2 = size.x
		val y2 = size.y

		vertices[X1] = x1
		vertices[Y1] = y1
		vertices[X2] = x2
		vertices[Y2] = y1
		vertices[X3] = x2
		vertices[Y3] = y2
		vertices[X4] = x1
		vertices[Y4] = y2
	}

	private fun setupColor() {
		val colorBits = color.gdx.toFloatBits()
		vertices[C1] = colorBits
		vertices[C2] = colorBits
		vertices[C3] = colorBits
		vertices[C4] = colorBits
	}

	override fun draw(renderers: Renderers) {
		renderers.sprites {
			draw(region.texture, vertices, 0, SPRITE_SIZE)
		}
	}
}