package pl.shockah.godwit.test

import com.badlogic.gdx.utils.viewport.ScreenViewport
import pl.shockah.godwit.color.RGBColor
import pl.shockah.godwit.geom.ImmutableVec2
import pl.shockah.godwit.geom.Rectangle
import pl.shockah.godwit.node.Stage
import pl.shockah.godwit.node.StageLayer
import pl.shockah.godwit.node.NodeGame
import pl.shockah.godwit.node.asFilledNode

private val rect = Rectangle(ImmutableVec2(-5000f, -5000f), ImmutableVec2(10000f, 10000f))

class UiViewportTest : NodeGame({ object : Stage(
		StageLayer(ScreenViewport()).apply {
			root += rect.asFilledNode().apply { color = RGBColor(1f, 0.5f, 0f).alpha() }
			root += Rectangle(size = ImmutableVec2(32f, 32f)).asFilledNode()
		},
		StageLayer(ScreenViewport()).apply {
			root += rect.asFilledNode().apply { color = RGBColor(0.5f, 0.5f, 0.5f).alpha(0.5f) }
		}
) {
	private val uiHeight = 150

	val mainLayer = stageLayers[0]
	val uiLayer = stageLayers[1]

	override fun resize(screenWidth: Int, screenHeight: Int) {
		super.resize(screenWidth, screenHeight)

		mainLayer.viewports[0].apply {
			val height = screenHeight - uiHeight
			update(screenWidth, height, true)
			screenY = uiHeight
		}

		uiLayer.viewports[0].apply {
			screenX = 0
			screenY = 0
			update(screenWidth, uiHeight, true)
		}
	}
} })