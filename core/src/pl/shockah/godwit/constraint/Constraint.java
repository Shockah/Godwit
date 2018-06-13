package pl.shockah.godwit.constraint;

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
}