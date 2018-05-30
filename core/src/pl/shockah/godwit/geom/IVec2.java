package pl.shockah.godwit.geom;

import javax.annotation.Nonnull;

import pl.shockah.unicorn.ease.Easable;
import pl.shockah.unicorn.ease.Easing;

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

	@Nonnull
	public abstract IVec2 getCopy();

	@Nonnull
	public Vec2 add(float x, float y) {
		return x == 0 && y == 0 ? asImmutable() : new Vec2(x() + x, y() + y);
	}

	@Nonnull
	public final Vec2 add(@Nonnull IVec2 v) {
		return add(v.x(), v.y());
	}

	@Nonnull
	public Vec2 subtract(float x, float y) {
		return x == 0 && y == 0 ? asImmutable() : new Vec2(x() - x, y() - y);
	}

	@Nonnull
	public final Vec2 subtract(@Nonnull IVec2 v) {
		return subtract(v.x(), v.y());
	}

	@Nonnull
	public Vec2 multiply(float x, float y) {
		return x == 1 && y == 1 ? asImmutable() : new Vec2(x() * x, y() * y);
	}

	@Nonnull
	public final Vec2 multiply(@Nonnull IVec2 v) {
		return multiply(v.x(), v.y());
	}

	@Nonnull
	public final Vec2 multiply(float f) {
		return multiply(f, f);
	}

	@Nonnull
	public Vec2 divide(float x, float y) {
		return x == 1 && y == 1 ? asImmutable() : new Vec2(x() / x, y() / y);
	}

	@Nonnull
	public final Vec2 divide(@Nonnull IVec2 v) {
		return divide(v.x(), v.y());
	}

	@Nonnull
	public final Vec2 divide(float f) {
		return divide(f, f);
	}

	@Nonnull
	public Vec2 negate() {
		return x() == 0 && y() == 0 ? asImmutable() : new Vec2(-x(), -y());
	}

	@Nonnull
	public Vec2 withX(float x) {
		return x == x() ? asImmutable() : new Vec2(x, y());
	}

	@Nonnull
	public Vec2 withY(float y) {
		return y == y() ? asImmutable() : new Vec2(x(), y);
	}

	@Nonnull
	public final Vec2 getAbs() {
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

	@Nonnull
	public final Vec2 getNormalized() {
		float length = getLength();
		if (length == 0f || length == 1f)
			return asImmutable();
		else
			return this.multiply(1f / length);
	}

	@Nonnull
	public final MutableVec2 mutableCopy() {
		return new MutableVec2(x(), y());
	}

	@Nonnull
	public Vec2 asImmutable() {
		return new Vec2(x(), y());
	}

	@Override
	@Nonnull
	public final Vec2 ease(@Nonnull IVec2 other, float f) {
		return new Vec2(Easing.linear.ease(x(), other.x(), f), Easing.linear.ease(y(), other.y(), f));
	}
}