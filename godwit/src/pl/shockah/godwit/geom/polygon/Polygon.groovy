package pl.shockah.godwit.geom.polygon

import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import groovy.transform.CompileStatic
import pl.shockah.godwit.Math2
import pl.shockah.godwit.geom.*
import pl.shockah.godwit.gl.Gfx

@CompileStatic
class Polygon extends Shape {
	protected List<Vec2> points = []
	protected List<Triangle> triangulated = []
	protected boolean dirty = true
	boolean closed = true

	@Override
	Shape copy() {
		return copyPolygon()
	}

	Polygon copyPolygon() {
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
		for (Vec2 point : points) {
			hash *= 31 * 31
			hash += point.hashCode()
		}
		return hash * 2 + (closed ? 1 : 0) as int
	}

	@Override
	Rectangle getBoundingBox() {
		float[] x = new float[points.size()]
		float[] y = new float[points.size()]
		for (int i in 0..<points.size()) {
			Vec2 v = points[i]
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

	protected Triangulator triangulator() {
		return new NeatTriangulator()
	}

	protected void triangulate() {
		if (!dirty)
			return
		def triangulator = triangulator()
		for (Vec2 point : points) {
			triangulator.addPolyPoint(point)
		}

		triangulated.clear()
		assert triangulator.triangulate() && triangulator.triangleCount(), "Failed to triangulate polygon."
		for (int i in 0..<triangulator.triangleCount()) {
			triangulated << new Triangle(triangulator.trianglePoint(i, 0), triangulator.trianglePoint(i, 1), triangulator.trianglePoint(i, 2))
		}
		dirty = false
	}

	@Override
	void draw(Gfx gfx, boolean filled, float x, float y) {
		if (filled) {
			assert closed, "Can't fill an open polygon."
			triangulate()
			for (Triangle triangle : triangulated) {
				triangle.draw(gfx, true, x, y)
			}
		} else {
			gfx.prepareShapes(ShapeRenderer.ShapeType.Line)
			def size = points.size()
			def loopSize = closed ? size : (size - 1)
			for (int i in 0..<loopSize) {
				Vec2 v1 = points[i]
				Vec2 v2 = points[(i + 1) % size]
				gfx.shapes.line(x + v1.x as float, y + v1.y as float, x + v2.x as float, y + v2.y as float)
			}
		}
	}

	void addPoint(float x, float y) {
		addPoint(new Vec2(x, y))
	}

	void addPoint(Vec2 v) {
		points << v
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

	Polygon leftShift(Vec2 v) {
		addPoint(v)
		return this
	}

	Vec2 getAt(int index) {
		return points[index]
	}

	void putAt(int index, Vec2 value) {
		points[index] = value
		dirty = true
	}

	List<Line> getLines() {
		List<Line> lines = []
		for (int i in 1..<points.size()) {
			lines << new Line(points[i - 1], points[i])
		}
		if (closed)
			lines << new Line(points.last(), points.first())
		return lines
	}

	static class NoHoles extends Polygon {
		@Override
		protected Triangulator triangulator() {
			return new BasicTriangulator()
		}
	}
}