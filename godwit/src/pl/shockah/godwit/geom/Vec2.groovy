package pl.shockah.godwit.geom

import com.badlogic.gdx.math.Vector2
import groovy.transform.CompileStatic
import groovy.transform.EqualsAndHashCode
import pl.shockah.godwit.Math2
import pl.shockah.godwit.animfx.ease.Easing

import javax.annotation.Nonnull

@CompileStatic
@EqualsAndHashCode
final class Vec2 extends IVec2<Vec2> {
	float x
	float y

	Vec2() {
		this(0, 0)
	}

	Vec2(float v) {
		this(v, v)
	}

	Vec2(float x, float y) {
		this.x = x
		this.y = y
	}

	Vec2(@Nonnull IVec2 v) {
		this(v.x, v.y)
	}

	Vec2(@Nonnull Vector2 vec) {
		this(vec.x, vec.y)
	}

	@Nonnull static Vec2 angled(float dist, float angle) {
		return new Vec2(Math2.ldirX(dist, angle), Math2.ldirY(dist, angle))
	}

	void set(IVec2 v) {
		x = v.x
		y = v.y
	}

	@Override
	String toString() {
		return String.format("[%.2f, %.2f]", x, y)
	}

	@Override
	@Nonnull Vec2 getCopy() {
		return new Vec2(this)
	}

	@Override
	@Nonnull Vec2 plus(float x, float y) {
		return new Vec2(this.x + x as float, this.y + y as float)
	}

	@Override
	@Nonnull Vec2 multiply(float x, float y) {
		return new Vec2(this.x * x as float, this.y * y as float)
	}

	@Override
	@Nonnull Vec2 withX(float x) {
		return new Vec2(x, y)
	}

	@Override
	@Nonnull Vec2 withY(float y) {
		return new Vec2(x, y)
	}

	@Override
	@Nonnull Vec2 getAbs() {
		return new Vec2(Math.abs(x), Math.abs(y))
	}

	@Override
	@Nonnull Vec2 ease(@Nonnull Vec2 other, float f) {
		return new Vec2(Easing.linear.ease(x, other.x, f), Easing.linear.ease(y, other.y, f))
	}

	@Override
	@Nonnull Vec2 getMutable() {
		return this
	}
}