package pl.shockah.godwit.geom;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import lombok.EqualsAndHashCode;
import pl.shockah.godwit.gl.Gfx;
import pl.shockah.unicorn.ease.Easable;

@EqualsAndHashCode(callSuper = false)
public class Line extends AbstractShape implements Shape.Outline, Easable<Line> {
	@Nonnull
	public MutableVec2 point1;

	@Nonnull
	public MutableVec2 point2;

	public Line(float x1, float y1, float x2, float y2) {
		this(new MutableVec2(x1, y1), new MutableVec2(x2, y2));
	}

	public Line(@Nonnull MutableVec2 point1, float x2, float y2) {
		this(point1, new MutableVec2(x2, y2));
	}

	public Line(float x1, float y1, @Nonnull IVec2 point2) {
		this(new MutableVec2(x1, y1), point2);
	}

	public Line(@Nonnull IVec2 point1, @Nonnull IVec2 point2) {
		this.point1 = point1.mutableCopy();
		this.point2 = point2.mutableCopy();
	}

	@Override
	@Nonnull
	public Line copy() {
		return new Line(point1, point2);
	}

	@Override
	public String toString() {
		return String.format("[Line: %s->%s]", point1, point2);
	}

	@Override
	@Nonnull
	public Rectangle getBoundingBox() {
		float minX = Math.min(point1.x, point2.x);
		float minY = Math.min(point1.y, point2.y);
		float maxX = Math.max(point1.x, point2.x);
		float maxY = Math.max(point1.y, point2.y);
		return new Rectangle(minX, minY, maxX - minX, maxY - minY);
	}

	@Nonnull
	public Vec2 getCenter() {
		return point1.add(point2).multiply(0.5f);
	}

	@Override
	public void translate(@Nonnull IVec2 v) {
		point1.x += v.x();
		point1.y += v.y();
		point2.x += v.x();
		point2.y += v.y();
	}

	@Override
	public void mirror(boolean horizontally, boolean vertically) {
		if (horizontally) {
			point1.x *= -1;
			point2.x *= -1;
		}
		if (vertically) {
			point1.y *= -1;
			point2.y *= -1;
		}
	}

	@Override
	public void scale(float scale) {
		point1.set(point1.multiply(scale));
		point2.set(point2.multiply(scale));
	}

	@Override
	public boolean collides(@Nonnull Shape shape, boolean secondTry) {
		if (shape instanceof Line)
			return collides((Line)shape);
		return super.collides(shape, secondTry);
	}

	public boolean collides(@Nonnull Line line) {
		return intersect(line) != null;
	}

	@Nullable
	public Vec2 intersect(@Nonnull Line line) {
		float dx1 = point2.x - point1.x;
		float dx2 = line.point2.x - line.point1.x;
		float dy1 = point2.y - point1.y;
		float dy2 = line.point2.y - line.point1.y;
		float denom = (dy2 * dx1) - (dx2 * dy1);

		if (denom == 0)
			return null;

		float ua = (dx2 * (point1.y - line.point1.y)) - (dy2 * (point1.x - line.point1.x));
		ua = ua / denom;
		float ub = (dx1 * (point1.y - line.point1.y)) - (dy1 * (point1.x - line.point1.x));
		ub = ub / denom;

		if (((ua < 0) || (ua > 1) || (ub < 0) || (ub > 1)))
			return null;

		float u = ua;

		float ix = point1.x + (u * (point2.x - point1.x));
		float iy = point1.y + (u * (point2.y - point1.y));

		return new Vec2(ix, iy);
	}

	@Nonnull
	public Vec2[] intersect(@Nonnull Circle circle) {
		return circle.intersect(this);
	}

	@Override
	public void drawOutline(@Nonnull Gfx gfx, @Nonnull IVec2 v) {
		gfx.prepareShapes(ShapeRenderer.ShapeType.Line);
		gfx.getShapeRenderer().line(v.x() + point1.x, v.y() + point1.y, v.x() + point2.x, v.y() + point2.y);
	}

	@Override
	@Nonnull
	public Line ease(@Nonnull Line other, float f) {
		return new Line(point1.ease(other.point1, f), point2.ease(other.point2, f));
	}
}