package io.shockah.godwit.geom

import com.badlogic.gdx.math.Vector2
import groovy.transform.CompileStatic
import io.shockah.godwit.Math2

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

    static Vec2 angled(float dist, float angle) {
        new Vec2(Math2.ldirX(dist, angle), Math2.ldirY(dist, angle))
    }

    @Override
    boolean equals(Object obj) {
        if (!(obj instanceof Vec2))
            return false
        Vec2 v = obj as Vec2
        x == v.x && y == v.y
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
        plus(v.x, v.y)
    }

    Vec2 plus(float x, float y) {
        new Vec2(this.x + x as float, this.y + y as float)
    }

    Vec2 minus(Vec2 v) {
        minus(v.x, v.y)
    }

    Vec2 minus(float x, float y) {
        new Vec2(this.x - x as float, this.y - y as float)
    }

    Vec2 multiply(Vec2 v) {
        multiply(v.x, v.y)
    }

    Vec2 multiply(float f) {
        multiply(f, f)
    }

    Vec2 multiply(float x, float y) {
        new Vec2(this.x * x as float, this.y * y as float)
    }

    Vec2 div(Vec2 v) {
        div(v.x, v.y)
    }

    Vec2 div(float f) {
        div(f, f)
    }

    Vec2 div(float x, float y) {
        new Vec2(this.x / x as float, this.y / y as float)
    }

    Vec2 positive() {
        this
    }

    Vec2 negative() {
        new Vec2(-x, -y)
    }

    Vec2 x() {
        new Vec2(x, 0)
    }

    Vec2 y() {
        new Vec2(0, y)
    }

    float length() {
        Math.sqrt(x * x + y * y)
    }
}