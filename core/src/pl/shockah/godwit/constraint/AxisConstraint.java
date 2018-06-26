package pl.shockah.godwit.constraint;

import javax.annotation.Nonnull;

public abstract class AxisConstraint extends Constraint {
	public enum Axis {
		Horizontal, Vertical
	}

	@Nonnull
	public final Axis axis;

	public AxisConstraint(@Nonnull Axis axis) {
		this.axis = axis;
	}

	@Nonnull
	public static Attribute getCenterAttribute(@Nonnull Axis axis) {
		switch (axis) {
			case Horizontal:
				return Attribute.CenterX;
			case Vertical:
				return Attribute.CenterY;
			default:
				throw new IllegalArgumentException();
		}
	}

	@Nonnull
	public final Attribute getCenterAttribute() {
		return getCenterAttribute(axis);
	}

	@Nonnull
	public static Attribute getLeadingAttribute(@Nonnull Axis axis) {
		switch (axis) {
			case Horizontal:
				return Attribute.Left;
			case Vertical:
				return Attribute.Top;
			default:
				throw new IllegalArgumentException();
		}
	}

	@Nonnull
	public final Attribute getLeadingAttribute() {
		return getLeadingAttribute(axis);
	}

	@Nonnull
	public static Attribute getTrailingAttribute(@Nonnull Axis axis) {
		switch (axis) {
			case Horizontal:
				return Attribute.Right;
			case Vertical:
				return Attribute.Bottom;
			default:
				throw new IllegalArgumentException();
		}
	}

	@Nonnull
	public final Attribute getTrailingAttribute() {
		return getTrailingAttribute(axis);
	}

	@Nonnull
	public static Attribute getLengthAttribute(@Nonnull Axis axis) {
		switch (axis) {
			case Horizontal:
				return Attribute.Width;
			case Vertical:
				return Attribute.Height;
			default:
				throw new IllegalArgumentException();
		}
	}

	@Nonnull
	public final Attribute getLengthAttribute() {
		return getLengthAttribute(axis);
	}
}