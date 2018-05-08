package pl.shockah.godwit.geom;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Nonnull;

import java8.util.stream.Collectors;
import java8.util.stream.StreamSupport;
import pl.shockah.godwit.gl.Gfx;

public class ComplexShape<T extends Shape> extends AbstractShape {
	@Nonnull protected final Set<T> shapes = new HashSet<>();

	public void add(@Nonnull T shape) {
		shapes.add(shape);
	}

	public void remove(@Nonnull T shape) {
		shapes.remove(shape);
	}

	@SuppressWarnings("unchecked")
	@Override
	@Nonnull public ComplexShape<T> copy() {
		ComplexShape<T> complexShape = new ComplexShape<>();
		for (T shape : shapes) {
			complexShape.add((T)shape.copy());
		}
		return complexShape;
	}

	@Override
	@Nonnull public Rectangle getBoundingBox() {
		if (shapes.isEmpty())
			return new Rectangle(0f, 0f);
		List<Rectangle> boundingBoxes = StreamSupport.stream(shapes).map(Shape::getBoundingBox).collect(Collectors.toList());
		float minX = (float)StreamSupport.stream(boundingBoxes).mapToDouble(box -> box.position.x).min().getAsDouble();
		float minY = (float)StreamSupport.stream(boundingBoxes).mapToDouble(box -> box.position.y).min().getAsDouble();
		float maxX = (float)StreamSupport.stream(boundingBoxes).mapToDouble(box -> box.position.x + box.size.x).min().getAsDouble();
		float maxY = (float)StreamSupport.stream(boundingBoxes).mapToDouble(box -> box.position.y + box.size.y).min().getAsDouble();
		return new Rectangle(minX, minY, maxX - minX, maxY - minY);
	}

	@Override
	public void translate(@Nonnull IVec2 v) {
		for (T shape : shapes) {
			shape.translate(v);
		}
	}

	@Override
	public void mirror(boolean horizontally, boolean vertically) {
		for (T shape : shapes) {
			shape.mirror(horizontally, vertically);
		}
	}

	@Override
	public void scale(float scale) {
		for (T shape : shapes) {
			shape.scale(scale);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean collides(@Nonnull Shape shape, boolean secondTry) {
		if (shape instanceof ComplexShape<?>) {
			return collides((ComplexShape<Shape>)shape);
		} else {
			for (Shape innerShape : shapes) {
				if (innerShape.collides(shape))
					return true;
			}
			return false;
		}
	}

	public boolean collides(@Nonnull ComplexShape<Shape> shape) {
		for (Shape innerShape1 : shapes) {
			for (Shape innerShape2 : shape.shapes) {
				if (innerShape1.collides(innerShape2))
					return true;
			}
		}
		return false;
	}

	public static class Filled<T extends Shape.Filled> extends ComplexShape<T> implements Shape.Filled {
		@SuppressWarnings("unchecked")
		@Override
		@Nonnull public ComplexShape.Filled<T> copy() {
			ComplexShape.Filled<T> complexShape = new ComplexShape.Filled<>();
			for (T shape : shapes) {
				complexShape.add((T)shape.copy());
			}
			return complexShape;
		}

		@Override
		public void drawFilled(@Nonnull Gfx gfx, @Nonnull IVec2 v) {
			for (Shape.Filled shape : shapes) {
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

	public static class Outline<T extends Shape.Outline> extends ComplexShape<T> implements Shape.Outline {
		@SuppressWarnings("unchecked")
		@Override
		@Nonnull public ComplexShape.Outline<T> copy() {
			ComplexShape.Outline<T> complexShape = new ComplexShape.Outline<>();
			for (T shape : shapes) {
				complexShape.add((T)shape.copy());
			}
			return complexShape;
		}

		@Override
		public void drawOutline(@Nonnull Gfx gfx, @Nonnull IVec2 v) {
			for (Shape.Outline shape : shapes) {
				shape.drawOutline(gfx, v);
			}
		}
	}

	public static class FilledOutline<T extends Shape.Filled & Shape.Outline> extends ComplexShape.Filled<T> implements Shape.Outline {
		@SuppressWarnings("unchecked")
		@Override
		@Nonnull public FilledOutline<T> copy() {
			FilledOutline<T> complexShape = new FilledOutline<>();
			for (T shape : shapes) {
				complexShape.add((T)shape.copy());
			}
			return complexShape;
		}

		@Override
		public void drawOutline(@Nonnull Gfx gfx, @Nonnull IVec2 v) {
			for (Shape.Outline shape : shapes) {
				shape.drawOutline(gfx, v);
			}
		}
	}
}