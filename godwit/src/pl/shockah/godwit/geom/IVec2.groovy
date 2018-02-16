package pl.shockah.godwit.geom

import groovy.transform.CompileStatic
import pl.shockah.godwit.animfx.ease.Easable

import javax.annotation.Nonnull

@CompileStatic
abstract class IVec2<V extends IVec2<V>> implements Easable<V> {
	abstract float getX()
	abstract float getY()

	@Nonnull abstract V getCopy()

	@Nonnull abstract V plus(float x, float y)
	@Nonnull abstract V multiply(float x, float y)
	@Nonnull abstract V withX(float x)
	@Nonnull abstract V withY(float y)
	@Nonnull abstract V getAbs()

	@Nonnull V plus(@Nonnull IVec2 v) {
		return plus(v.x, v.y)
	}

	@Nonnull V plus(float f) {
		return multiply(length + f as float) / length
	}

	@Nonnull V minus(float x, float y) {
		return plus(-x, -y)
	}

	@Nonnull V minus(@Nonnull IVec2 v) {
		return minus(v.x, v.y)
	}

	@Nonnull V minus(float f) {
		return plus(-f)
	}

	@Nonnull V multiply(@Nonnull IVec2 v) {
		return multiply(v.x, v.y)
	}

	@Nonnull V multiply(float f) {
		return multiply(f, f)
	}

	@Nonnull V div(float x, float y) {
		return multiply(1 / x as float, 1 / y as float)
	}

	@Nonnull V div(@Nonnull IVec2 v) {
		return div(v.x, v.y)
	}

	@Nonnull V div(float f) {
		return div(f, f)
	}

	@Nonnull V negative() {
		return multiply(-1f)
	}

	@Nonnull V getOnlyX() {
		return multiply(1, 0)
	}

	@Nonnull V getOnlyY() {
		return multiply(0, 1)
	}

	float getLength() {
		return Math.sqrt(x * x + y * y)
	}

	float getAngle() {
		return new ImmutableVec2().getAngle(this)
	}

	float getAngle(@Nonnull IVec2 v) {
		return Math.toDegrees(Math.atan2(y - v.y, v.x - x))
	}

	@Nonnull IVec2 getNormalized() {
		float length = this.length
		if (length == 1f)
			return this
		return this * (1f / length as float)
	}

	@Nonnull Vec2 getMutable() {
		return getMutableCopy()
	}

	@Nonnull ImmutableVec2 getImmutable() {
		return getImmutableCopy()
	}

	@Nonnull Vec2 getMutableCopy() {
		return new Vec2(x, y)
	}

	@Nonnull ImmutableVec2 getImmutableCopy() {
		return new ImmutableVec2(x, y)
	}
}