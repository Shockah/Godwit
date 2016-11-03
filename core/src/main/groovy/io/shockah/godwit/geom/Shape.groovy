package io.shockah.godwit.geom

import groovy.transform.CompileStatic
import io.shockah.godwit.gl.Gfx

/**
 * Created by michaldolas on 03.11.16.
 */
@CompileStatic
abstract class Shape {
    abstract Shape copy()

    abstract Rectangle getBoundingBox()

    final void translate(Vec2 v) {
        translate(v.x, v.y)
    }

    abstract void translate(float x, float y)

    final void draw(Gfx gfx, boolean filled, Vec2 v) {
        draw(gfx, filled, v.x, v.y)
    }

    abstract void draw(Gfx gfx, boolean filled, float x, float y)

    final boolean contains(Vec2 v) {
        return contains(v.x, v.y)
    }

    abstract boolean contains(float x, float y)
}