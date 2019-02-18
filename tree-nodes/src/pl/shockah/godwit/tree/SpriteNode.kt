package pl.shockah.godwit.tree

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch.*
import com.badlogic.gdx.graphics.g2d.TextureRegion
import pl.shockah.godwit.color.GAlphaColor
import pl.shockah.godwit.geom.ObservableVec2
import kotlin.properties.Delegates

open class SpriteNode(
		val region: TextureRegion
) : Node() {
	constructor(texture: Texture) : this(TextureRegion(texture))

	companion object {
		const val VERTEX_SIZE = 2 + 1 + 2
		const val SPRITE_SIZE = 4 * VERTEX_SIZE
	}

	val texture: Texture
		get() = region.texture

	var size: ObservableVec2 by ObservableVec2.property { setupPoints() }

	var color: GAlphaColor by Delegates.observable(GAlphaColor.white) { _, old, new ->
		if (old != new)
			setupColor()
	}

	private val vertices = FloatArray(SPRITE_SIZE)

	init {
		size.x = region.regionWidth.toFloat()
		size.y = region.regionHeight.toFloat()
		origin.x = size.x * 0.5f
		origin.y = size.y * 0.5f
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
		vertices[X2] = x1
		vertices[Y2] = y2
		vertices[X3] = x2
		vertices[Y3] = y2
		vertices[X4] = x2
		vertices[Y4] = y1
	}

	private fun setupColor() {
		val colorBits = color.gdx.toFloatBits()
		vertices[C1] = colorBits
		vertices[C2] = colorBits
		vertices[C3] = colorBits
		vertices[C4] = colorBits
	}

	override fun drawSelf(renderers: TreeNodeRenderers) {
		renderers.sprites {
			draw(texture, vertices, 0, SPRITE_SIZE)
		}
	}
}