package pl.shockah.godwit.geom;

import com.badlogic.gdx.math.Vector2;

import javax.annotation.Nonnull;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import pl.shockah.godwit.Math2;
import pl.shockah.godwit.fx.ease.Easing;

public final class ImmutableVec2 extends IVec2<ImmutableVec2> {
	@Getter
	public final float x;

	@Getter
	public final float y;

	public ImmutableVec2() {
		this(0, 0);
	}

	public ImmutableVec2(float v) {
		this(v, v);
	}

	public ImmutableVec2(float x, float y) {
		this.x = x;
		this.y = y;
	}

	public ImmutableVec2(@Nonnull IVec2 v) {
		this(v.x(), v.y());
	}

	public ImmutableVec2(@Nonnull Vector2 vec) {
		this(vec.x, vec.y);
	}

	@Nonnull public static ImmutableVec2 angled(float dist, float angle) {
		return new ImmutableVec2(Math2.ldirX(dist, angle), Math2.ldirY(dist, angle));
	}

	@Override
	public String toString() {
		return String.format("[%.2f, %.2f]", x, y);
	}

	@Override
	@Nonnull public ImmutableVec2 getCopy() {
		return new ImmutableVec2(this);
	}

	@Override
	@Nonnull public ImmutableVec2 add(float x, float y) {
		return new ImmutableVec2(this.x + x, this.y + y);
	}

	@Override
	@Nonnull public ImmutableVec2 multiply(float x, float y) {
		return new ImmutableVec2(this.x * x, this.y * y);
	}

	@Override
	@Nonnull public ImmutableVec2 withX(float x) {
		return new ImmutableVec2(x, y);
	}

	@Override
	@Nonnull public ImmutableVec2 withY(float y) {
		return new ImmutableVec2(x, y);
	}

	@Override
	@Nonnull public ImmutableVec2 getAbs() {
		return new ImmutableVec2(Math.abs(x), Math.abs(y));
	}

	@Override
	@Nonnull public ImmutableVec2 ease(@Nonnull ImmutableVec2 other, float f) {
		return new ImmutableVec2(Easing.linear.ease(x, other.x, f), Easing.linear.ease(y, other.y, f));
	}

	@Override
	@Nonnull public ImmutableVec2 getImmutable() {
		return this;
	}
}