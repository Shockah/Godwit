package pl.shockah.godwit.geom

import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import groovy.transform.CompileStatic
import pl.shockah.godwit.geom.polygon.Polygon
import pl.shockah.godwit.geom.polygon.Polygonable
import pl.shockah.godwit.gl.Gfx

@CompileStatic
class Rectangle extends Shape implements Polygonable {
	Vec2 pos
	Vec2 size

	static Rectangle centered(float x, float y, float w, float h) {
		return centered(new Vec2(x, y), new Vec2(w, h))
	}

	static Rectangle centered(float x, float y, Vec2 size) {
		return centered(new Vec2(x, y), size)
	}

	static Rectangle centered(Vec2 pos, float w, float h) {
		return centered(pos, new Vec2(w, h))
	}

	static Rectangle centered(Vec2 pos, Vec2 size) {
		return new Rectangle(pos - size * 0.5f, size)
	}

	static Rectangle centered(float x, float y, float l) {
		return centered(new Vec2(x, y), new Vec2(l, l))
	}

	static Rectangle centered(Vec2 pos, float l) {
		return centered(pos, new Vec2(l, l))
	}

	static Rectangle centered(float w, float h) {
		return centered(new Vec2(), new Vec2(w, h))
	}

	static Rectangle centered(Vec2 size) {
		return centered(new Vec2(), size)
	}

	Rectangle(float x, float y, float w, float h) {
		this(new Vec2(x, y), new Vec2(w, h))
	}

	Rectangle(float x, float y, Vec2 size) {
		this(new Vec2(x, y), size)
	}

	Rectangle(Vec2 pos, float w, float h) {
		this(pos, new Vec2(w, h))
	}

	Rectangle(Vec2 pos, Vec2 size) {
		this.pos = pos
		this.size = size
	}

	Rectangle(float x, float y, float l) {
		this(new Vec2(x, y), new Vec2(l, l))
	}

	Rectangle(Vec2 pos, float l) {
		this(pos, new Vec2(l, l))
	}

	Rectangle(float w, float h) {
		this(new Vec2(), new Vec2(w, h))
	}

	Rectangle(Vec2 size) {
		this(new Vec2(), size)
	}

	@Override
	Shape copy() {
		return copyRectangle()
	}

	Rectangle copyRectangle() {
		return new Rectangle(pos, size)
	}

	@Override
	boolean equals(Object obj) {
		if (!(obj instanceof Rectangle))
			return false
		Rectangle rect = obj as Rectangle
		return pos == rect.pos && size == rect.size
	}

	@Override
	int hashCode() {
		return pos.hashCode() * 31 * 31 + size.hashCode()
	}

	@Override
	String toString() {
		return String.format("[Rectangle: @%s, %.2fx%.2f]", pos, size.x, size.y)
	}

	@Override
	Rectangle getBoundingBox() {
		return copyRectangle()
	}

	Vec2 getCenter() {
		return pos + size * 0.5f
	}

	@Override
	void translate(float x, float y) {
		pos.x += x
		pos.y += y
	}

	@Override
	void draw(Gfx gfx, boolean filled, float x, float y) {
		gfx.prepareShapes(filled ? ShapeRenderer.ShapeType.Filled : ShapeRenderer.ShapeType.Line)
		gfx.shapes.rect(x + pos.x as float, y + pos.y as float, size.x, size.y)
	}

	@Override
	boolean contains(float x, float y) {
		return x >= pos.x && y >= pos.y && x < pos.x + size.x && y < pos.y + size.y
	}

	@Override
	boolean collides(Shape shape, boolean tryAgain) {
		if (shape instanceof Rectangle)
			return collides(shape as Rectangle)
		if (shape instanceof Line)
			return collides(shape as Line)
		return super.collides(shape, tryAgain)
	}

	boolean collides(Line line) {
		if (contains(line.pos1) || contains(line.pos2))
			return true
		for (Line myLine : asPolygon().lines) {
			if (myLine.collides(line))
				return true
		}
		return false
	}

	boolean collides(Rectangle rectangle) {
		Vec2 v = (center - rectangle.center).abs - (size * 0.5f + rectangle.size * 0.5f)
		return v.x < 0 && v.y < 0
	}

	@Override
	Polygon asPolygon() {
		def p = new Polygon()
		p << pos
		p << pos + size.onlyX
		p << pos + size
		p << pos + size.onlyY
		return p
	}
}