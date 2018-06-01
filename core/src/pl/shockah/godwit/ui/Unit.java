package pl.shockah.godwit.ui;

import javax.annotation.Nonnull;

import lombok.EqualsAndHashCode;
import pl.shockah.godwit.Godwit;

public abstract class Unit {
	@Nonnull
	public static final Unit Zero = new Unit() {
		@Override
		public float getPixels() {
			return 0;
		}
	};

	public abstract float getPixels();

	@Nonnull
	public final Unit and(@Nonnull Unit unit) {
		return new Compound(this, unit);
	}

	@EqualsAndHashCode(callSuper = false)
	public static final class Pixels extends Unit {
		public final float pixels;

		public Pixels(float pixels) {
			this.pixels = pixels;
		}

		@Override
		public float getPixels() {
			return pixels;
		}

		@Override
		public String toString() {
			return String.format("[Pixels: %.1f]", pixels);
		}
	}

	@EqualsAndHashCode(callSuper = false)
	public static final class Inches extends Unit {
		public final float inches;

		public Inches(float inches) {
			this.inches = inches;
		}

		@Override
		public float getPixels() {
			return inches * Godwit.getInstance().getPpi().x;
		}

		@Override
		public String toString() {
			return String.format("[Inches: %.1f]", inches);
		}
	}

	@EqualsAndHashCode(callSuper = false)
	public static final class ScreenWidths extends Unit {
		public final float widths;

		public ScreenWidths(float widths) {
			this.widths = widths;
		}

		@Override
		public float getPixels() {
			return widths * Godwit.getInstance().gfx.getWidth();
		}

		@Override
		public String toString() {
			return String.format("[Widths: %.1f]", widths);
		}
	}

	@EqualsAndHashCode(callSuper = false)
	public static final class ScreenHeights extends Unit {
		public final float heights;

		public ScreenHeights(float heights) {
			this.heights = heights;
		}

		@Override
		public float getPixels() {
			return heights * Godwit.getInstance().gfx.getHeight();
		}

		@Override
		public String toString() {
			return String.format("[Heights: %.1f]", heights);
		}
	}

	@EqualsAndHashCode(callSuper = false)
	public static final class Compound extends Unit {
		@Nonnull
		public final Unit first;

		@Nonnull
		public final Unit second;

		public Compound(@Nonnull Unit first, @Nonnull Unit second) {
			this.first = first;
			this.second = second;
		}

		@Override
		public float getPixels() {
			return first.getPixels() + second.getPixels();
		}
	}
}