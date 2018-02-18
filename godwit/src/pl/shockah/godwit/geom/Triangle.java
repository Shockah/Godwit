package pl.shockah.godwit.geom;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import javax.annotation.Nonnull;

import lombok.EqualsAndHashCode;
import pl.shockah.godwit.Math2;
import pl.shockah.godwit.fx.ease.Easable;
import pl.shockah.godwit.geom.polygon.Polygon;
import pl.shockah.godwit.geom.polygon.Polygonable;
import pl.shockah.godwit.gl.Gfx;

@EqualsAndHashCode(callSuper = false)
public class Triangle extends Shape implements Polygonable, Shape.Filled, Shape.Outline, Easable<Triangle> {
	@Nonnull public Vec2 point1;
	@Nonnull public Vec2 point2;
	@Nonnull public Vec2 point3;

	public Triangle(float x1, float y1, float x2, float y2, float x3, float y3) {
		this(new Vec2(x1, y1), new Vec2(x2, y2), new Vec2(x3, y3));
	}

	public Triangle(float x1, float y1, @Nonnull IVec2 point2, float x3, float y3) {
		this(new Vec2(x1, y1), point2, new Vec2(x3, y3));
	}

	public Triangle(@Nonnull IVec2 point1, float x2, float y2, float x3, float y3) {
		this(point1, new Vec2(x2, y2), new Vec2(x3, y3));
	}

	public Triangle(float x1, float y1, float x2, float y2, @Nonnull IVec2 point3) {
		this(new Vec2(x1, y1), new Vec2(x2, y2), point3);
	}

	public Triangle(float x1, float y1, @Nonnull IVec2 point2, @Nonnull IVec2 point3) {
		this(new Vec2(x1, y1), point2, point3);
	}

	public Triangle(@Nonnull IVec2 point1, float x2, float y2, @Nonnull IVec2 point3) {
		this(point1, new Vec2(x2, y2), point3);
	}

	public Triangle(@Nonnull IVec2 point1, @Nonnull IVec2 point2, float x3, float y3) {
		this(point1, point2, new Vec2(x3, y3));
	}

	public Triangle(@Nonnull IVec2 point1, @Nonnull IVec2 point2, @Nonnull IVec2 point3) {
		this.point1 = point1.getMutableCopy();
		this.point2 = point2.getMutableCopy();
		this.point3 = point3.getMutableCopy();
	}

	@Override
	@Nonnull public Shape copy() {
		return copyTriangle();
	}

	@Nonnull public Triangle copyTriangle() {
		return new Triangle(point1, point2, point3);
	}

	@Override
	public String toString() {
		return String.format("[Triangle: %s-%s-%s]", point1, point2, point3);
	}

	@Override
	@Nonnull public Rectangle getBoundingBox() {
		float minX = Math2.min(point1.x, point2.x, point3.x);
		float minY = Math2.min(point1.y, point2.y, point3.y);
		float maxX = Math2.max(point1.x, point2.x, point3.x);
		float maxY = Math2.max(point1.y, point2.y, point3.y);
		return new Rectangle(minX, minY, maxX - minX, maxY - minY);
	}

	@Override
	public void translate(@Nonnull IVec2 v) {
		point1.x += v.x();
		point1.y += v.y();
		point2.x += v.x();
		point2.y += v.y();
		point3.x += v.x();
		point3.y += v.y();
	}

	private static float sign(float x1, float y1, float x2, float y2, float x3, float y3) {
		return (x1 - x3) * (y2 - y3) - (x2 - x3) * (y1 - y3);
	}

	@Override
	public boolean contains(@Nonnull IVec2 v) {
		boolean b1 = sign(v.x(), v.y(), point1.x, point1.y, point2.x, point2.y) < 0f;
		boolean b2 = sign(v.x(), v.y(), point2.x, point2.y, point3.x, point3.y) < 0f;
		boolean b3 = sign(v.x(), v.y(), point3.x, point3.y, point1.x, point1.y) < 0f;
		return b1 == b2 && b2 == b3;
	}

	@Override
	@Nonnull public Polygon asPolygon() {
		Polygon p = new Polygon();
		p.addPoint(point1);
		p.addPoint(point2);
		p.addPoint(point3);
		return p;
	}

	@Override
	public void drawFilled(@Nonnull Gfx gfx, @Nonnull IVec2 v) {
		gfx.prepareShapes(ShapeRenderer.ShapeType.Filled, () -> {
			gfx.getShapeRenderer().triangle(v.x() + point1.x, v.y() + point1.y, v.x() + point2.x, v.y() + point2.y, v.x() + point3.x, v.y() + point3.y);
		});
	}

	@Override
	public void drawOutline(@Nonnull Gfx gfx, @Nonnull IVec2 v) {
		gfx.prepareShapes(ShapeRenderer.ShapeType.Line, () -> {
			gfx.getShapeRenderer().triangle(v.x() + point1.x, v.y() + point1.y, v.x() + point2.x, v.y() + point2.y, v.x() + point3.x, v.y() + point3.y);
		});
	}

	@Override
	@Nonnull public Triangle ease(@Nonnull Triangle other, float f) {
		return new Triangle(
				point1.ease(other.point1, f),
				point2.ease(other.point2, f),
				point3.ease(other.point3, f)
		);
	}
}