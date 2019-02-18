package pl.shockah.godwit.render

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

	var projectionMatrix: Matrix4 by Delegates.observable(Matrix4()) { _, _, new ->
//		if (old notEquals new)
//			return@observable

		currentRenderer?.end()
		spriteBatch.projectionMatrix = new
		shapeRenderer.projectionMatrix = new
		currentRenderer?.begin()
	}

	var transformMatrix: Matrix4 by Delegates.observable(Matrix4()) { _, _, new ->
//		if (old notEquals new)
//			return@observable

		currentRenderer?.end()
		spriteBatch.transformMatrix = new
		shapeRenderer.transformMatrix = new
		currentRenderer?.begin()
	}

	@PublishedApi
	internal fun begin(renderer: Renderer<*>) {
		if (renderer != currentRenderer) {
			currentRenderer?.end()
			renderer.begin()
			currentRenderer = renderer
		}
	}

	@PublishedApi
	internal inline fun <R : Any, RR : Renderer<R>> wrapRendering(renderer: RR, renderingCode: R.(renderer : R) -> Unit) {
		begin(renderer)
		renderingCode(renderer.renderer, renderer.renderer)
	}

	inline fun sprites(renderingCode: SpriteBatch.(batch: SpriteBatch) -> Unit) {
		wrapRendering(Renderer.Sprite(spriteBatch), renderingCode)
	}

	inline fun shapes(shapeType: ShapeRenderer.ShapeType, renderingCode: ShapeRenderer.(batch: ShapeRenderer) -> Unit) {
		wrapRendering(Renderer.Shape(shapeRenderer, shapeType), renderingCode)
	}

	fun flush() {
		currentRenderer?.apply {
			end()
			currentRenderer = null
		}
	}
}