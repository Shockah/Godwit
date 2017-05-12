package pl.shockah.godwit.geom

import com.badlogic.gdx.math.Vector2
import groovy.transform.CompileStatic
import pl.shockah.godwit.Math2

@CompileStatic
final class Vec2 {
    public static Vec2 Zero = new Vec2()

    final float x
    final float y

    Vec2() {
        this(0, 0)
    }

    Vec2(float x, float y) {
        this.x = x
        this.y = y
    }

    Vec2(Vector2 vec) {
        this(vec.x, vec.y)
    }

    static Vec2 angled(float dist, float angle) {
        return new Vec2(Math2.ldirX(dist, angle), Math2.ldirY(dist, angle))
    }

    @Override
    boolean equals(Object obj) {
        if (!(obj instanceof Vec2))
            return false
        Vec2 v = obj as Vec2
        return x == v.x && y == v.y
    }

	@Override
	int hashCode() {
		return Float.hashCode(x) * 31 + Float.hashCode(y)
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

    Vec2 plus(Vec2 v) {
        return plus(v.x, v.y)
    }

    Vec2 plus(float x, float y) {
        return new Vec2(this.x + x as float, this.y + y as float)
    }

    Vec2 minus(Vec2 v) {
        return minus(v.x, v.y)
    }

    Vec2 minus(float x, float y) {
        return new Vec2(this.x - x as float, this.y - y as float)
    }

    Vec2 multiply(Vec2 v) {
        return multiply(v.x, v.y)
    }

    Vec2 multiply(float f) {
        return multiply(f, f)
    }

    Vec2 multiply(float x, float y) {
        return new Vec2(this.x * x as float, this.y * y as float)
    }

    Vec2 div(Vec2 v) {
        return div(v.x, v.y)
    }

    Vec2 div(float f) {
        return div(f, f)
    }

    Vec2 div(float x, float y) {
        return new Vec2(this.x / x as float, this.y / y as float)
    }

    Vec2 negative() {
        return new Vec2(-x, -y)
    }

    Vec2 x() {
        return new Vec2(x, 0)
    }

    Vec2 y() {
        return new Vec2(0, y)
    }

    float length() {
        return Math.sqrt(x * x + y * y)
    }

    Vec2 normalized() {
        float length = length()
        if (length == 1f)
            return this
        return this * (1f / length as float)
    }
}