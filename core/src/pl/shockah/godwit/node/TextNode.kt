package pl.shockah.godwit.node

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.DistanceFieldFont
import com.badlogic.gdx.graphics.g2d.GlyphLayout
import com.badlogic.gdx.graphics.glutils.ShaderProgram
import com.badlogic.gdx.utils.Align
import pl.shockah.godwit.Alignment
import pl.shockah.godwit.GDelegates
import pl.shockah.godwit.LazyDirty

open class TextNode(
		val font: BitmapFont
) : Node() {
	companion object {
		private val distanceFontShader: ShaderProgram by lazy { DistanceFieldFont.createDistanceFieldShader() }
	}

	private val dirtyLayout = LazyDirty {
		GlyphLayout(font, text, Color.WHITE, maxWidth ?: 0f, Align.left, lineBreakMode == LineBreakMode.Wrap)
	}
	val layout: GlyphLayout by dirtyLayout

	var text: String by GDelegates.changeObservable("") { -> dirtyLayout.markDirty() }
	var alignment: Alignment = Alignment.Horizontal.Left + Alignment.Vertical.Top
	var maxWidth: Float? by GDelegates.changeObservable(null) { -> dirtyLayout.markDirty() }
	var lineBreakMode: LineBreakMode by GDelegates.changeObservable(LineBreakMode.Wrap) { -> dirtyLayout.markDirty() }

	private val isDistanceField = font is DistanceFieldFont

	override fun drawSelf(renderers: NodeRenderers) {
		if (text.isEmpty())
			return

		renderers.sprites {
			if (isDistanceField)
				renderers.shader = distanceFontShader
			font.draw(this, text, -layout.width * alignment.multiplier.x, layout.height * alignment.multiplier.y)
			if (isDistanceField)
				renderers.shader = renderers.defaultShader
		}
	}

	enum class LineBreakMode {
		Wrap, Truncate
	}
}