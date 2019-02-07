package pl.shockah.godwit.tree

import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.Matrix4
import kotlin.properties.Delegates

class Renderers {
	@PublishedApi
	internal val spriteBatch = SpriteBatch()

	@PublishedApi
	internal val shapeRenderer = ShapeRenderer()

	private var currentRenderer: Renderer<*>? = null

	var projectionMatrix: Matrix4 by Delegates.observable(Matrix4()) { _, old, new ->
		if (old == new)
			return@observable

		currentRenderer?.end()
		spriteBatch.projectionMatrix = new
		shapeRenderer.projectionMatrix = new
		currentRenderer?.begin()
	}

	var transformMatrix: Matrix4 by Delegates.observable(Matrix4()) { _, old, new ->
		if (old == new)
			return@observable

		currentRenderer?.end()
		spriteBatch.transformMatrix = new
		shapeRenderer.transformMatrix = new
		currentRenderer?.begin()
	}

	private fun begin(renderer: Renderer<*>) {
		if (renderer != currentRenderer) {
			currentRenderer?.end()
			renderer.begin()
			currentRenderer = renderer
		}
	}

	inline fun sprites(renderingCode: (batch: SpriteBatch) -> Unit) {
		val renderer = Renderer.Sprite(spriteBatch)
		renderer.begin()
		renderingCode(renderer.renderer)
	}

	inline fun shapes(shapeType: ShapeRenderer.ShapeType, renderingCode: (batch: ShapeRenderer) -> Unit) {
		val renderer = Renderer.Shape(shapeRenderer, shapeType)
		renderer.begin()
		renderingCode(renderer.renderer)
	}

	fun flush() {
		currentRenderer?.apply {
			end()
			currentRenderer = null
		}
	}
}