package pl.shockah.godwit.geom;

import javax.annotation.Nonnull;

import pl.shockah.godwit.fx.ease.Easable;
import pl.shockah.godwit.fx.ease.Easing;

public abstract class IVec2 implements Easable<IVec2> {
	IVec2() {
	}

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof IVec2))
			return false;
		IVec2 v = (IVec2)o;
		return x() == v.x() && y() == v.y();
	}

	@Override
	public int hashCode() {
		return Float.floatToIntBits(x()) * 31 + Float.floatToIntBits(y());
	}

	public abstract float x();
	public abstract float y();

	@Nonnull public abstract IVec2 getCopy();

	@Nonnull public IVec2 add(float x, float y) {
		return x == 0 && y == 0 ? this : new Vec2(x() + x, y() + y);
	}

	@Nonnull public final IVec2 add(@Nonnull IVec2 v) {
		return add(v.x(), v.y());
	}

	@Nonnull public IVec2 subtract(float x, float y) {
		return x == 0 && y == 0 ? this : new Vec2(x() - x, y() - y);
	}

	@Nonnull public final IVec2 subtract(@Nonnull IVec2 v) {
		return subtract(v.x(), v.y());
	}

	@Nonnull public IVec2 multiply(float x, float y) {
		return x == 1 && y == 1 ? this : new Vec2(x() * x, y() * y);
	}

	@Nonnull public final IVec2 multiply(@Nonnull IVec2 v) {
		return multiply(v.x(), v.y());
	}

	@Nonnull public final IVec2 multiply(float f) {
		return multiply(f, f);
	}

	@Nonnull public IVec2 divide(float x, float y) {
		return x == 1 && y == 1 ? this : new Vec2(x() / x, y() / y);
	}

	@Nonnull public final IVec2 divide(@Nonnull IVec2 v) {
		return divide(v.x(), v.y());
	}

	@Nonnull public final IVec2 divide(float f) {
		return divide(f, f);
	}

	@Nonnull public IVec2 negate() {
		return x() == 0 && y() == 0 ? this : new Vec2(-x(), -y());
	}

	@Nonnull public IVec2 withX(float x) {
		return x == x() ? this : new Vec2(x, y());
	}

	@Nonnull public IVec2 withY(float y) {
		return y == y() ? this : new Vec2(x(), y);
	}

	@Nonnull public final IVec2 getAbs() {
		return new Vec2(Math.abs(x()), Math.abs(y()));
	}

	public final float getLength() {
		return (float)Math.sqrt(x() * x() + y() * y());
	}

	public final float getAngle() {
		return Vec2.zero.getAngle(this);
	}

	public final float getAngle(@Nonnull IVec2 v) {
		return (float)Math.toDegrees(Math.atan2(y() - v.y(), v.x() - x()));
	}

	@Nonnull public final IVec2 getNormalized() {
		float length = getLength();
		if (length == 0f || length == 1f)
			return this;
		else
			return this * (1f / length);
	}

	@Nonnull public final MutableVec2 getMutableCopy() {
		return new MutableVec2(x(), y());
	}

	@Nonnull public final Vec2 getImmutableCopy() {
		return new Vec2(x(), y());
	}

	@Override
	@Nonnull public final IVec2 ease(@Nonnull IVec2 other, float f) {
		return new Vec2(Easing.linear.ease(x(), other.x(), f), Easing.linear.ease(y(), other.y(), f));
	}

	// ----- java-oo weirdness fixes for IntelliJ

	@Nonnull public final IVec2 add(@Nonnull MutableVec2 v) {
		return add(v.x, v.y);
	}

	@Nonnull public final IVec2 add(@Nonnull Vec2 v) {
		return add(v.x, v.y);
	}

	@Nonnull public final IVec2 subtract(@Nonnull MutableVec2 v) {
		return subtract(v.x, v.y);
	}

	@Nonnull public final IVec2 subtract(@Nonnull Vec2 v) {
		return subtract(v.x, v.y);
	}

	@Nonnull public final IVec2 multiply(@Nonnull MutableVec2 v) {
		return multiply(v.x, v.y);
	}

	@Nonnull public final IVec2 multiply(@Nonnull Vec2 v) {
		return multiply(v.x, v.y);
	}

	@Nonnull public final IVec2 multiply(int f) {
		return multiply(f, f);
	}

	@Nonnull public final IVec2 divide(@Nonnull MutableVec2 v) {
		return divide(v.x, v.y);
	}

	@Nonnull public final IVec2 divide(@Nonnull Vec2 v) {
		return divide(v.x, v.y);
	}

	@Nonnull public final IVec2 divide(int f) {
		return divide(f, f);
	}
}