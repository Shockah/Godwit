package pl.shockah.godwit.geom

import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import pl.shockah.godwit.color.GAlphaColor
import pl.shockah.godwit.color.godwit
import pl.shockah.godwit.ease.Easable
import pl.shockah.godwit.geom.polygon.ClosedPolygon
import pl.shockah.godwit.geom.polygon.Polygonable

class Rectangle(
		position: Vec2 = ImmutableVec2.ZERO,
		size: Vec2
) : Polygonable.Closed, Easable<Rectangle> {
	var position: MutableVec2 = position.mutableCopy()
	var size: MutableVec2 = size.mutableCopy()

	var topLeftColor: GAlphaColor? = null
	var topRightColor: GAlphaColor? = null
	var bottomLeftColor: GAlphaColor? = null
	var bottomRightColor: GAlphaColor? = null

	override val boundingBox: Rectangle
		get() = copy()

	override val center: Vec2
		get() = position + size * 0.5f

	val left: Float
		get() = position.x

	val right: Float
		get() = position.x + size.x

	val top: Float
		get() = position.y

	val bottom: Float
		get() = position.y + size.y

	val topLeft: Vec2
		get() = position

	val topRight: Vec2
		get() = vec2(position.x + size.x, position.y)

	val bottomLeft: Vec2
		get() = vec2(position.x, position.y + size.y)

	val bottomRight: Vec2
		get() = position + size

	val lines: List<Line>
		get() = listOf(
				Line(topLeft, topRight),
				Line(topRight, bottomRight),
				Line(bottomRight, bottomLeft),
				Line(bottomLeft, topLeft)
		)

	companion object {
		fun centered(position: Vec2, size: Vec2): Rectangle {
			return Rectangle(position - size * 0.5f, size)
		}

		init {
			Shape.registerCollisionHandler { a: Rectangle, b: Rectangle ->
				a.position.x < b.position.x + b.size.x
						&& a.position.x + a.size.x > b.position.x
						&& a.position.y < b.position.y + b.size.y
						&& a.position.y + a.size.y > b.position.y
			}
			Shape.registerCollisionHandler { rectangle: Rectangle, line: Line ->
				if (line.point1 in rectangle || line.point2 in rectangle)
					return@registerCollisionHandler true
				for (rectangleLine in rectangle.lines) {
					if (line collides rectangleLine)
						return@registerCollisionHandler true
				}
				return@registerCollisionHandler false
			}
		}
	}

	override fun copy(): Rectangle {
		return Rectangle(position, size)
	}

	override fun equals(other: Any?): Boolean {
		return other is Rectangle && other.position == position && other.size == size
	}

	override fun hashCode(): Int {
		return position.hashCode() * 31 + size.hashCode()
	}

	override fun translate(vector: Vec2) {
		position.xy += vector
	}

	override fun mirror(horizontal: Boolean, vertical: Boolean) {
		if (horizontal)
			position.x = -position.x - size.x
		if (vertical)
			position.y = -position.y - size.y
	}

	override fun scale(scale: Float) {
		position.xy *= scale
		size.xy *= scale
	}

	override operator fun contains(point: Vec2): Boolean {
		return point.x in left..right && point.y in top..bottom
	}

	override fun asClosedPolygon(): ClosedPolygon {
		return ClosedPolygon(topLeft, topRight, bottomRight, bottomLeft)
	}

	override fun ease(other: Rectangle, f: Float): Rectangle {
		return Rectangle(
				position.ease(other.position, f),
				size.ease(other.size, f)
		)
	}

	private fun draw(shapes: ShapeRenderer) {
		if (topLeftColor != null || topRightColor != null || bottomLeftColor != null || bottomRightColor != null) {
			val baseColor = shapes.color.godwit
			val topLeftColor = this.topLeftColor ?: baseColor
			val topRightColor = this.topRightColor ?: baseColor
			val bottomLeftColor = this.bottomLeftColor ?: baseColor
			val bottomRightColor = this.bottomRightColor ?: baseColor
			shapes.rect(position.x, position.y, size.x, size.y, topLeftColor.gdx, topRightColor.gdx, bottomRightColor.gdx, bottomLeftColor.gdx)
		} else {
			shapes.rect(position.x, position.y, size.x, size.y)
		}
	}

	override fun drawFilled(shapes: ShapeRenderer) {
		draw(shapes)
	}

	override fun drawOutline(shapes: ShapeRenderer) {
		draw(shapes)
	}
}