package pl.shockah.godwit.geom

import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import groovy.transform.CompileStatic
import pl.shockah.godwit.gl.Gfx

@CompileStatic
class Line extends Shape {
	Vec2 pos1
	Vec2 pos2

	Line(float x1, float y1, float x2, float y2) {
		this(new Vec2(x1, y1), new Vec2(x2, y2));
	}

	Line(Vec2 pos1, float x2, float y2) {
		this(pos1, new Vec2(x2, y2));
	}

	Line(float x1, float y1, Vec2 pos2) {
		this(new Vec2(x1, y1), pos2);
	}

	Line(Vec2 pos1, Vec2 pos2) {
		this.pos1 = pos1
		this.pos2 = pos2
	}

	@Override
	Shape copy() {
		return copyLine()
	}

	Line copyLine() {
		return new Line(pos1, pos2)
	}

	@Override
	boolean equals(Object obj) {
		if (!(obj instanceof Line))
			return false
		Line line = obj as Line
		return pos1 == line.pos1 && pos2 == line.pos2
	}

	@Override
	int hashCode() {
		return pos1.hashCode() * 31 * 31 + pos2.hashCode()
	}

	@Override
	String toString() {
		return String.format("[Line: %s->%s]", pos1, pos2)
	}

	@Override
	Rectangle getBoundingBox() {
		float minX = Math.min(pos1.x, pos2.x)
		float minY = Math.min(pos1.y, pos2.y)
		float maxX = Math.max(pos1.x, pos2.x)
		float maxY = Math.max(pos1.y, pos2.y)
		return new Rectangle(minX, minY, maxX - minX as float, maxY - minY as float)
	}

	@Override
	void translate(float x, float y) {
		pos1.x += x
		pos1.y += y
		pos2.x += x
		pos2.y += y
	}

	@Override
	void draw(Gfx gfx, boolean filled, float x, float y) {
		assert !filled
		gfx.prepareShapes(ShapeRenderer.ShapeType.Line)
		gfx.shapes.line(x + pos1.x as float ,y + pos1.y as float, x + pos2.x as float, y + pos2.y as float)
	}

	@Override
	boolean contains(float x, float y) {
		return false
	}

	@Override
	boolean collides(Shape shape, boolean tryAgain) {
		if (shape instanceof Line)
			return collides(shape as Line)
		return super.collides(shape, tryAgain)
	}

	boolean collides(Line line) {
		return intersect(line) != null
	}

	Vec2 intersect(Line line) {
		float dx1 = pos2.x - pos1.x
		float dx2 = line.pos2.x - line.pos1.x
		float dy1 = pos2.y - pos1.y
		float dy2 = line.pos2.y - line.pos1.y
		float denom = (dy2 * dx1) - (dx2 * dy1)

		if (denom == 0)
			return null

		float ua = (dx2 * (pos1.y - line.pos1.y)) - (dy2 * (pos1.x - line.pos1.x))
		ua = ua / denom as float
		float ub = (dx1 * (pos1.y - line.pos1.y)) - (dy1 * (pos1.x - line.pos1.x))
		ub = ub / denom as float

		/*if ((limit) && ((ua < 0) || (ua > 1) || (ub < 0) || (ub > 1)))
			return null*/

		float u = ua

		float ix = pos1.x + (u * (pos2.x - pos1.x))
		float iy = pos1.y + (u * (pos2.y - pos1.y))

		return new Vec2(ix, iy)
	}
}