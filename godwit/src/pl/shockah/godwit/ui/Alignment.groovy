package pl.shockah.godwit.ui

import groovy.transform.CompileStatic
import pl.shockah.godwit.geom.Vec2

import javax.annotation.Nonnull

@CompileStatic
interface Alignment {
	@Nonnull Vec2 getVector()

	static enum Horizontal implements Alignment {
		Left(new Vec2(0f, Float.NaN)), Center(new Vec2(0.5f, Float.NaN)), Right(new Vec2(1f, Float.NaN))

		@Nonnull private final Vec2 vector

		private Horizontal(@Nonnull Vec2 vector) {
			this.vector = vector
		}

		@Override
		@Nonnull Vec2 getVector() {
			return vector
		}

		@Nonnull Plane and(@Nonnull Vertical vertical) {
			return new Plane(this, vertical)
		}
	}

	static enum Vertical implements Alignment {
		Top(new Vec2(Float.NaN, 0f)), Middle(new Vec2(Float.NaN, 0.5f)), Bottom(new Vec2(Float.NaN, 1f))

		@Nonnull private final Vec2 vector

		private Vertical(@Nonnull Vec2 vector) {
			this.vector = vector
		}

		@Override
		@Nonnull Vec2 getVector() {
			return vector
		}

		@Nonnull Plane and(@Nonnull Horizontal horizontal) {
			return new Plane(horizontal, this)
		}
	}

	static class Plane implements Alignment {
		@Nonnull final Horizontal horizontal
		@Nonnull final Vertical vertical

		private Plane(@Nonnull Horizontal horizontal, @Nonnull Vertical vertical) {
			this.horizontal = horizontal
			this.vertical = vertical
		}

		@Override
		@Nonnull Vec2 getVector() {
			return horizontal.vector.onlyX + vertical.vector.onlyY
		}
	}
}