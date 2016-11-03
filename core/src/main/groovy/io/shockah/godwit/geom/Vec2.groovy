package io.shockah.godwit.geom

import com.badlogic.gdx.math.Vector2
import groovy.transform.CompileStatic

/**
 * Created by michaldolas on 03.11.16.
 */
@CompileStatic
final class Vec2 {
    static Vec2 Zero = new Vec2(0, 0)

    final float x
    final float y

    Vec2(float x, float y) {
        this.x = x
        this.y = y
    }

    Vec2(Vector2 vec) {
        this(vec.x, vec.y)
    }

    Object asType(Class clazz) {
        if (clazz == Vector2)
            return new Vector2(x, y)
        throw new ClassCastException()
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
        new Vec2(x + v.x, y + v.y)
    }

    Vec2 minus(Vec2 v) {
        new Vec2(x - v.x, y - v.y)
    }

    Vec2 multiply(Vec2 v) {
        new Vec2(x * v.x, y * v.y)
    }

    Vec2 multiply(float f) {
        new Vec2(x * f as float, y * f as float)
    }

    Vec2 div(Vec2 v) {
        new Vec2(x / v.x as float, y / v.y as float)
    }

    Vec2 div(float f) {
        new Vec2(x / f as float, y / f as float)
    }

    Vec2 positive() {
        this
    }

    Vec2 negative() {
        new Vec2(-x, -y)
    }
}