package pl.shockah.godwit.geom

import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import pl.shockah.godwit.geom.polygon.Polygonable
import kotlin.reflect.KClass
import kotlin.reflect.full.isSubclassOf
import kotlin.reflect.full.superclasses

interface Shape {
	val boundingBox: Rectangle

	val center: Vec2
		get() = boundingBox.center

	companion object {
		val none = object : Shape.Filled {
			override val boundingBox: Rectangle
				get() = Rectangle(size = ImmutableVec2(0f, 0f))

			override fun copy(): Shape.Filled {
				return this
			}

			override fun translate(vector: Vec2) {
			}

			override fun mirror(horizontal: Boolean, vertical: Boolean) {
			}

			override fun scale(scale: Float) {
			}

			override fun drawFilled(shapes: ShapeRenderer) {
			}

			override fun contains(point: Vec2): Boolean {
				return false
			}
		}

		val infinitePlane = object : Shape.Filled {
			override val boundingBox: Rectangle
				get() = Rectangle(
						ImmutableVec2(-Float.MAX_VALUE, -Float.MAX_VALUE),
						ImmutableVec2(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY)
				)

			override fun copy(): Shape.Filled {
				return this
			}

			override fun translate(vector: Vec2) {
			}

			override fun mirror(horizontal: Boolean, vertical: Boolean) {
			}

			override fun scale(scale: Float) {
			}

			override fun drawFilled(shapes: ShapeRenderer) {
				shapes.rect(
						-Float.MAX_VALUE, -Float.MAX_VALUE,
						Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY
				)
			}

			override fun contains(point: Vec2): Boolean {
				return true
			}
		}

		private val collisionHandlers: MutableMap<Pair<KClass<out Shape>, KClass<out Shape>>, (Shape, Shape) -> Boolean> = mutableMapOf()

		fun <A : Shape, B : Shape> registerCollisionHandler(first: KClass<A>, second: KClass<B>, handler: (A, B) -> Boolean) {
			@Suppress("UNCHECKED_CAST")
			collisionHandlers[Pair(first, second)] = handler as ((Shape, Shape) -> Boolean)
		}

		fun <A : Shape, B : Shape> shapesCollide(first: A, second: B): Boolean {
			val handler = findHandler(first::class, second::class)
			if (handler != null)
				return handler(first, second)

			if (first is Polygonable.Closed && second is Polygonable.Closed)
				return first.asClosedPolygon() collides second.asClosedPolygon()

			if (first is Polygonable.Open && second is Polygonable.Open)
				return first.asPolygon() collides second.asPolygon()

			throw UnsupportedOperationException("${first::class.simpleName} --><-- ${second::class.simpleName} collision isn't implemented.")
		}

		private fun findHandler(first: KClass<out Shape>, second: KClass<out Shape>): ((Shape, Shape) -> Boolean)? {
			return collisionHandlers[Pair(first, second)] ?: collisionHandlers[Pair(second, first)] ?: findHandlerFromSupertypes(first, second)
		}

		private fun findHandlerFromSupertypes(first: KClass<out Shape>, second: KClass<out Shape>): ((Shape, Shape) -> Boolean)? {
			for (firstSubclass in first.superclasses.filter { it != Shape::class && it.isSubclassOf(Shape::class) }.map {
				@Suppress("UNCHECKED_CAST")
				it as KClass<out Shape>
			}) {
				val result = findHandler(firstSubclass, second)
				if (result != null)
					return result
			}
			for (secondSubclass in second.superclasses.filter { it != Shape::class && it.isSubclassOf(Shape::class) }.map {
				@Suppress("UNCHECKED_CAST")
				it as KClass<out Shape>
			}) {
				val result = findHandler(first, secondSubclass)
				if (result != null)
					return result
			}
			return null
		}
	}

	fun copy(): Shape

	fun translate(vector: Vec2)

	fun mirror(horizontal: Boolean, vertical: Boolean)

	fun scale(scale: Float)

	infix fun collides(other: Shape): Boolean {
		return Shape.shapesCollide(this, other)
	}

	interface Filled : Shape {
		fun drawFilled(shapes: ShapeRenderer)

		operator fun contains(point: Vec2): Boolean
	}

	interface Outline : Shape {
		fun drawOutline(shapes: ShapeRenderer)
	}
}