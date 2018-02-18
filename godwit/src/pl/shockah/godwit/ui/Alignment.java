package pl.shockah.godwit.ui;

import javax.annotation.Nonnull;

import pl.shockah.godwit.geom.IVec2;
import pl.shockah.godwit.geom.ImmutableVec2;

public interface Alignment {
	@Nonnull IVec2 getVector();

	@Nonnull default IVec2 getNonNanVector() {
		return getNonNanVector(1f);
	}

	@Nonnull default IVec2 getNonNanVector(float nanValue) {
		IVec2 v = getVector();
		return new ImmutableVec2(Float.isNaN(v.x()) ? nanValue : v.x(), Float.isNaN(v.y()) ? nanValue : v.y());
	}

	enum Horizontal implements Alignment {
		Left(new ImmutableVec2(0f, Float.NaN)),
		Center(new ImmutableVec2(0.5f, Float.NaN)),
		Right(new ImmutableVec2(1f, Float.NaN));

		@Nonnull private final IVec2 vector;

		Horizontal(@Nonnull IVec2 vector) {
			this.vector = vector;
		}

		@Override
		@Nonnull public IVec2 getVector() {
			return vector;
		}

		@Nonnull public Plane and(@Nonnull Vertical vertical) {
			return new Plane(this, vertical);
		}
	}

	enum Vertical implements Alignment {
		Top(new ImmutableVec2(Float.NaN, 0f)),
		Middle(new ImmutableVec2(Float.NaN, 0.5f)),
		Bottom(new ImmutableVec2(Float.NaN, 1f));

		@Nonnull private final IVec2 vector;

		Vertical(@Nonnull IVec2 vector) {
			this.vector = vector;
		}

		@Override
		@Nonnull public IVec2 getVector() {
			return vector;
		}

		@Nonnull public Plane and(@Nonnull Horizontal horizontal) {
			return new Plane(horizontal, this);
		}
	}

	class Plane implements Alignment {
		@Nonnull public final Horizontal horizontal;
		@Nonnull public final Vertical vertical;

		private Plane(@Nonnull Horizontal horizontal, @Nonnull Vertical vertical) {
			this.horizontal = horizontal;
			this.vertical = vertical;
		}

		@Override
		@Nonnull public IVec2 getVector() {
			return horizontal.vector.getOnlyX() + vertical.vector.getOnlyY();
		}
	}
}