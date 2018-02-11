package pl.shockah.godwit.geom

import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import groovy.transform.CompileStatic
import groovy.transform.EqualsAndHashCode
import pl.shockah.godwit.gl.Gfx

import javax.annotation.Nonnull
import javax.annotation.Nullable

@CompileStatic
@EqualsAndHashCode
class Line extends Shape implements Shape.Outline {
	@Nonnull Vec2 point1
	@Nonnull Vec2 point2

	Line(float x1, float y1, float x2, float y2) {
		this(new Vec2(x1, y1), new Vec2(x2, y2));
	}

	Line(@Nonnull Vec2 point1, float x2, float y2) {
		this(point1, new Vec2(x2, y2));
	}

	Line(float x1, float y1, @Nonnull Vec2 point2) {
		this(new Vec2(x1, y1), point2);
	}

	Line(@Nonnull Vec2 point1, @Nonnull Vec2 point2) {
		this.point1 = point1
		this.point2 = point2
	}

	@Override
	Shape copy() {
		return copyLine()
	}

	Line copyLine() {
		return new Line(point1, point2)
	}

	@Override
	String toString() {
		return "[Line: ${point1}->${point2}]"
	}

	@Override
	@Nonnull Rectangle getBoundingBox() {
		float minX = Math.min(point1.x, point2.x)
		float minY = Math.min(point1.y, point2.y)
		float maxX = Math.max(point1.x, point2.x)
		float maxY = Math.max(point1.y, point2.y)
		return new Rectangle(minX, minY, maxX - minX as float, maxY - minY as float)
	}

	@Override
	void translate(float x, float y) {
		point1.x += x
		point1.y += y
		point2.x += x
		point2.y += y
	}

	@Override
	protected boolean collides(@Nonnull Shape shape, boolean tryAgain) {
		if (shape instanceof Line)
			return collides(shape as Line)
		return super.collides(shape, tryAgain)
	}

	boolean collides(@Nonnull Line line) {
		return intersect(line) != null
	}

	@Nullable Vec2 intersect(@Nonnull Line line) {
		float dx1 = point2.x - point1.x
		float dx2 = line.point2.x - line.point1.x
		float dy1 = point2.y - point1.y
		float dy2 = line.point2.y - line.point1.y
		float denom = (dy2 * dx1) - (dx2 * dy1)

		if (denom == 0)
			return null

		float ua = (dx2 * (point1.y - line.point1.y)) - (dy2 * (point1.x - line.point1.x))
		ua = ua / denom as float
		float ub = (dx1 * (point1.y - line.point1.y)) - (dy1 * (point1.x - line.point1.x))
		ub = ub / denom as float

		/*if ((limit) && ((ua < 0) || (ua > 1) || (ub < 0) || (ub > 1)))
			return null*/

		float u = ua

		float ix = point1.x + (u * (point2.x - point1.x))
		float iy = point1.y + (u * (point2.y - point1.y))

		return new Vec2(ix, iy)
	}

	@Override
	void drawOutline(@Nonnull Gfx gfx, float x, float y) {
		gfx.prepareShapes(ShapeRenderer.ShapeType.Line) {
			line(x + point1.x as float ,y + point1.y as float, x + point2.x as float, y + point2.y as float)
		}
	}
}