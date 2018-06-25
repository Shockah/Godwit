package pl.shockah.godwit.constraint;

import javax.annotation.Nonnull;

import pl.shockah.godwit.ui.Unit;

public abstract class Constraint {
	public abstract void apply();

	public enum Attribute {
		Left, Right, Top, Bottom, Width, Height, CenterX, CenterY;

		public boolean isPositional() {
			return this != Width && this != Height;
		}

		public boolean isDimensional() {
			return !isPositional();
		}

		@Nonnull
		public AxisConstraint.Axis getAxis() {
			return this == Left || this == Right || this == Width || this == CenterX ? AxisConstraint.Axis.Horizontal : AxisConstraint.Axis.Vertical;
		}
	}

	public static final class InstancedAttribute {
		@Nonnull
		public final Constrainable item;

		@Nonnull
		public final Attribute attribute;

		public InstancedAttribute(@Nonnull Constrainable item, @Nonnull Attribute attribute) {
			this.item = item;
			this.attribute = attribute;
		}

		public float get() {
			return item.getAttribute(attribute);
		}

		public void set(float value) {
			item.setAttribute(attribute, value);
		}

		@Nonnull
		public BasicConstraint constraint(@Nonnull Constrainable source) {
			return new BasicConstraint(this, source.getAttributes().get(attribute));
		}

		@Nonnull
		public BasicConstraint constraint(@Nonnull InstancedAttribute source) {
			return new BasicConstraint(this, source);
		}

		@Nonnull
		public BasicConstraint constraint(@Nonnull Constrainable source, @Nonnull Unit length) {
			return new BasicConstraint(this, source.getAttributes().get(attribute), length);
		}

		@Nonnull
		public BasicConstraint constraint(@Nonnull InstancedAttribute source, @Nonnull Unit length) {
			return new BasicConstraint(this, source, length);
		}

		@Nonnull
		public BasicConstraint constraint(@Nonnull Constrainable source, float ratio) {
			return new BasicConstraint(this, source.getAttributes().get(attribute), ratio);
		}

		@Nonnull
		public BasicConstraint constraint(@Nonnull InstancedAttribute source, float ratio) {
			return new BasicConstraint(this, source, ratio);
		}

		@Nonnull
		public BasicConstraint constraint(@Nonnull Constrainable source, @Nonnull Unit length, float ratio) {
			return new BasicConstraint(this, source.getAttributes().get(attribute), length, ratio);
		}

		@Nonnull
		public BasicConstraint constraint(@Nonnull InstancedAttribute source, @Nonnull Unit length, float ratio) {
			return new BasicConstraint(this, source, length, ratio);
		}

		@Nonnull
		public BasicConstraint constraint(@Nonnull Unit length) {
			return new BasicConstraint(this, length);
		}
	}

	public static final class InstancedAttributes {
		@Nonnull
		public final Constrainable item;

		@Nonnull
		public final InstancedAttribute left;

		@Nonnull
		public final InstancedAttribute right;

		@Nonnull
		public final InstancedAttribute top;

		@Nonnull
		public final InstancedAttribute bottom;

		@Nonnull
		public final InstancedAttribute width;

		@Nonnull
		public final InstancedAttribute height;

		@Nonnull
		public final InstancedAttribute centerX;

		@Nonnull
		public final InstancedAttribute centerY;

		public InstancedAttributes(@Nonnull Constrainable item) {
			this.item = item;
			left = new InstancedAttribute(item, Attribute.Left);
			right = new InstancedAttribute(item, Attribute.Right);
			top = new InstancedAttribute(item, Attribute.Top);
			bottom = new InstancedAttribute(item, Attribute.Bottom);
			width = new InstancedAttribute(item, Attribute.Width);
			height = new InstancedAttribute(item, Attribute.Height);
			centerX = new InstancedAttribute(item, Attribute.CenterX);
			centerY = new InstancedAttribute(item, Attribute.CenterY);
		}

		@Nonnull
		public InstancedAttribute get(@Nonnull Attribute attribute) {
			switch (attribute) {
				case Left:
					return left;
				case Right:
					return right;
				case Top:
					return top;
				case Bottom:
					return bottom;
				case Width:
					return width;
				case Height:
					return height;
				case CenterX:
					return centerX;
				case CenterY:
					return centerY;
				default:
					throw new IllegalArgumentException();
			}
		}
	}
}