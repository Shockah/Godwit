package pl.shockah.godwit.geom

import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import groovy.transform.CompileStatic
import groovy.transform.EqualsAndHashCode
import pl.shockah.godwit.animfx.ease.Easable
import pl.shockah.godwit.geom.polygon.Polygon
import pl.shockah.godwit.geom.polygon.Polygonable
import pl.shockah.godwit.gl.Gfx

import javax.annotation.Nonnull

@CompileStatic
@EqualsAndHashCode
class Rectangle extends Shape implements Polygonable, Shape.Filled, Shape.Outline, Easable<Rectangle> {
	@Nonnull Vec2 position
	@Nonnull Vec2 size

	static Rectangle centered(float x, float y, float w, float h) {
		return centered(new Vec2(x, y), new Vec2(w, h))
	}

	static Rectangle centered(float x, float y, @Nonnull IVec2 size) {
		return centered(new Vec2(x, y), size)
	}

	static Rectangle centered(@Nonnull IVec2 position, float w, float h) {
		return centered(position, new Vec2(w, h))
	}

	static Rectangle centered(@Nonnull IVec2 position, @Nonnull IVec2 size) {
		return new Rectangle(position - size * 0.5f, size)
	}

	static Rectangle centered(float x, float y, float l) {
		return centered(new Vec2(x, y), new Vec2(l, l))
	}

	static Rectangle centered(@Nonnull IVec2 position, float l) {
		return centered(position, new Vec2(l, l))
	}

	static Rectangle centered(float w, float h) {
		return centered(new Vec2(), new Vec2(w, h))
	}

	static Rectangle centered(@Nonnull IVec2 size) {
		return centered(new Vec2(), size)
	}

	Rectangle(float x, float y, float w, float h) {
		this(new Vec2(x, y), new Vec2(w, h))
	}

	Rectangle(float x, float y, @Nonnull IVec2 size) {
		this(new Vec2(x, y), size)
	}

	Rectangle(@Nonnull IVec2 position, float w, float h) {
		this(position, new Vec2(w, h))
	}

	Rectangle(@Nonnull IVec2 position, @Nonnull IVec2 size) {
		this.position = position.mutableCopy
		this.size = size.mutableCopy
	}

	Rectangle(float x, float y, float l) {
		this(new Vec2(x, y), new Vec2(l, l))
	}

	Rectangle(@Nonnull IVec2 pos, float l) {
		this(pos, new Vec2(l, l))
	}

	Rectangle(float w, float h) {
		this(new Vec2(), new Vec2(w, h))
	}

	Rectangle(@Nonnull IVec2 size) {
		this(new Vec2(), size)
	}

	@Override
	@Nonnull Shape copy() {
		return copyRectangle()
	}

	@Nonnull Rectangle copyRectangle() {
		return new Rectangle(position, size)
	}

	@Override
	String toString() {
		return String.format("[Rectangle: @%s, %.2fx%.2f]", position, size.x, size.y)
	}

	@Override
	@Nonnull Rectangle getBoundingBox() {
		return copyRectangle()
	}

	@Nonnull IVec2 getCenter() {
		return position + size * 0.5f
	}

	@Override
	void translate(float x, float y) {
		position.x += x
		position.y += y
	}

	@Override
	boolean contains(float x, float y) {
		return x >= position.x && y >= position.y && x < position.x + size.x && y < position.y + size.y
	}

	@Override
	protected boolean collides(@Nonnull Shape shape, boolean tryAgain) {
		if (shape instanceof Rectangle)
			return collides(shape as Rectangle)
		if (shape instanceof Line)
			return collides(shape as Line)
		return super.collides(shape, tryAgain)
	}

	boolean collides(@Nonnull Line line) {
		if (contains(line.point1) || contains(line.point2))
			return true
		for (Line myLine : asPolygon().lines) {
			if (myLine.collides(line))
				return true
		}
		return false
	}

	boolean collides(@Nonnull Rectangle rectangle) {
		Vec2 v = (center - rectangle.center).abs - (size * 0.5f + rectangle.size * 0.5f)
		return v.x < 0 && v.y < 0
	}

	@Override
	@Nonnull Polygon asPolygon() {
		def p = new Polygon()
		p.addPoint(position)
		p.addPoint(position + size.onlyX)
		p.addPoint(position + size)
		p.addPoint(position + size.onlyY)
		return p
	}

	@Override
	void drawFilled(@Nonnull Gfx gfx, float x, float y) {
		gfx.prepareShapes(ShapeRenderer.ShapeType.Filled) {
			rect(x + position.x as float, y + position.y as float, size.x, size.y)
		}
	}

	@Override
	void drawOutline(@Nonnull Gfx gfx, float x, float y) {
		gfx.prepareShapes(ShapeRenderer.ShapeType.Line) {
			rect(x + position.x as float, y + position.y as float, size.x, size.y)
		}
	}

	@Override
	@Nonnull Rectangle ease(@Nonnull Rectangle other, float f) {
		return centered(center.ease(other.center, f), size.ease(other.size, f))
	}
}