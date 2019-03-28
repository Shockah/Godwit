package pl.shockah.godwit.node

import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import pl.shockah.godwit.color.RGBColor
import pl.shockah.godwit.geom.Shape

inline fun <reified S : Shape.Filled> S.asFilledNode(): ShapeNode.Filled<S> {
	return ShapeNode.Filled(this)
}

inline fun <reified S : Shape.Outline> S.asOutlineNode(): ShapeNode.Outline<S> {
	return ShapeNode.Outline(this)
}

abstract class ShapeNode<S : Shape>(
		val shape: S
) : Node() {
	var color = RGBColor.white.alpha()

	class Filled<S : Shape.Filled>(
			shape: S
	) : ShapeNode<S>(shape) {
		init {
			touchShape = shape
		}

		override fun drawSelf(renderers: NodeRenderers) {
			val c = color
			renderers.shapes(ShapeRenderer.ShapeType.Filled) {
				color = c.gdx
				shape.drawFilled(this)
			}
		}
	}

	class Outline<S : Shape.Outline>(
			shape: S
	) : ShapeNode<S>(shape) {
		override fun drawSelf(renderers: NodeRenderers) {
			val c = color
			renderers.shapes(ShapeRenderer.ShapeType.Line) {
				color = c.gdx
				shape.drawOutline(this)
			}
		}
	}
}