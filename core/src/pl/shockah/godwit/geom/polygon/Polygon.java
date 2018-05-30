package pl.shockah.godwit.geom.polygon;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

import pl.shockah.godwit.geom.AbstractShape;
import pl.shockah.godwit.geom.IVec2;
import pl.shockah.godwit.geom.Line;
import pl.shockah.godwit.geom.Rectangle;
import pl.shockah.godwit.geom.Shape;
import pl.shockah.godwit.geom.Triangle;
import pl.shockah.godwit.geom.Vec2;
import pl.shockah.godwit.gl.Gfx;
import pl.shockah.unicorn.Math2;

public class Polygon extends AbstractShape implements Polygonable, Shape.Filled, Shape.Outline {
	@Nonnull
	protected final List<IVec2> points = new ArrayList<>();

	@Nonnull
	protected final List<Triangle> triangulated = new ArrayList<>();

	protected boolean dirty = true;
	public boolean closed = true;

	@Override
	@Nonnull
	public Polygon copy() {
		Polygon p = new Polygon();
		p.closed = closed;
		p.points.addAll(points);
		if (!dirty) {
			for (Triangle triangle : triangulated) {
				p.triangulated.add(triangle.copy());
			}
			p.dirty = false;
		}
		return p;
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Polygon))
			return false;
		Polygon polygon = (Polygon)obj;
		if (closed != polygon.closed || points.size() != polygon.points.size())
			return false;
		for (int i = 0; i < points.size(); i++) {
			if (points.get(i) != polygon.points.get(i))
				return false;
		}
		return true;
	}

	@Override
	public int hashCode() {
		BigInteger hash = BigInteger.ZERO;
		for (IVec2 point : points) {
			hash = hash.multiply(BigInteger.valueOf(31)).multiply(BigInteger.valueOf(31));
			hash = hash.add(BigInteger.valueOf(point.hashCode()));
		}
		return hash.multiply(BigInteger.valueOf(2)).add(BigInteger.valueOf(closed ? 1 : 0)).intValue();
	}

	@Override
	@Nonnull
	public Rectangle getBoundingBox() {
		float[] x = new float[points.size()];
		float[] y = new float[points.size()];
		for (int i = 0; i < points.size(); i++) {
			IVec2 v = points.get(i);
			x[i] = v.x();
			y[i] = v.y();
		}
		float minX = Math2.min(x);
		float minY = Math2.min(y);
		float maxX = Math2.max(x);
		float maxY = Math2.max(y);
		return new Rectangle(minX, minY, maxX - minX, maxY - minY);
	}

	@Override
	public void translate(@Nonnull IVec2 v) {
		for (int i = 0; i < points.size(); i++) {
			points.set(i, points.get(i).add(v));
		}
		if (!dirty) {
			for (Triangle triangle : triangulated) {
				triangle.translate(v);
			}
		}
	}

	@Override
	public void mirror(boolean horizontally, boolean vertically) {
		IVec2 modifier = new Vec2(horizontally ? -1 : 1, vertically ? -1 : 1);
		for (int i = 0; i < points.size(); i++) {
			points.set(i, points.get(i).multiply(modifier));
		}
		if (!dirty) {
			for (Triangle triangle : triangulated) {
				triangle.mirror(horizontally, vertically);
			}
		}
	}

	@Override
	public void scale(float scale) {
		for (int i = 0; i < points.size(); i++) {
			points.set(i, points.get(i).multiply(scale));
		}
		if (!dirty) {
			for (Triangle triangle : triangulated) {
				triangle.scale(scale);
			}
		}
	}

	@Nonnull
	protected Triangulator triangulator() {
		return new NeatTriangulator();
	}

	protected void triangulate() {
		if (!dirty)
			return;
		Triangulator triangulator = triangulator();
		for (IVec2 point : points) {
			triangulator.addPoint(point);
		}

		triangulated.clear();
		if (!triangulator.triangulate() || triangulator.getTriangleCount() == 0)
			throw new IllegalStateException("Failed to triangulate polygon.");
		for (int i = 0; i < triangulator.getTriangleCount(); i++) {
			triangulated.add(new Triangle(
					triangulator.getTrianglePoint(i, 0),
					triangulator.getTrianglePoint(i, 1),
					triangulator.getTrianglePoint(i, 2)
			));
		}
		dirty = false;
	}

	public void addPoint(float x, float y) {
		addPoint(new Vec2(x, y));
	}

	public void addPoint(@Nonnull IVec2 v) {
		points.add(v);
		dirty = true;
		triangulated.clear();
	}

	public int getPointCount() {
		return points.size();
	}

	@Override
	public boolean contains(@Nonnull IVec2 v) {
		if (!closed)
			return false;
		triangulate();
		for (Triangle triangle : triangulated) {
			if (triangle.contains(v))
				return true;
		}
		return false;
	}

	@Override
	public boolean collides(@Nonnull Shape shape, boolean secondTry) {
		if (shape instanceof Triangle)
			return collides((Triangle)shape);
		else if (shape instanceof Polygonable)
			return collides((Polygonable)shape);
		else
			return super.collides(shape, secondTry);
	}

	public boolean collides(@Nonnull Polygonable polygonable) {
		Polygon polygon = polygonable.asPolygon();
		polygon.triangulate();
		for (Triangle triangle : polygon.triangulated) {
			if (collides(triangle))
				return true;
		}
		return false;
	}

	public boolean collides(@Nonnull Triangle triangle) {
		triangulate();
		for (Triangle myTriangle : triangulated) {
			if (triangle.collides(myTriangle))
				return true;
		}
		return false;
	}

	@Override
	@Nonnull
	public Polygon asPolygon() {
		return this;
	}

	@Nonnull
	public IVec2 get(int index) {
		return points.get(index);
	}

	public void set(int index, @Nonnull IVec2 value) {
		points.set(index, value);
		dirty = true;
		triangulated.clear();
	}

	@Nonnull
	public List<Line> getLines() {
		List<Line> lines = new ArrayList<>();
		for (int i = 1; i < points.size(); i++) {
			lines.add(new Line(points.get(i - 1), points.get(i)));
		}
		if (closed)
			lines.add(new Line(points.get(points.size() - 1), points.get(0)));
		return lines;
	}

	@Nonnull
	public List<Triangle> getTriangles() {
		triangulate();
		return new ArrayList<>(triangulated);
	}

	@Override
	public void drawFilled(@Nonnull Gfx gfx, @Nonnull IVec2 v) {
		if (!closed)
			throw new IllegalStateException("Can't fill an open polygon.");
		triangulate();
		for (Triangle triangle : triangulated) {
			triangle.drawFilled(gfx, v);
		}
	}

	@Override
	public void drawOutline(@Nonnull Gfx gfx, @Nonnull IVec2 v) {
		gfx.prepareShapes(ShapeRenderer.ShapeType.Line);
		int size = points.size();
		int loopSize = closed ? size : (size - 1);
		for (int i = 0; i < loopSize; i++) {
			IVec2 v1 = points.get(i);
			IVec2 v2 = points.get((i + 1) % size);
			gfx.getShapeRenderer().line(v.x() + v1.x(), v.y() + v1.y(), v.x() + v2.x(), v.y() + v2.y());
		}
	}

	public static class NoHoles extends Polygon {
		@Override
		@Nonnull
		protected Triangulator triangulator() {
			return new BasicTriangulator();
		}
	}
}