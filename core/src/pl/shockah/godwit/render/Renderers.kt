package pl.shockah.godwit.render

import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShaderProgram
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.Matrix4
import com.badlogic.gdx.utils.Disposable
import pl.shockah.godwit.GDelegates
import pl.shockah.godwit.LazyInspectable
import kotlin.properties.Delegates

open class Renderers : Disposable {
	private val defaultShaderInspectable = LazyInspectable<ShaderProgram> { SpriteBatch.createDefaultShader() }
	val defaultShader by defaultShaderInspectable

	@PublishedApi
	internal val spriteBatch = SpriteBatch(4096, defaultShader)

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

	var shader: ShaderProgram by GDelegates.changeObservable(defaultShader) { _, _, new ->
		currentRenderer?.end()
		spriteBatch.shader = new
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

	override fun dispose() {
		if (defaultShaderInspectable.initialized)
			defaultShader.dispose()
	}
}