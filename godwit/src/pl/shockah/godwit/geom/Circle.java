package pl.shockah.godwit.geom;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import pl.shockah.godwit.fx.ease.Easable;
import pl.shockah.godwit.fx.ease.Easing;
import pl.shockah.godwit.geom.polygon.Polygon;
import pl.shockah.godwit.geom.polygon.Polygonable;
import pl.shockah.godwit.gl.Gfx;

public class Circle extends Shape implements Polygonable, Shape.Filled, Shape.Outline, Easable<Circle> {
	@Nonnull public Vec2 position;
	public float radius;

	@Nullable protected Vec2 lastPos = null;
	protected int lastPrecision = -1;
	@Nullable protected Polygon lastPoly = null;

	public Circle(float x, float y, float radius) {
		this(new Vec2(x, y), radius);
	}

	public Circle(float radius) {
		this(new Vec2(), radius);
	}

	public Circle(@Nonnull IVec2 position, float radius) {
		this.position = position.getMutableCopy();
		this.radius = radius;
	}

	@Override
	@Nonnull public Shape copy() {
		return copyCircle();
	}

	@Nonnull public Circle copyCircle() {
		return new Circle(position, radius);
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Circle))
			return false;
		Circle circle = (Circle)obj;
		return position == circle.position && radius == circle.radius;
	}

	@Override
	public int hashCode() {
		return position.hashCode() * 31 + Float.hashCode(radius);
	}

	@Override
	public String toString() {
		return String.format("[Circle: %s, radius %.2f]", position, radius);
	}

	@Override
	@Nonnull public Rectangle getBoundingBox() {
		return Rectangle.centered(position, radius * 2f);
	}

	@Override
	public void translate(@Nonnull IVec2 v) {
		position.x += v.x();
		position.y += v.y();
	}

	@Override
	public boolean contains(@Nonnull IVec2 v) {
		return (position - v).getLength() <= radius;
	}

	@Override
	public boolean collides(@Nonnull Shape shape, boolean secondTry) {
		//TODO: Circle --><-- Line
		//TODO: Circle --><-- Rectangle
		if (shape instanceof Circle)
			return collides((Circle)shape);
		return super.collides(shape, secondTry);
	}

	public boolean collides(@Nonnull Circle circle) {
		return (circle.position - position).getLength() < radius + circle.radius;
	}

	@Override
	@Nonnull public Polygon asPolygon() {
		return asPolygon((int)Math.ceil(Math.PI * radius * 0.5f));
	}

	@Nonnull public Polygon asPolygon(int precision) {
		if (lastPoly != null && lastPoly.getPointCount() == precision && lastPrecision == precision && position.equals(lastPos))
			return lastPoly;

		Polygon p = new Polygon.NoHoles();
		for (int i = 0; i < precision; i++) {
			p.addPoint(Vec2.angled(radius, 360f / precision * i) + position);
		}

		lastPos = position;
		lastPrecision = precision;
		return lastPoly = p;
	}

	@Override
	public void drawFilled(@Nonnull Gfx gfx, @Nonnull IVec2 v) {
		asPolygon().drawFilled(gfx, v);
	}

	@Override
	public void drawOutline(@Nonnull Gfx gfx, @Nonnull IVec2 v) {
		asPolygon().drawOutline(gfx, v);
	}

	@Override
	@Nonnull public Circle ease(@Nonnull Circle other, float f) {
		return new Circle(position.ease(other.position, f), Easing.linear.ease(radius, other.radius, f));
	}
}