package pl.shockah.godwit.ui;

import com.badlogic.gdx.utils.Align;

import javax.annotation.Nonnull;

import lombok.Getter;
import pl.shockah.godwit.geom.IVec2;
import pl.shockah.godwit.geom.ImmutableVec2;

public interface Alignment {
	@Nonnull IVec2 getVector();

	int getGdxAlignment();

	@Nonnull default IVec2 getNonNanVector() {
		return getNonNanVector(1f);
	}

	@Nonnull default IVec2 getNonNanVector(float nanValue) {
		IVec2 v = getVector();
		return new ImmutableVec2(Float.isNaN(v.x()) ? nanValue : v.x(), Float.isNaN(v.y()) ? nanValue : v.y());
	}

	default int getHorizontalGdxAlignment() {
		return getGdxAlignment() & (Align.center | Align.left | Align.right);
	}

	default int getVerticalGdxAlignment() {
		return getGdxAlignment() & (Align.center | Align.top | Align.bottom);
	}

	enum Horizontal implements Alignment {
		Left(new ImmutableVec2(0f, Float.NaN)),
		Center(new ImmutableVec2(0.5f, Float.NaN)),
		Right(new ImmutableVec2(1f, Float.NaN));

		@Getter
		@Nonnull private final IVec2 vector;

		Horizontal(@Nonnull IVec2 vector) {
			this.vector = vector;
		}

		@Nonnull public Plane and(@Nonnull Vertical vertical) {
			return new Plane(this, vertical);
		}

		@Override
		public int getGdxAlignment() {
			switch (this) {
				case Left: return Align.left;
				case Center: return Align.center;
				case Right: return Align.right;
			}
			throw new IllegalStateException();
		}
	}

	enum Vertical implements Alignment {
		Top(new ImmutableVec2(Float.NaN, 0f)),
		Middle(new ImmutableVec2(Float.NaN, 0.5f)),
		Bottom(new ImmutableVec2(Float.NaN, 1f));

		@Getter
		@Nonnull private final IVec2 vector;

		Vertical(@Nonnull IVec2 vector) {
			this.vector = vector;
		}

		@Nonnull public Plane and(@Nonnull Horizontal horizontal) {
			return new Plane(horizontal, this);
		}

		@Override
		public int getGdxAlignment() {
			switch (this) {
				case Top: return Align.top;
				case Middle: return Align.center;
				case Bottom: return Align.bottom;
			}
			throw new IllegalStateException();
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
			return new ImmutableVec2(horizontal.vector.x(), vertical.vector.y());
		}

		@Override
		public int getGdxAlignment() {
			switch (horizontal) {
				case Left: switch (vertical) {
					case Top: return Align.topLeft;
					case Middle: return Align.left;
					case Bottom: return Align.bottomLeft;
				}
				case Center: switch (vertical) {
					case Top: return Align.top;
					case Middle: return Align.center;
					case Bottom: return Align.bottom;
				}
				case Right: switch (vertical) {
					case Top: return Align.topRight;
					case Middle: return Align.right;
					case Bottom: return Align.bottomRight;
				}
			}
			throw new IllegalStateException();
		}
	}
}