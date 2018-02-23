package pl.shockah.godwit.geom;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import javax.annotation.Nonnull;

import lombok.EqualsAndHashCode;
import pl.shockah.godwit.geom.polygon.Polygon;
import pl.shockah.godwit.geom.polygon.Polygonable;
import pl.shockah.godwit.gl.Gfx;

@EqualsAndHashCode(callSuper = false)
public class Rectangle extends Shape implements Shape.Filled, Shape.Outline, Polygonable {
	@Nonnull public Vec2 position;
	@Nonnull public Vec2 size;

	public static Rectangle centered(float x, float y, float w, float h) {
		return centered(new Vec2(x, y), new Vec2(w, h));
	}

	public static Rectangle centered(float x, float y, @Nonnull IVec2 size) {
		return centered(new Vec2(x, y), size);
	}

	public static Rectangle centered(@Nonnull IVec2 position, float w, float h) {
		return centered(position, new Vec2(w, h));
	}

	public static Rectangle centered(@Nonnull IVec2 position, @Nonnull IVec2 size) {
		return new Rectangle(position - size * 0.5f, size);
	}

	public static Rectangle centered(float x, float y, float l) {
		return centered(new Vec2(x, y), new Vec2(l, l));
	}

	public static Rectangle centered(@Nonnull IVec2 position, float l) {
		return centered(position, new Vec2(l, l));
	}

	public static Rectangle centered(float w, float h) {
		return centered(ImmutableVec2.zero, new Vec2(w, h));
	}

	public static Rectangle centered(@Nonnull IVec2 size) {
		return centered(ImmutableVec2.zero, size);
	}

	public Rectangle(float x, float y, float w, float h) {
		this(new Vec2(x, y), new Vec2(w, h));
	}

	public Rectangle(float x, float y, @Nonnull IVec2 size) {
		this(new Vec2(x, y), size);
	}

	public Rectangle(@Nonnull IVec2 position, float w, float h) {
		this(position, new Vec2(w, h));
	}

	public Rectangle(@Nonnull IVec2 position, @Nonnull IVec2 size) {
		this.position = position.getMutableCopy();
		this.size = size.getMutableCopy();
	}

	public Rectangle(float x, float y, float l) {
		this(new Vec2(x, y), new Vec2(l, l));
	}

	public Rectangle(@Nonnull IVec2 pos, float l) {
		this(pos, new Vec2(l, l));
	}

	public Rectangle(float w, float h) {
		this(ImmutableVec2.zero, new Vec2(w, h));
	}

	public Rectangle(@Nonnull IVec2 size) {
		this(ImmutableVec2.zero, size);
	}

	@Override
	@Nonnull public Shape copy() {
		return copyRectangle();
	}

	@Nonnull Rectangle copyRectangle() {
		return new Rectangle(position, size);
	}

	@Override
	public String toString() {
		return String.format("[Rectangle: @%s, %.2fx%.2f]", position, size.x, size.y);
	}

	@Override
	@Nonnull public Rectangle getBoundingBox() {
		return copyRectangle();
	}

	@Nonnull IVec2 getCenter() {
		return position + size * 0.5f;
	}

	@Override
	public void translate(@Nonnull IVec2 v) {
		position.x += v.x();
		position.y += v.y();
	}

	@Override
	public void mirror(boolean horizontally, boolean vertically) {
		if (horizontally)
			position.x = -position.x - size.x;
		if (vertically)
			position.y = -position.y - size.y;
	}

	@Override
	public boolean contains(@Nonnull IVec2 v) {
		return v.x() >= position.x && v.y() >= position.y && v.x() < position.x + size.x && v.y() < position.y + size.y;
	}

	@Override
	protected boolean collides(@Nonnull Shape shape, boolean secondTry) {
		if (shape instanceof Rectangle)
			return collides((Rectangle)shape);
		if (shape instanceof Line)
			return collides((Line)shape);
		return super.collides(shape, secondTry);
	}

	public boolean collides(@Nonnull Rectangle rectangle) {
		IVec2 v = (getCenter() - rectangle.getCenter()).getAbs() - (size + rectangle.size) * 0.5f;
		return v.x() < 0 && v.y() < 0;
	}

	public boolean collides(@Nonnull Line line) {
		if (contains(line.point1) || contains(line.point2))
			return true;
		for (Line myLine : asPolygon().getLines()) {
			if (myLine.collides(line))
				return true;
		}
		return false;
	}

	@Override
	@Nonnull public Polygon asPolygon() {
		Polygon p = new Polygon.NoHoles();
		p.addPoint(position);
		p.addPoint(position + size.getOnlyX());
		p.addPoint(position + size);
		p.addPoint(position + size.getOnlyY());
		return p;
	}

	@Override
	public void drawFilled(@Nonnull Gfx gfx, @Nonnull IVec2 v) {
		gfx.prepareShapes(ShapeRenderer.ShapeType.Filled, () -> {
			gfx.getShapeRenderer().rect(v.x() + position.x, v.y() + position.y, size.x, size.y);
		});
	}

	@Override
	public void drawOutline(@Nonnull Gfx gfx, @Nonnull IVec2 v) {
		gfx.prepareShapes(ShapeRenderer.ShapeType.Line, () -> {
			gfx.getShapeRenderer().rect(v.x() + position.x, v.y() + position.y, size.x, size.y);
		});
	}
}