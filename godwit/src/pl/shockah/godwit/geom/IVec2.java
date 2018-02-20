package pl.shockah.godwit.geom;

import javax.annotation.Nonnull;

import pl.shockah.godwit.fx.ease.Easable;

public abstract class IVec2<V extends IVec2<V>> implements Easable<V> {
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
		return Float.hashCode(x()) * 31 + Float.hashCode(y());
	}

	public abstract float getX();
	public abstract float getY();

	public final float x() {
		return getX();
	}

	public final float y() {
		return getY();
	}

	@Nonnull
	public abstract V getCopy();

	// java-oo is weird as hell, so there's a lot of overloads involved

	@Nonnull public abstract IVec2 add(float x, float y);
	@Nonnull public abstract IVec2 multiply(float x, float y);
	@Nonnull public abstract IVec2 withX(float x);
	@Nonnull public abstract IVec2 withY(float y);
	@Nonnull public abstract IVec2 getAbs();

	@Nonnull public IVec2 add(@Nonnull IVec2 v) {
		return add(v.x(), v.y());
	}

	@Nonnull public IVec2 add(@Nonnull Vec2 v) {
		return add((IVec2)v);
	}

	@Nonnull public IVec2 add(@Nonnull ImmutableVec2 v) {
		return add((IVec2)v);
	}

	@Nonnull public IVec2 add(float f) {
		float length = getLength();
		return this * (length + f) / length;
	}

	@Nonnull public IVec2 add(int f) {
		return add((float)f);
	}

	@Nonnull public IVec2 add(double f) {
		return add((float)f);
	}

	@Nonnull public IVec2 add(long f) {
		return add((float)f);
	}

	@Nonnull public IVec2 subtract(float x, float y) {
		return add(-x, -y);
	}

	@Nonnull public IVec2 subtract(@Nonnull IVec2 v) {
		return subtract(v.x(), v.y());
	}

	@Nonnull public IVec2 subtract(@Nonnull Vec2 v) {
		return subtract((IVec2)v);
	}

	@Nonnull public IVec2 subtract(@Nonnull ImmutableVec2 v) {
		return subtract((IVec2)v);
	}

	@Nonnull public IVec2 subtract(float f) {
		return add(-f);
	}

	@Nonnull public IVec2 subtract(int f) {
		return subtract((float)f);
	}

	@Nonnull public IVec2 subtract(double f) {
		return subtract((float)f);
	}

	@Nonnull public IVec2 subtract(long f) {
		return subtract((float)f);
	}

	@Nonnull public IVec2 multiply(@Nonnull IVec2 v) {
		return multiply(v.x(), v.y());
	}

	@Nonnull public IVec2 multiply(@Nonnull Vec2 v) {
		return multiply((IVec2)v);
	}

	@Nonnull public IVec2 multiply(@Nonnull ImmutableVec2 v) {
		return multiply((IVec2)v);
	}

	@Nonnull public IVec2 multiply(float f) {
		return multiply(f, f);
	}

	@Nonnull public IVec2 multiply(int f) {
		return multiply((float)f);
	}

	@Nonnull public IVec2 multiply(double f) {
		return multiply((float)f);
	}

	@Nonnull public IVec2 multiply(long f) {
		return multiply((float)f);
	}

	@Nonnull public IVec2 divide(float x, float y) {
		return multiply(1 / x, 1 / y);
	}

	@Nonnull public IVec2 divide(@Nonnull IVec2 v) {
		return divide(v.x(), v.y());
	}

	@Nonnull public IVec2 divide(@Nonnull Vec2 v) {
		return divide((IVec2)v);
	}

	@Nonnull public IVec2 divide(@Nonnull ImmutableVec2 v) {
		return divide((IVec2)v);
	}

	@Nonnull public IVec2 divide(float f) {
		return divide(f, f);
	}

	@Nonnull public IVec2 divide(int f) {
		return divide((float)f);
	}

	@Nonnull public IVec2 divide(double f) {
		return divide((float)f);
	}

	@Nonnull public IVec2 divide(long f) {
		return divide((float)f);
	}

	@Nonnull public IVec2 negate() {
		return multiply(-1f);
	}

	@Nonnull public IVec2 getOnlyX() {
		return multiply(1, 0);
	}

	@Nonnull public IVec2 getOnlyY() {
		return multiply(0, 1);
	}

	public float getLength() {
		return (float)Math.sqrt(x() * x() + y() * y());
	}

	public float getAngle() {
		return ImmutableVec2.zero.getAngle(this);
	}

	public float getAngle(@Nonnull IVec2 v) {
		return (float)Math.toDegrees(Math.atan2(y() - v.y(), v.x() - x()));
	}

	@Nonnull public IVec2 getNormalized() {
		float length = getLength();
		if (length == 1f)
			return this;
		return this * (1f / length);
	}

	@Nonnull public Vec2 getMutable() {
		return getMutableCopy();
	}

	@Nonnull public ImmutableVec2 getImmutable() {
		return getImmutableCopy();
	}

	@Nonnull public Vec2 getMutableCopy() {
		return new Vec2(x(), y());
	}

	@Nonnull public ImmutableVec2 getImmutableCopy() {
		return new ImmutableVec2(x(), y());
	}
}