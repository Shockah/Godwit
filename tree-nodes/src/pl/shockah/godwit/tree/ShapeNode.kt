package pl.shockah.godwit.tree

import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import pl.shockah.godwit.color.GAlphaColor
import pl.shockah.godwit.geom.Shape

abstract class ShapeNode<S : Shape>(
		val shape: S
) : Node() {
	var color = GAlphaColor.white

	open class Filled<S : Shape.Filled>(
			shape: S
	) : ShapeNode<S>(shape) {
		override fun drawSelf(renderers: TreeNodeRenderers) {
			val c = color
			renderers.shapes(ShapeRenderer.ShapeType.Filled) {
				color = c.gdx
				shape.drawFilled(this)
			}
		}
	}

	open class Outline<S : Shape.Outline>(
			shape: S
	) : ShapeNode<S>(shape) {
		override fun drawSelf(renderers: TreeNodeRenderers) {
			val c = color
			renderers.shapes(ShapeRenderer.ShapeType.Filled) {
				color = c.gdx
				shape.drawOutline(this)
			}
		}
	}
}