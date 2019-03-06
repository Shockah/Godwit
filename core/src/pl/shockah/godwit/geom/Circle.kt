package pl.shockah.godwit.geom

import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import pl.shockah.godwit.ease.Easable
import pl.shockah.godwit.geom.polygon.Polygon
import kotlin.math.sqrt

class Circle(
		position: Vec2 = ImmutableVec2.ZERO,
		var radius: Float
) : Shape.Filled, Shape.Outline, Easable<Circle> {
	var position: MutableVec2 = position.mutableCopy()

	override val boundingBox: Rectangle
		get() = Rectangle.centered(position, ImmutableVec2(radius * 2, radius * 2))

	override val center: Vec2
		get() = position

	companion object {
		init {
			Shape.registerCollisionHandler(Circle::class, Circle::class) { a, b ->
				(b.position - a.position).length < b.radius + a.radius
			}
			Shape.registerCollisionHandler(Circle::class, Line::class) { circle, line ->
				line.point1 in circle || line.point2 in circle || !(circle intersect line).isEmpty()
			}
			Shape.registerCollisionHandler(Circle::class, Rectangle::class) { circle, rectangle ->
				val testPoint = circle.position.mutableCopy()

				if (circle.position.x < rectangle.position.x)
					testPoint.x = rectangle.position.x
				else if (circle.position.x > rectangle.position.x + rectangle.size.x)
					testPoint.x = rectangle.position.x + rectangle.size.x

				if (circle.position.y < rectangle.position.y)
					testPoint.y = rectangle.position.y
				else if (circle.position.y > rectangle.position.y + rectangle.size.y)
					testPoint.y = rectangle.position.y + rectangle.size.y

				return@registerCollisionHandler (circle.position - testPoint).length < circle.radius
			}
			Shape.registerCollisionHandler(Circle::class, Polygon::class) { circle, polygon ->
				for (line in polygon.lines) {
					if (circle collides line)
						return@registerCollisionHandler true
				}
				return@registerCollisionHandler false
			}
		}
	}

	override fun copy(): Circle {
		return Circle(position, radius)
	}

	override fun equals(other: Any?): Boolean {
		return other is Circle && position == other.position && radius == other.radius
	}

	override fun hashCode(): Int {
		return position.hashCode() * 31 + radius.hashCode()
	}

	override fun translate(vector: Vec2) {
		position.xy += vector
	}

	override fun mirror(horizontal: Boolean, vertical: Boolean) {
		if (horizontal)
			position.x *= -1f
		if (vertical)
			position.y *= -1f
	}

	override fun scale(scale: Float) {
		position.xy *= scale
		radius *= scale
	}

	override operator fun contains(point: Vec2): Boolean {
		return (position - point).length <= radius
	}

	infix fun intersect(line: Line): List<ImmutableVec2> {
		val baX = line.point2.x - line.point1.x
		val baY = line.point2.y - line.point1.y
		val caX = position.x - line.point1.x
		val caY = position.y - line.point1.y

		val a = baX * baX + baY * baY
		val bBy2 = baX * caX + baY * caY
		val c = caX * caX + caY * caY - radius * radius

		val pBy2 = bBy2 / a
		val q = c / a

		val disc = pBy2 * pBy2 - q
		if (disc < 0)
			return emptyList()

		val tmpSqrt = sqrt(disc)
		val abScalingFactor1 = -pBy2 + tmpSqrt
		val abScalingFactor2 = -pBy2 - tmpSqrt

		val p1 = ImmutableVec2(line.point1.x - baX * abScalingFactor1, line.point1.y - baY * abScalingFactor1)
		if (disc == 0f)
			return listOf(p1)

		val p2 = ImmutableVec2(line.point1.x - baX * abScalingFactor2, line.point1.y - baY * abScalingFactor2)
		return listOf(p1, p2)
	}

	override fun ease(other: Circle, f: Float): Circle {
		return Circle(
				position.ease(other.position, f),
				radius.ease(other.radius, f)
		)
	}

	private fun draw(shapes: ShapeRenderer) {
		shapes.circle(position.x, position.y, radius)
	}

	override fun drawFilled(shapes: ShapeRenderer) {
		draw(shapes)
	}

	override fun drawOutline(shapes: ShapeRenderer) {
		draw(shapes)
	}
}