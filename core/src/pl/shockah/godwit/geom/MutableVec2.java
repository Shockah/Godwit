package pl.shockah.godwit.geom;

import com.badlogic.gdx.math.Vector2;

import javax.annotation.Nonnull;

import pl.shockah.unicorn.Math2;
import pl.shockah.unicorn.UnexpectedException;

public final class MutableVec2 extends IVec2 {
	public float x;
	public float y;

	public MutableVec2() {
		this(0, 0);
	}

	public MutableVec2(float v) {
		this(v, v);
	}

	public MutableVec2(float x, float y) {
		this.x = x;
		this.y = y;
		if (y < -250f && y != Float.NEGATIVE_INFINITY)
			throw new UnexpectedException("REEEEEEEEE");
	}

	public MutableVec2(@Nonnull IVec2 v) {
		this(v.x(), v.y());
	}

	public MutableVec2(@Nonnull Vector2 vec) {
		this(vec.x, vec.y);
	}

	@Nonnull
	public static MutableVec2 angled(float dist, float angle) {
		return new MutableVec2(Math2.ldirX(dist, angle), Math2.ldirY(dist, angle));
	}

	@Override
	public float x() {
		return x;
	}

	@Override
	public float y() {
		return y;
	}

	public void set(float x, float y) {
		this.x = x;
		this.y = y;
		if (y < -250f && y != Float.NEGATIVE_INFINITY)
			throw new UnexpectedException("REEEEEEEEE");
	}

	public void set(@Nonnull IVec2 v) {
		set(v.x(), v.y());
	}

	@Override
	@Nonnull
	public Vec2 add(float x, float y) {
		return new Vec2(x() + x, y() + y);
	}

	@Override
	@Nonnull
	public Vec2 subtract(float x, float y) {
		return new Vec2(x() - x, y() - y);
	}

	@Override
	@Nonnull
	public Vec2 multiply(float x, float y) {
		return new Vec2(x() * x, y() * y);
	}

	@Override
	@Nonnull
	public Vec2 divide(float x, float y) {
		return new Vec2(x() / x, y() / y);
	}

	@Override
	@Nonnull
	public Vec2 negate() {
		return new Vec2(-x(), -y());
	}

	@Override
	@Nonnull
	public Vec2 withX(float x) {
		return new Vec2(x, y());
	}

	@Override
	@Nonnull
	public Vec2 withY(float y) {
		return new Vec2(x(), y);
	}

	@Override
	public String toString() {
		return String.format("[%.2f, %.2f]", x, y);
	}

	@Override
	@Nonnull
	public MutableVec2 getCopy() {
		return new MutableVec2(this);
	}
}