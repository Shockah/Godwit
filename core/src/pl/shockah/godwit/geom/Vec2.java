package pl.shockah.godwit.geom;

import com.badlogic.gdx.math.Vector2;

import javax.annotation.Nonnull;

import pl.shockah.unicorn.Math2;

public final class Vec2 extends IVec2 {
	@Nonnull public static final Vec2 zero = new Vec2();

	public final float x;
	public final float y;

	public Vec2() {
		this(0, 0);
	}

	public Vec2(float v) {
		this(v, v);
	}

	public Vec2(float x, float y) {
		this.x = x;
		this.y = y;
	}

	public Vec2(@Nonnull IVec2 v) {
		this(v.x(), v.y());
	}

	public Vec2(@Nonnull Vector2 vec) {
		this(vec.x, vec.y);
	}

	@Nonnull public static Vec2 angled(float dist, float angle) {
		return new Vec2(Math2.ldirX(dist, angle), Math2.ldirY(dist, angle));
	}

	@Override
	public float x() {
		return x;
	}

	@Override
	public float y() {
		return y;
	}

	@Override
	public String toString() {
		return String.format("[%.2f, %.2f]", x, y);
	}

	@Override
	@Nonnull public Vec2 getCopy() {
		return new Vec2(this);
	}

	@Override
	@Nonnull public Vec2 asImmutable() {
		return this;
	}
}