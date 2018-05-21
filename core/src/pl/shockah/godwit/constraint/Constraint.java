package pl.shockah.godwit.constraint;

public abstract class Constraint {
	public abstract void apply();

	public enum Attribute {
		Left, Right, Top, Bottom, Width, Height, CenterX, CenterY
	}
}