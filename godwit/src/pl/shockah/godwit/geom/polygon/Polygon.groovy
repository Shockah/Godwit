package pl.shockah.godwit.geom.polygon

import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import groovy.transform.CompileStatic
import pl.shockah.godwit.Math2
import pl.shockah.godwit.geom.*
import pl.shockah.godwit.gl.Gfx

import javax.annotation.Nonnull

@CompileStatic
class Polygon extends Shape implements Shape.Filled, Shape.Outline {
	@Nonnull protected List<IVec2> points = []
	@Nonnull protected List<Triangle> triangulated = []
	protected boolean dirty = true
	boolean closed = true

	@Override
	@Nonnull Shape copy() {
		return copyPolygon()
	}

	@Nonnull Polygon copyPolygon() {
		def p = new Polygon()
		p.closed = closed
		p.points.addAll(points)
		if (!dirty) {
			for (Triangle triangle : triangulated) {
				p.triangulated << triangle.copyTriangle()
			}
			p.dirty = false
		}
		return p
	}

	@Override
	boolean equals(Object obj) {
		if (!(obj instanceof Polygon))
			return false
		Polygon polygon = obj as Polygon
		if (closed != polygon.closed || points.size() != polygon.points.size())
			return false
		for (int i in 0..<points.size()) {
			if (points[i] != polygon.points[i])
				return false
		}
		return true
	}

	@Override
	int hashCode() {
		BigInteger hash = 0
		for (IVec2 point : points) {
			hash *= 31 * 31
			hash += point.hashCode()
		}
		return hash * 2 + (closed ? 1 : 0) as int
	}

	@Override
	@Nonnull Rectangle getBoundingBox() {
		float[] x = new float[points.size()]
		float[] y = new float[points.size()]
		for (int i in 0..<points.size()) {
			IVec2 v = points[i]
			x[i] = v.x
			y[i] = v.y
		}
		float minX = (float)Math2.min(x)
		float minY = (float)Math2.min(y)
		float maxX = (float)Math2.max(x)
		float maxY = (float)Math2.max(y)
		return new Rectangle(minX, minY, maxX - minX as float, maxY - minY as float)
	}

	@Override
	void translate(float x, float y) {
		for (int i in 0..<points.size()) {
			points[i] = points[i].plus(x, y)
		}
		if (!dirty) {
			for (Triangle triangle : triangulated) {
				triangle.translate(x, y)
			}
		}
	}

	@Nonnull protected Triangulator triangulator() {
		return new NeatTriangulator()
	}

	protected void triangulate() {
		if (!dirty)
			return
		def triangulator = triangulator()
		for (IVec2 point : points) {
			triangulator.addPolyPoint(point)
		}

		triangulated.clear()
		assert triangulator.triangulate() && triangulator.triangleCount(), "Failed to triangulate polygon."
		for (int i in 0..<triangulator.triangleCount()) {
			triangulated << new Triangle(triangulator.trianglePoint(i, 0), triangulator.trianglePoint(i, 1), triangulator.trianglePoint(i, 2))
		}
		dirty = false
	}

	void addPoint(float x, float y) {
		addPoint(new Vec2(x, y))
	}

	void addPoint(IVec2 v) {
		points.add(v)
		dirty = true
	}

	int getPointCount() {
		return points.size()
	}

	@Override
	boolean contains(float x, float y) {
		if (!closed)
			return false
		triangulate()
		for (Triangle triangle : triangulated) {
			if (triangle.contains(x, y))
				return true
		}
		return false
	}

	@Nonnull IVec2 getAt(int index) {
		return points[index]
	}

	void putAt(int index, IVec2 value) {
		points[index] = value
		dirty = true
	}

	@Nonnull List<Line> getLines() {
		List<Line> lines = []
		for (int i in 1..<points.size()) {
			lines.add(new Line(points[i - 1], points[i]))
		}
		if (closed)
			lines.add(new Line(points.last(), points.first()))
		return lines
	}

	@Override
	void drawFilled(@Nonnull Gfx gfx, float x, float y) {
		assert closed, "Can't fill an open polygon."
		triangulate()
		for (Triangle triangle : triangulated) {
			triangle.drawFilled(gfx, x, y)
		}
	}

	@Override
	void drawOutline(@Nonnull Gfx gfx, float x, float y) {
		gfx.prepareShapes(ShapeRenderer.ShapeType.Line)
		def size = points.size()
		def loopSize = closed ? size : (size - 1)
		for (int i in 0..<loopSize) {
			IVec2 v1 = points[i]
			IVec2 v2 = points[(i + 1) % size]
			gfx.shapeRenderer.line(x + v1.x as float, y + v1.y as float, x + v2.x as float, y + v2.y as float)
		}
	}

	static class NoHoles extends Polygon {
		@Override
		@Nonnull protected Triangulator triangulator() {
			return new BasicTriangulator()
		}
	}
}