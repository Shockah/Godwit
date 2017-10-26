package pl.shockah.godwit.geom

import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import groovy.transform.CompileStatic
import groovy.transform.EqualsAndHashCode
import pl.shockah.godwit.Math2
import pl.shockah.godwit.geom.polygon.Polygon
import pl.shockah.godwit.geom.polygon.Polygonable
import pl.shockah.godwit.gl.Gfx

import javax.annotation.Nonnull

@CompileStatic
@EqualsAndHashCode
class Triangle extends Shape implements Polygonable {
	@Nonnull Vec2 point1
	@Nonnull Vec2 point2
	@Nonnull Vec2 point3

	Triangle(float x1, float y1, float x2, float y2, float x3, float y3) {
		this(new Vec2(x1, y1), new Vec2(x2, y2), new Vec2(x3, y3))
	}

	Triangle(float x1, float y1, @Nonnull Vec2 point2, float x3, float y3) {
		this(new Vec2(x1, y1), point2, new Vec2(x3, y3))
	}

	Triangle(@Nonnull Vec2 point1, float x2, float y2, float x3, float y3) {
		this(point1, new Vec2(x2, y2), new Vec2(x3, y3))
	}

	Triangle(float x1, float y1, float x2, float y2, @Nonnull Vec2 point3) {
		this(new Vec2(x1, y1), new Vec2(x2, y2), point3)
	}

	Triangle(float x1, float y1, @Nonnull Vec2 point2, @Nonnull Vec2 point3) {
		this(new Vec2(x1, y1), point2, point3)
	}

	Triangle(@Nonnull Vec2 point1, float x2, float y2, @Nonnull Vec2 point3) {
		this(point1, new Vec2(x2, y2), point3)
	}

	Triangle(@Nonnull Vec2 point1, @Nonnull Vec2 point2, float x3, float y3) {
		this(point1, point2, new Vec2(x3, y3))
	}

	Triangle(@Nonnull Vec2 point1, @Nonnull Vec2 point2, @Nonnull Vec2 point3) {
		this.point1 = point1
		this.point2 = point2
		this.point3 = point3
	}

	@Override
	@Nonnull Shape copy() {
		return copyTriangle()
	}

	@Nonnull Triangle copyTriangle() {
		return new Triangle(point1, point2, point3)
	}

	@Override
	String toString() {
		return String.format("[Triangle: %s-%s-%s]", point1, point2, point3)
	}

	@Override
	@Nonnull Rectangle getBoundingBox() {
		float minX = (float)Math2.min(point1.x, point2.x, point3.x)
		float minY = (float)Math2.min(point1.y, point2.y, point3.y)
		float maxX = (float)Math2.max(point1.x, point2.x, point3.x)
		float maxY = (float)Math2.max(point1.y, point2.y, point3.y)
		return new Rectangle(minX, minY, maxX - minX as float, maxY - minY as float)
	}

	@Override
	void translate(float x, float y) {
		point1.x += x
		point1.y += y
		point2.x += x
		point2.y += y
		point3.x += x
		point3.y += y
	}

	@Override
	void draw(@Nonnull Gfx gfx, boolean filled, float x, float y) {
		gfx.prepareShapes(filled ? ShapeRenderer.ShapeType.Filled : ShapeRenderer.ShapeType.Line)
		gfx.shapes.triangle(x + point1.x as float, y + point1.y as float, x + point2.x as float, y + point2.y as float, x + point3.x as float, y + point3.y as float)
	}

	private static float sign(float x1, float y1, float x2, float y2, float x3, float y3) {
		return (x1 - x3) * (y2 - y3) - (x2 - x3) * (y1 - y3)
	}

	@Override
	boolean contains(float x, float y) {
		def b1 = sign(x, y, point1.x, point1.y, point2.x, point2.y) < 0f
		def b2 = sign(x, y, point2.x, point2.y, point3.x, point3.y) < 0f
		def b3 = sign(x, y, point3.x, point3.y, point1.x, point1.y) < 0f
		return b1 == b2 && b2 == b3
	}

	@Override
	@Nonnull Polygon asPolygon() {
		def p = new Polygon()
		p << point1
		p << point2
		p << point3
		return p
	}
}