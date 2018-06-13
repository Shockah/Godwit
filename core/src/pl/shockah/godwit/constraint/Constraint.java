package pl.shockah.godwit.constraint;

import javax.annotation.Nonnull;

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

		public boolean isHorizontal() {
			return this == Left || this == Right || this == Width || this == CenterX;
		}

		public boolean isVertical() {
			return !isHorizontal();
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