package pl.shockah.godwit.geom

import groovy.transform.CompileStatic
import groovy.transform.SelfType
import pl.shockah.godwit.gl.Gfx

import javax.annotation.Nonnull

@CompileStatic
abstract class Shape {
	@Nonnull abstract Shape copy()

	@Nonnull abstract Rectangle getBoundingBox()

	final void translate(@Nonnull IVec2 v) {
		translate(v.x, v.y)
	}

	abstract void translate(float x, float y)

	final boolean collides(@Nonnull Shape shape) {
		return collides(shape, true)
	}

	boolean collides(@Nonnull Shape shape, boolean tryAgain) {
		if (tryAgain)
			return shape.collides(this, false)
		throw new IllegalArgumentException()
	}

	@SelfType(Shape)
	trait Filled {
		void drawFilled(@Nonnull Gfx gfx, @Nonnull IVec2 v) {
			drawFilled(gfx, v.x, v.y)
		}

		abstract void drawFilled(@Nonnull Gfx gfx, float x, float y)

		boolean contains(@Nonnull IVec2 v) {
			return contains(v.x, v.y)
		}

		abstract boolean contains(float x, float y)

		@Nonnull Entity asFilledEntity() {
			return new Entity(this)
		}

		static class Entity extends pl.shockah.godwit.Entity {
			@Nonnull @Delegate(interfaces = false, excludes = "asFilledEntity") final Filled shape

			Entity(@Nonnull Filled shape) {
				this.shape = shape
			}

			@Override
			void onRender(@Nonnull Gfx gfx, float x, float y) {
				super.onRender(gfx, x, y)
				gfx.drawFilled(shape, x, y)
			}
		}
	}

	@SelfType(Shape)
	trait Outline {
		void drawOutline(@Nonnull Gfx gfx, @Nonnull IVec2 v) {
			drawOutline(gfx, v.x, v.y)
		}

		abstract void drawOutline(@Nonnull Gfx gfx, float x, float y)

		@Nonnull Entity asOutlineEntity() {
			return new Entity(this)
		}

		static class Entity extends pl.shockah.godwit.Entity {
			@Nonnull @Delegate(interfaces = false, excludes = "asOutlineEntity") final Outline shape

			Entity(@Nonnull Outline shape) {
				this.shape = shape
			}

			@Override
			void onRender(@Nonnull Gfx gfx, float x, float y) {
				super.onRender(gfx, x, y)
				gfx.drawOutline(shape, x, y)
			}
		}
	}
}