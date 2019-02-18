package pl.shockah.godwit.tree

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.utils.viewport.ScreenViewport
import com.badlogic.gdx.utils.viewport.Viewport

open class Stage(
		val viewport: Viewport = ScreenViewport()
) {
	val root = Node()

	private val renderers = TreeNodeRenderers()

	init {
		viewport.update(Gdx.graphics.width, Gdx.graphics.height, true)
	}

	open fun draw() {
		if (!root.visible)
			return

		val camera = viewport.camera
		camera.update()
		renderers.projectionMatrix = camera.combined

		root.draw(renderers, null)
		renderers.currentPassZLayers.forEach { zLayer, nodes ->
			nodes.forEach { it.draw(renderers, zLayer) }
		}
		renderers.currentPassZLayers.clear()

		renderers.flush()
	}

	open fun update(delta: Float) {
		root.update(delta)
	}
}