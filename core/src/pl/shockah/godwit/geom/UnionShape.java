package pl.shockah.godwit.geom;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Nonnull;

import java8.util.stream.Collectors;
import java8.util.stream.StreamSupport;
import pl.shockah.godwit.gl.Gfx;

public class UnionShape<T extends Shape> extends AbstractShape {
	@Nonnull
	protected final Set<T> shapes = new HashSet<>();

	public void add(@Nonnull T shape) {
		shapes.add(shape);
	}

	public void remove(@Nonnull T shape) {
		shapes.remove(shape);
	}

	@SuppressWarnings("unchecked")
	@Override
	@Nonnull
	public UnionShape<T> copy() {
		UnionShape<T> unionShape = new UnionShape<>();
		for (T shape : shapes) {
			unionShape.add((T)shape.copy());
		}
		return unionShape;
	}

	@Override
	@Nonnull
	public Rectangle getBoundingBox() {
		if (shapes.isEmpty())
			return new Rectangle(0f, 0f);
		List<Rectangle> boundingBoxes = StreamSupport.stream(shapes).map(Shape::getBoundingBox).collect(Collectors.toList());
		float minX = (float)StreamSupport.stream(boundingBoxes).mapToDouble(box -> box.position.x).min().getAsDouble();
		float minY = (float)StreamSupport.stream(boundingBoxes).mapToDouble(box -> box.position.y).min().getAsDouble();
		float maxX = (float)StreamSupport.stream(boundingBoxes).mapToDouble(box -> box.position.x + box.size.x).max().getAsDouble();
		float maxY = (float)StreamSupport.stream(boundingBoxes).mapToDouble(box -> box.position.y + box.size.y).max().getAsDouble();
		return new Rectangle(minX, minY, maxX - minX, maxY - minY);
	}

	@Nonnull
	@Override
	public UnionShape<T> translate(@Nonnull IVec2 v) {
		for (T shape : shapes) {
			shape.translate(v);
		}
		return this;
	}

	@Nonnull
	@Override
	public UnionShape<T> mirror(boolean horizontally, boolean vertically) {
		for (T shape : shapes) {
			shape.mirror(horizontally, vertically);
		}
		return this;
	}

	@Nonnull
	@Override
	public UnionShape<T> scale(float scale) {
		for (T shape : shapes) {
			shape.scale(scale);
		}
		return this;
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean collides(@Nonnull Shape shape, boolean secondTry) {
		if (shape instanceof UnionShape<?>) {
			return collides((UnionShape<Shape>)shape);
		} else {
			for (Shape innerShape : shapes) {
				if (innerShape.collides(shape))
					return true;
			}
			return false;
		}
	}

	public boolean collides(@Nonnull UnionShape<Shape> shape) {
		for (Shape innerShape1 : shapes) {
			for (Shape innerShape2 : shape.shapes) {
				if (innerShape1.collides(innerShape2))
					return true;
			}
		}
		return false;
	}

	public static class Filled<T extends Shape.Filled> extends UnionShape<T> implements Shape.Filled {
		@SuppressWarnings("unchecked")
		@Override
		@Nonnull
		public UnionShape.Filled<T> copy() {
			UnionShape.Filled<T> unionShape = new UnionShape.Filled<>();
			for (T shape : shapes) {
				unionShape.add((T)shape.copy());
			}
			return unionShape;
		}

		@Override
		public void drawFilled(@Nonnull Gfx gfx, @Nonnull IVec2 v) {
			for (T shape : shapes) {
				shape.drawFilled(gfx, v);
			}
		}

		@Override
		public boolean contains(@Nonnull IVec2 v) {
			for (T shape : shapes) {
				if (shape.contains(v))
					return true;
			}
			return false;
		}
	}

	public static class Outline<T extends Shape.Outline> extends UnionShape<T> implements Shape.Outline {
		@SuppressWarnings("unchecked")
		@Override
		@Nonnull
		public UnionShape.Outline<T> copy() {
			UnionShape.Outline<T> unionShape = new UnionShape.Outline<>();
			for (T shape : shapes) {
				unionShape.add((T)shape.copy());
			}
			return unionShape;
		}

		@Override
		public void drawOutline(@Nonnull Gfx gfx, @Nonnull IVec2 v) {
			for (T shape : shapes) {
				shape.drawOutline(gfx, v);
			}
		}
	}

	public static class Closed<T extends Shape.Filled & Shape.Outline> extends UnionShape.Filled<T> implements Shape.Outline {
		@SuppressWarnings("unchecked")
		@Override
		@Nonnull
		public Closed<T> copy() {
			Closed<T> unionShape = new Closed<>();
			for (T shape : shapes) {
				unionShape.add((T)shape.copy());
			}
			return unionShape;
		}

		@Override
		public void drawOutline(@Nonnull Gfx gfx, @Nonnull IVec2 v) {
			for (T shape : shapes) {
				shape.drawOutline(gfx, v);
			}
		}
	}
}