package pl.shockah.godwit.geom

import groovy.transform.CompileStatic
import pl.shockah.godwit.gl.Gfx

import javax.annotation.Nonnull

@CompileStatic
class ComplexShape<T extends Shape> extends Shape {
	protected final List<T> shapes = []

	void add(@Nonnull T shape) {
		shapes.add(shape)
	}

	void remove(@Nonnull T shape) {
		shapes.remove(shape)
	}

	@Override
	@Nonnull Shape copy() {
		return copyComplexShape()
	}

	@Nonnull ComplexShape<T> copyComplexShape() {
		def complexShape = new ComplexShape<>()
		for (T shape : shapes) {
			complexShape.add((T)shape.copy())
		}
		return complexShape
	}

	@Override
	Rectangle getBoundingBox() {
		if (shapes.empty)
			return new Rectangle(0f, 0f)
		def boundingBoxes = shapes.collect { it.boundingBox }
		def minX = boundingBoxes.collect { it.position.x }.min() as float
		def minY = boundingBoxes.collect { it.position.y }.min() as float
		def maxX = boundingBoxes.collect { it.position.x + it.size.x }.min() as float
		def maxY = boundingBoxes.collect { it.position.x + it.size.y }.min() as float
		return new Rectangle(minX, minY, maxX - minX as float, maxY - minY as float)
	}

	@Override
	void translate(float x, float y) {
		for (T shape : shapes) {
			shape.translate(x, y)
		}
	}

	static class Filled<T extends Shape & Shape.Filled> extends ComplexShape<T> implements Shape.Filled {
		@Override
		@Nonnull ComplexShape<T> copyComplexShape() {
			return copyComplexShapeFilled()
		}

		@Nonnull Filled<T> copyComplexShapeFilled() {
			def complexShape = new Filled<>()
			for (T shape : shapes) {
				complexShape.add((T)shape.copy())
			}
			return complexShape
		}

		@Override
		void drawFilled(@Nonnull Gfx gfx, float x, float y) {
			for (Shape.Filled shape : shapes) {
				shape.drawFilled(gfx, x, y)
			}
		}

		@Override
		boolean contains(float x, float y) {
			for (T shape : shapes) {
				if ((shape as Shape.Filled).contains(x, y))
					return true
			}
			return false
		}
	}

	static class Outline<T extends Shape & Shape.Outline> extends ComplexShape<T> implements Shape.Outline {
		@Override
		@Nonnull ComplexShape<T> copyComplexShape() {
			return copyComplexShapeOutline()
		}

		@Nonnull Outline<T> copyComplexShapeOutline() {
			def complexShape = new Outline<>()
			for (T shape : shapes) {
				complexShape.add((T)shape.copy())
			}
			return complexShape
		}

		@Override
		void drawOutline(@Nonnull Gfx gfx, float x, float y) {
			for (Shape.Outline shape : shapes) {
				shape.drawOutline(gfx, x, y)
			}
		}
	}

	static class FilledOutline<T extends Shape & Shape.Filled & Shape.Outline> extends Filled<T> implements Shape.Outline {
		@Override
		@Nonnull ComplexShape<T> copyComplexShape() {
			return copyComplexShapeFilledOutline()
		}

		@Nonnull FilledOutline<T> copyComplexShapeFilledOutline() {
			def complexShape = new FilledOutline<>()
			for (T shape : shapes) {
				complexShape.add((T)shape.copy())
			}
			return complexShape
		}

		@Override
		void drawOutline(@Nonnull Gfx gfx, float x, float y) {
			for (Shape.Outline shape : shapes) {
				shape.drawOutline(gfx, x, y)
			}
		}
	}
}