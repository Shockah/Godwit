package pl.shockah.godwit.tree

import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer

abstract class Renderer<R>(
		val renderer: R
) {
	abstract fun begin()

	abstract fun end()

	class Sprite(
			renderer: SpriteBatch
	) : Renderer<SpriteBatch>(renderer) {
		override fun begin() {
			renderer.begin()
		}

		override fun end() {
			renderer.end()
		}

		override fun equals(other: Any?): Boolean {
			if (other !is Sprite)
				return false
			return renderer == other.renderer
		}

		override fun hashCode(): Int {
			return renderer.hashCode()
		}
	}

	class Shape(
			renderer: ShapeRenderer,
			val shapeType: ShapeRenderer.ShapeType
	) : Renderer<ShapeRenderer>(renderer) {
		override fun begin() {
			renderer.begin(shapeType)
		}

		override fun end() {
			renderer.end()
		}

		override fun equals(other: Any?): Boolean {
			if (other !is Shape)
				return false
			return renderer == other.renderer && shapeType == other.shapeType
		}

		override fun hashCode(): Int {
			return renderer.hashCode() * 31 + shapeType.hashCode()
		}
	}
}