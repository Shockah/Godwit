package pl.shockah.godwit.test

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.TextureRegion
import pl.shockah.godwit.Alignment
import pl.shockah.godwit.applyEach
import pl.shockah.godwit.assetDescriptor
import pl.shockah.godwit.node.NodeGame
import pl.shockah.godwit.node.Stage
import pl.shockah.godwit.node.StageLayer
import pl.shockah.godwit.node.TextNode

class TextAlignTest : NodeGame({ Stage(object : StageLayer() {
	private val manager = AssetManager()

	private val textNodes: List<TextNode>

	init {
		manager.load(Assets.fontTexture)
		manager.finishLoading()

		val font = BitmapFont(Gdx.files.internal("5x7.fnt"), TextureRegion(manager.get(Assets.fontTexture)))
		textNodes = Alignment.Horizontal.values().flatMap { h ->
			Alignment.Vertical.values().map { v ->
				TextNode(font).apply {
					alignment = h + v
					text = "${h.name} + ${v.name}"
					root += this
				}
			}
		}
	}

	override fun update(delta: Float) {
		textNodes.applyEach {
			position.x = Gdx.graphics.width * alignment.multiplier.x
			position.y = Gdx.graphics.height * (1f - alignment.multiplier.y)
		}
		super.update(delta)
	}
}) }) {
	private object Assets {
		val fontTexture = assetDescriptor<Texture>("5x7.png")
	}
}