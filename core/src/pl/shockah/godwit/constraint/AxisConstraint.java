package pl.shockah.godwit.constraint;

import javax.annotation.Nonnull;

public abstract class AxisConstraint extends Constraint {
	@Nonnull
	public final Axis axis;

	public AxisConstraint(@Nonnull Axis axis) {
		this.axis = axis;
	}

	@Nonnull
	protected final Attribute getCenterAttribute() {
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
	protected final Attribute getLeadingAttribute() {
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
	protected final Attribute getTrailingAttribute() {
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
	protected final Attribute getLengthAttribute() {
		switch (axis) {
			case Horizontal:
				return Attribute.Width;
			case Vertical:
				return Attribute.Height;
			default:
				throw new IllegalArgumentException();
		}
	}

	public enum Axis {
		Horizontal, Vertical
	}
}