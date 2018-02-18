package pl.shockah.godwit.geom;

import com.badlogic.gdx.math.Vector2;

import javax.annotation.Nonnull;

import lombok.Getter;
import lombok.Setter;
import pl.shockah.godwit.Math2;
import pl.shockah.godwit.fx.ease.Easing;

public final class Vec2 extends IVec2<Vec2> {
	@Getter @Setter
	public float x;

	@Getter @Setter
	public float y;

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

	public void set(IVec2 v) {
		x = v.x();
		y = v.y();
	}

	@Override
	public String toString() {
		return String.format("[%.2f, %.2f]", x, y);
	}

	@Override
	@Nonnull
	public Vec2 getCopy() {
		return new Vec2(this);
	}

	@Override
	@Nonnull public Vec2 add(float x, float y) {
		return new Vec2(this.x + x, this.y + y);
	}

	@Override
	@Nonnull public Vec2 multiply(float x, float y) {
		return new Vec2(this.x * x, this.y * y);
	}

	@Override
	@Nonnull public Vec2 withX(float x) {
		return new Vec2(x, y);
	}

	@Override
	@Nonnull public Vec2 withY(float y) {
		return new Vec2(x, y);
	}

	@Override
	@Nonnull public Vec2 getAbs() {
		return new Vec2(Math.abs(x), Math.abs(y));
	}

	@Override
	@Nonnull public Vec2 ease(@Nonnull Vec2 other, float f) {
		return new Vec2(Easing.linear.ease(x, other.x, f), Easing.linear.ease(y, other.y, f));
	}

	@Override
	@Nonnull public Vec2 getMutable() {
		return this;
	}
}