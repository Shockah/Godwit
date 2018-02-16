package pl.shockah.godwit.ui

import groovy.transform.CompileStatic
import pl.shockah.godwit.geom.IVec2
import pl.shockah.godwit.geom.ImmutableVec2

import javax.annotation.Nonnull

@CompileStatic
trait Alignment {
	@Nonnull abstract IVec2 getVector()

	@Nonnull IVec2 getNonNanVector(float nanValue = 1f) {
		IVec2 v = vector
		return new ImmutableVec2(Float.isNaN(v.x) ? nanValue : v.x, Float.isNaN(v.y) ? nanValue : v.y)
	}

	static enum Horizontal implements Alignment {
		Left(new ImmutableVec2(0f, Float.NaN)), Center(new ImmutableVec2(0.5f, Float.NaN)), Right(new ImmutableVec2(1f, Float.NaN))

		@Nonnull private final IVec2 vector

		private Horizontal(@Nonnull IVec2 vector) {
			this.vector = vector
		}

		@Override
		@Nonnull IVec2 getVector() {
			return vector
		}

		@Nonnull Plane and(@Nonnull Vertical vertical) {
			return new Plane(this, vertical)
		}
	}

	static enum Vertical implements Alignment {
		Top(new ImmutableVec2(Float.NaN, 0f)), Middle(new ImmutableVec2(Float.NaN, 0.5f)), Bottom(new ImmutableVec2(Float.NaN, 1f))

		@Nonnull private final IVec2 vector

		private Vertical(@Nonnull IVec2 vector) {
			this.vector = vector
		}

		@Override
		@Nonnull IVec2 getVector() {
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
		@Nonnull IVec2 getVector() {
			return horizontal.vector.onlyX + vertical.vector.onlyY
		}
	}
}