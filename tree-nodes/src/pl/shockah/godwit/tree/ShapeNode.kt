package pl.shockah.godwit.tree

import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import pl.shockah.godwit.color.GAlphaColor
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