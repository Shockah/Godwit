package pl.shockah.godwit.node

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.BitmapFontCache
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

	private val dirtyCache = LazyDirty {
		val layout = GlyphLayout(font, text, Color.WHITE, maxWidth ?: 0f, Align.left, lineBreakMode == LineBreakMode.Wrap)
		val cache = BitmapFontCache(font)
		cache.addText(text, -layout.width * alignment.multiplier.x, layout.height * alignment.multiplier.y, maxWidth ?: 0f, Align.left, lineBreakMode == LineBreakMode.Wrap)
		return@LazyDirty cache
	}
	private val cache: BitmapFontCache by dirtyCache

	var text: String by GDelegates.observable("") { -> dirtyCache.markDirty() }
	var alignment: Alignment by GDelegates.observable(Alignment.Horizontal.Left + Alignment.Vertical.Top) { -> dirtyCache.markDirty() }
	var maxWidth: Float? by GDelegates.observable(null) { -> dirtyCache.markDirty() }
	var lineBreakMode: LineBreakMode by GDelegates.observable(LineBreakMode.Wrap) { -> dirtyCache.markDirty() }

	private val isDistanceField = font is DistanceFieldFont

	override fun drawSelf(renderers: NodeRenderers) {
		if (text.isEmpty())
			return

		renderers.sprites {
			if (isDistanceField)
				renderers.shader = distanceFontShader
			cache.draw(this)
			if (isDistanceField)
				renderers.shader = renderers.defaultShader
		}
	}

	enum class LineBreakMode {
		Wrap, Truncate
	}
}