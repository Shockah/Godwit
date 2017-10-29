package pl.shockah.godwit.geom

import com.badlogic.gdx.math.Vector2
import groovy.transform.CompileStatic
import groovy.transform.EqualsAndHashCode
import pl.shockah.godwit.Math2

import javax.annotation.Nonnull

@CompileStatic
@EqualsAndHashCode
final class Vec2 {
	float x
	float y

	Vec2() {
		this(0, 0)
	}

	Vec2(float x, float y) {
		this.x = x
		this.y = y
	}

	Vec2(@Nonnull Vec2 v) {
		this(v.x, v.y)
	}

	Vec2(@Nonnull Vector2 vec) {
		this(vec.x, vec.y)
	}

	@Nonnull static Vec2 angled(float dist, float angle) {
		return new Vec2(Math2.ldirX(dist, angle), Math2.ldirY(dist, angle))
	}

	@Override
	String toString() {
		return String.format("[%.2f, %.2f]", x, y)
	}

	Object asType(Class clazz) {
		if (clazz.isAssignableFrom(Vec2.class))
			return this
		if (clazz.isAssignableFrom(Vector2.class))
			return new Vector2(x, y)
		return null
	}

	float getAt(int index) {
		switch (index) {
			case 0:
				return x
			case 1:
				return y
			default:
				throw new IndexOutOfBoundsException()
		}
	}

	@Nonnull Vec2 plus(Vec2 v) {
		return plus(v.x, v.y)
	}

	@Nonnull Vec2 plus(float f) {
		return this * (length + f as float) / length
	}

	@Nonnull Vec2 plus(float x, float y) {
		return new Vec2(this.x + x as float, this.y + y as float)
	}

	@Nonnull Vec2 minus(Vec2 v) {
		return minus(v.x, v.y)
	}

	@Nonnull Vec2 minus(float f) {
		return this + (-f)
	}

	@Nonnull Vec2 minus(float x, float y) {
		return new Vec2(this.x - x as float, this.y - y as float)
	}

	@Nonnull Vec2 multiply(Vec2 v) {
		return multiply(v.x, v.y)
	}

	@Nonnull Vec2 multiply(float f) {
		return multiply(f, f)
	}

	@Nonnull Vec2 multiply(float x, float y) {
		return new Vec2(this.x * x as float, this.y * y as float)
	}

	@Nonnull Vec2 div(Vec2 v) {
		return div(v.x, v.y)
	}

	@Nonnull Vec2 div(float f) {
		return div(f, f)
	}

	@Nonnull Vec2 div(float x, float y) {
		return new Vec2(this.x / x as float, this.y / y as float)
	}

	@Nonnull Vec2 negative() {
		return new Vec2(-x, -y)
	}

	@Nonnull Vec2 withX(float x) {
		return new Vec2(x, y)
	}

	@Nonnull Vec2 withY(float y) {
		return new Vec2(x, y)
	}

	@Nonnull Vec2 getOnlyX() {
		return new Vec2(x, 0)
	}

	@Nonnull Vec2 getOnlyY() {
		return new Vec2(0, y)
	}

	float getLength() {
		return Math.sqrt(x * x + y * y)
	}

	float getAngle() {
		return new Vec2().getAngle(this)
	}

	float getAngle(@Nonnull Vec2 v) {
		return Math.toDegrees(Math.atan2(y - v.y, v.x - x))
	}

	@Nonnull Vec2 getNormalized() {
		float length = this.length
		if (length == 1f)
			return this
		return this * (1f / length as float)
	}

	@Nonnull Vec2 getAbs() {
		return new Vec2(Math.abs(x), Math.abs(y))
	}
}