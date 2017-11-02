package pl.shockah.godwit.geom

import groovy.transform.CompileStatic
import groovy.transform.SelfType
import pl.shockah.godwit.gl.Gfx

import javax.annotation.Nonnull

@CompileStatic
abstract class Shape {
	@Nonnull abstract Shape copy()

	@Nonnull abstract Rectangle getBoundingBox()

	final void translate(@Nonnull Vec2 v) {
		translate(v.x, v.y)
	}

	abstract void translate(float x, float y)

	final boolean collides(@Nonnull Shape shape) {
		return collides(shape, true)
	}

	protected boolean collides(@Nonnull Shape shape, boolean tryAgain) {
		if (tryAgain)
			return shape.collides(this, false)
		throw new IllegalArgumentException()
	}

	@SelfType(Shape)
	trait Filled {
		void drawFilled(@Nonnull Gfx gfx, @Nonnull Vec2 v) {
			drawFilled(gfx, v.x, v.y)
		}

		abstract void drawFilled(@Nonnull Gfx gfx, float x, float y)

		boolean contains(@Nonnull Vec2 v) {
			return contains(v.x, v.y)
		}

		abstract boolean contains(float x, float y)
	}

	@SelfType(Shape)
	trait Outline {
		void drawOutline(@Nonnull Gfx gfx, @Nonnull Vec2 v) {
			drawOutline(gfx, v.x, v.y)
		}

		abstract void drawOutline(@Nonnull Gfx gfx, float x, float y)
	}
}