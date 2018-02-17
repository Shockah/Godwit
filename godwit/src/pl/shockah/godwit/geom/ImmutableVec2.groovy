package pl.shockah.godwit.geom

import com.badlogic.gdx.math.Vector2
import groovy.transform.CompileStatic
import groovy.transform.EqualsAndHashCode
import pl.shockah.godwit.Math2
import pl.shockah.godwit.animfx.ease.Easing

import javax.annotation.Nonnull

@CompileStatic
@EqualsAndHashCode
final class ImmutableVec2 extends IVec2<ImmutableVec2> {
	final float x
	final float y

	ImmutableVec2() {
		this(0, 0)
	}

	ImmutableVec2(float v) {
		this(v, v)
	}

	ImmutableVec2(float x, float y) {
		this.x = x
		this.y = y
	}

	ImmutableVec2(@Nonnull IVec2 v) {
		this(v.x, v.y)
	}

	ImmutableVec2(@Nonnull Vector2 vec) {
		this(vec.x, vec.y)
	}

	@Nonnull static ImmutableVec2 angled(float dist, float angle) {
		return new ImmutableVec2(Math2.ldirX(dist, angle), Math2.ldirY(dist, angle))
	}

	@Override
	String toString() {
		return String.format("[%.2f, %.2f]", x, y)
	}

	@Override
	@Nonnull ImmutableVec2 getCopy() {
		return new ImmutableVec2(this)
	}

	@Override
	@Nonnull ImmutableVec2 plus(float x, float y) {
		return new ImmutableVec2(this.x + x as float, this.y + y as float)
	}

	@Override
	@Nonnull ImmutableVec2 multiply(float x, float y) {
		return new ImmutableVec2(this.x * x as float, this.y * y as float)
	}

	@Override
	@Nonnull ImmutableVec2 withX(float x) {
		return new ImmutableVec2(x, y)
	}

	@Override
	@Nonnull ImmutableVec2 withY(float y) {
		return new ImmutableVec2(x, y)
	}

	@Override
	@Nonnull ImmutableVec2 getAbs() {
		return new ImmutableVec2(Math.abs(x), Math.abs(y))
	}

	@Override
	@Nonnull ImmutableVec2 ease(@Nonnull ImmutableVec2 other, float f) {
		return new ImmutableVec2(Easing.linear.ease(x, other.x, f), Easing.linear.ease(y, other.y, f))
	}

	@Override
	@Nonnull ImmutableVec2 getImmutable() {
		return this
	}
}