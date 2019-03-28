package pl.shockah.godwit.node

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch.*
import com.badlogic.gdx.graphics.g2d.TextureRegion
import pl.shockah.godwit.GDelegates
import pl.shockah.godwit.color.GAlphaColor
import pl.shockah.godwit.color.RGBColor
import pl.shockah.godwit.geom.MutableVec2
import pl.shockah.godwit.geom.ObservableVec2
import pl.shockah.godwit.geom.Rectangle

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

	protected val observableSize = ObservableVec2 { setupPoints() }
	var size: MutableVec2 by observableSize.asMutableProperty()

	var color: GAlphaColor by GDelegates.observable(RGBColor.white.alpha()) { -> setupColor() }

	var usesRectangleTouchShape: Boolean by GDelegates.observable(true) { -> setupRectangleTouchShape() }

	private val vertices = FloatArray(SPRITE_SIZE)

	init {
		size.x = region.regionWidth.toFloat()
		size.y = region.regionHeight.toFloat()
		origin.x = size.x * 0.5f
		origin.y = size.y * 0.5f

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

	private fun setupRectangleTouchShape() {
		if (usesRectangleTouchShape)
			touchShape = Rectangle(position - origin, size)
	}

	private fun setupPoints() {
		vertices[X1] = 0f
		vertices[Y1] = 0f
		vertices[X2] = 0f
		vertices[Y2] = size.y
		vertices[X3] = size.x
		vertices[Y3] = size.y
		vertices[X4] = size.x
		vertices[Y4] = 0f
		setupRectangleTouchShape()
	}

	private fun setupColor() {
		val colorBits = color.gdx.toFloatBits()
		vertices[C1] = colorBits
		vertices[C2] = colorBits
		vertices[C3] = colorBits
		vertices[C4] = colorBits
	}

	override fun drawSelf(renderers: NodeRenderers) {
		renderers.sprites {
			draw(texture, vertices, 0, SPRITE_SIZE)
		}
	}
}