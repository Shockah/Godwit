package pl.shockah.godwit.geom

import groovy.transform.CompileStatic
import pl.shockah.godwit.animfx.ease.Easable
import pl.shockah.godwit.animfx.ease.Easing
import pl.shockah.godwit.geom.polygon.Polygon
import pl.shockah.godwit.geom.polygon.Polygonable
import pl.shockah.godwit.gl.Gfx

import javax.annotation.Nonnull
import javax.annotation.Nullable

@CompileStatic
class Circle extends Shape implements Polygonable, Shape.Filled, Shape.Outline, Easable<Circle> {
	@Nonnull Vec2 position
	float radius

	@Nullable protected Vec2 lastPos = null
	protected int lastPrecision = -1
	@Nullable protected Polygon lastPoly = null

	Circle(float x, float y, float radius) {
		this(new Vec2(x, y), radius)
	}

	Circle(float radius) {
		this(new Vec2(), radius)
	}

	Circle(@Nonnull IVec2 position, float radius) {
		this.position = position.mutableCopy
		this.radius = radius
	}

	@Override
	@Nonnull Shape copy() {
		return copyCircle()
	}

	@Nonnull Circle copyCircle() {
		return new Circle(position, radius)
	}

	@Override
	boolean equals(Object obj) {
		if (!(obj instanceof Circle))
			return false
		Circle circle = obj as Circle
		return position == circle.position && radius == circle.radius
	}

	@Override
	int hashCode() {
		return position.hashCode() * 31 + Float.hashCode(radius)
	}

	@Override
	String toString() {
		return String.format("[Circle: %s, radius %.2f]", position, radius)
	}

	@Override
	@Nonnull Rectangle getBoundingBox() {
		return Rectangle.centered(position, radius * 2f as float)
	}

	@Override
	void translate(float x, float y) {
		position.x += x
		position.y += y
	}

	@Override
	boolean contains(float x, float y) {
		return position.minus(x, y).length <= radius
	}

	@Override
	protected boolean collides(@Nonnull Shape shape, boolean tryAgain) {
		//TODO: Circle --><-- Line
		//TODO: Circle --><-- Rectangle
		if (shape instanceof Circle)
			return collides(shape as Circle)
		return super.collides(shape, tryAgain)
	}

	boolean collides(@Nonnull Circle circle) {
		return (circle.position - position).length < radius + circle.radius
	}

	@Override
	@Nonnull Polygon asPolygon() {
		return asPolygon(Math.ceil(Math.PI * radius * 0.5f) as int)
	}

	@Nonnull Polygon asPolygon(int precision) {
		if (lastPoly && lastPoly.pointCount == precision && lastPrecision == precision && position == lastPos)
			return lastPoly

		Polygon p = new Polygon.NoHoles()
		for (int i in 0..<precision) {
			p.addPoint(Vec2.angled(radius, 360f / precision * i as float) + position)
		}

		lastPos = position
		lastPrecision = precision
		return lastPoly = p
	}

	@Override
	void drawFilled(@Nonnull Gfx gfx, float x, float y) {
		asPolygon().drawFilled(gfx, x, y)
	}

	@Override
	void drawOutline(@Nonnull Gfx gfx, float x, float y) {
		asPolygon().drawOutline(gfx, x, y)
	}

	@Override
	@Nonnull Circle ease(@Nonnull Circle other, float f) {
		return new Circle(position.ease(other.position, f), Easing.linear.ease(radius, other.radius, f))
	}
}