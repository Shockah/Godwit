package pl.shockah.godwit.ui

import groovy.transform.CompileStatic
import pl.shockah.godwit.geom.Shape
import pl.shockah.godwit.geom.Vec2
import pl.shockah.godwit.gl.Gfx

import javax.annotation.Nonnull
import javax.annotation.Nullable

@CompileStatic
abstract class ShapeView<T extends Shape> extends View {
	@Nullable T shape

	ShapeView(@Nullable T shape) {
		this.shape = shape
	}

	@Override
	Vec2 getIntrinsicSize(@Nonnull Vec2 availableSize) {
		return shape?.boundingBox?.size ?: new Vec2()
	}

	static class Filled<T extends Shape & Shape.Filled> extends ShapeView<T> {
		Filled(@Nullable T shape) {
			super(shape)
		}

		@Override
		void onRender(@Nonnull Gfx gfx, float x, float y) {
			super.onRender(gfx, x, y)
			shape?.with {
				gfx.drawFilled(this as Shape.Filled, -boundingBox.position + new Vec2(x, y))
			}
		}
	}

	static class Outline<T extends Shape & Shape.Outline> extends ShapeView<T> {
		Outline(@Nullable T shape) {
			super(shape)
		}

		@Override
		void onRender(@Nonnull Gfx gfx, float x, float y) {
			super.onRender(gfx, x, y)
			shape?.with {
				gfx.drawOutline(this as Shape.Outline, -boundingBox.position + new Vec2(x, y))
			}
		}
	}
}