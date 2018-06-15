package pl.shockah.godwit.geom;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import pl.shockah.godwit.Godwit;
import pl.shockah.godwit.geom.polygon.Polygon;
import pl.shockah.godwit.geom.polygon.Polygonable;
import pl.shockah.godwit.gl.Gfx;
import pl.shockah.godwit.gl.GfxSprite;
import pl.shockah.godwit.ui.Padding;

@EqualsAndHashCode(callSuper = false)
public class Rectangle extends AbstractShape implements Shape.Filled, Shape.Outline, Polygonable {
	@Nonnull
	public MutableVec2 position;

	@Nonnull
	public MutableVec2 size;

	@Getter(lazy = true)
	private final GfxSprite sprite = new GfxSprite(new Sprite(Godwit.getInstance().getPixelTexture()));

	public static Rectangle centered(float x, float y, float w, float h) {
		return centered(new MutableVec2(x, y), new MutableVec2(w, h));
	}

	public static Rectangle centered(float x, float y, @Nonnull IVec2 size) {
		return centered(new MutableVec2(x, y), size);
	}

	public static Rectangle centered(@Nonnull IVec2 position, float w, float h) {
		return centered(position, new MutableVec2(w, h));
	}

	public static Rectangle centered(@Nonnull IVec2 position, @Nonnull IVec2 size) {
		return new Rectangle(position.subtract(size.multiply(0.5f)), size);
	}

	public static Rectangle centered(float x, float y, float l) {
		return centered(new MutableVec2(x, y), new MutableVec2(l, l));
	}

	public static Rectangle centered(@Nonnull IVec2 position, float l) {
		return centered(position, new MutableVec2(l, l));
	}

	public static Rectangle centered(float w, float h) {
		return centered(Vec2.zero, new MutableVec2(w, h));
	}

	public static Rectangle centered(@Nonnull IVec2 size) {
		return centered(Vec2.zero, size);
	}

	public Rectangle(float x, float y, float w, float h) {
		this(new MutableVec2(x, y), new MutableVec2(w, h));
	}

	public Rectangle(float x, float y, @Nonnull IVec2 size) {
		this(new MutableVec2(x, y), size);
	}

	public Rectangle(@Nonnull IVec2 position, float w, float h) {
		this(position, new MutableVec2(w, h));
	}

	public Rectangle(@Nonnull IVec2 position, @Nonnull IVec2 size) {
		this.position = position.mutableCopy();
		this.size = size.mutableCopy();
	}

	public Rectangle(float x, float y, float l) {
		this(new MutableVec2(x, y), new MutableVec2(l, l));
	}

	public Rectangle(@Nonnull IVec2 pos, float l) {
		this(pos, new MutableVec2(l, l));
	}

	public Rectangle(float w, float h) {
		this(Vec2.zero, new MutableVec2(w, h));
	}

	public Rectangle(@Nonnull IVec2 size) {
		this(Vec2.zero, size);
	}

	public Rectangle() {
		this(0f, 0f);
	}

	public Rectangle(@Nonnull com.badlogic.gdx.math.Rectangle gdxRectangle) {
		this(gdxRectangle.x, gdxRectangle.y, gdxRectangle.width, gdxRectangle.height);
	}

	@Nonnull
	public com.badlogic.gdx.math.Rectangle toGdx() {
		return new com.badlogic.gdx.math.Rectangle(position.x, position.y, size.x, size.y);
	}

	@Override
	@Nonnull
	public Rectangle copy() {
		return new Rectangle(position, size);
	}

	@Override
	public String toString() {
		return String.format("[Rectangle: @%s, %.2fx%.2f]", position, size.x, size.y);
	}

	@Override
	@Nonnull
	public Rectangle getBoundingBox() {
		return copy();
	}

	@Nonnull
	public Vec2 getCenter() {
		return position.add(size.multiply(0.5f));
	}

	@Nonnull
	@Override
	public Rectangle translate(@Nonnull IVec2 v) {
		position.x += v.x();
		position.y += v.y();
		return this;
	}

	@Nonnull
	@Override
	public Rectangle mirror(boolean horizontally, boolean vertically) {
		if (horizontally)
			position.x = -position.x - size.x;
		if (vertically)
			position.y = -position.y - size.y;
		return this;
	}

	@Nonnull
	@Override
	public Rectangle scale(float scale) {
		position.set(position.multiply(scale));
		size.set(size.multiply(scale));
		return this;
	}

	@Override
	public boolean contains(@Nonnull IVec2 v) {
		return v.x() >= position.x && v.y() >= position.y && v.x() < position.x + size.x && v.y() < position.y + size.y;
	}

	@Override
	public boolean collides(@Nonnull Shape shape, boolean secondTry) {
		if (shape instanceof Rectangle)
			return collides((Rectangle)shape);
		if (shape instanceof Line)
			return collides((Line)shape);
		return super.collides(shape, secondTry);
	}

	public boolean collides(@Nonnull Rectangle rectangle) {
		return collides(rectangle.position.x, rectangle.position.y, rectangle.size.x, rectangle.size.y);
	}

	public boolean collides(float x, float y, float w, float h) {
		return position.x < x + w
				&& position.x + size.x > x
				&& position.y < y + h
				&& position.y + size.y > y;
	}

	public boolean collides(@Nonnull Line line) {
		if (contains(line.point1) || contains(line.point2))
			return true;
		for (Line myLine : getLines()) {
			if (myLine.collides(line))
				return true;
		}
		return false;
	}

	@Nonnull
	public List<Line> getLines() {
		List<Line> lines = new ArrayList<>();
		lines.add(new Line(position.x, position.y, position.x + size.x, position.y));
		lines.add(new Line(position.x + size.x, position.y, position.x + size.x, position.y + size.y));
		lines.add(new Line(position.x + size.x, position.y + size.y, position.x, position.y + size.y));
		lines.add(new Line(position.x, position.y + size.y, position.x, position.y));
		return lines;
	}

	@Override
	@Nonnull
	public Polygon asPolygon() {
		Polygon p = new Polygon.NoHoles();
		p.addPoint(position);
		p.addPoint(position.add(size.withY(0f)));
		p.addPoint(position.add(size));
		p.addPoint(position.add(size.withX(0f)));
		return p;
	}

	@Nonnull
	public Rectangle withPadding(@Nonnull Padding padding) {
		return new Rectangle(
				position.x - padding.left.getPixels(),
				position.y - padding.top.getPixels(),
				size.x + padding.left.getPixels() + padding.right.getPixels(),
				size.y + padding.top.getPixels() + padding.bottom.getPixels()
		);
	}

	@Nonnull
	public Rectangle withoutPadding(@Nonnull Padding padding) {
		return new Rectangle(
				position.x + padding.left.getPixels(),
				position.y + padding.top.getPixels(),
				size.x - padding.left.getPixels() - padding.right.getPixels(),
				size.y - padding.top.getPixels() - padding.bottom.getPixels()
		);
	}

	@Override
	public void drawFilled(@Nonnull Gfx gfx, @Nonnull IVec2 v) {
		GfxSprite sprite = getSprite();
		sprite.setColor(gfx.getColor());
		sprite.setSize(size);
		gfx.draw(sprite, v.add(position));
	}

	@Override
	public void drawOutline(@Nonnull Gfx gfx, @Nonnull IVec2 v) {
		gfx.prepareShapes(ShapeRenderer.ShapeType.Line);
		gfx.getShapeRenderer().rect(v.x() + position.x, v.y() + position.y, size.x, size.y);
	}
}