package io.shockah.godwit.geom

import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import groovy.transform.CompileStatic
import io.shockah.godwit.geom.polygon.Polygon
import io.shockah.godwit.geom.polygon.Polygonable
import io.shockah.godwit.gl.Gfx

/**
 * Created by michaldolas on 03.11.16.
 */
@CompileStatic
class Rectangle extends Shape implements Polygonable {
    Vec2 pos
    Vec2 size

    static Rectangle centered(float x, float y, float w, float h) {
        centered(new Vec2(x, y), new Vec2(w, h))
    }

    static Rectangle centered(float x, float y, Vec2 size) {
        centered(new Vec2(x, y), size)
    }

    static Rectangle centered(Vec2 pos, float w, float h) {
        centered(pos, new Vec2(w, h))
    }

    static Rectangle centered(Vec2 pos, Vec2 size) {
        new Rectangle(pos - size * 0.5f, size)
    }

    static Rectangle centered(float x, float y, float l) {
        centered(new Vec2(x, y), new Vec2(l, l))
    }

    static Rectangle centered(Vec2 pos, float l) {
        centered(pos, new Vec2(l, l))
    }

    static Rectangle centered(float w, float h) {
        centered(Vec2.Zero, new Vec2(w, h))
    }

    static Rectangle centered(Vec2 size) {
        centered(Vec2.Zero, size)
    }

    Rectangle(float x, float y, float w, float h) {
        this(new Vec2(x, y), new Vec2(w, h))
    }

    Rectangle(float x, float y, Vec2 size) {
        this(new Vec2(x, y), size)
    }

    Rectangle(Vec2 pos, float w, float h) {
        this(pos, new Vec2(w, h))
    }

    Rectangle(Vec2 pos, Vec2 size) {
        this.pos = pos
        this.size = size
    }

    Rectangle(float x, float y, float l) {
        this(new Vec2(x, y), new Vec2(l, l))
    }

    Rectangle(Vec2 pos, float l) {
        this(pos, new Vec2(l, l))
    }

    Rectangle(float w, float h) {
        this(Vec2.Zero, new Vec2(w, h))
    }

    Rectangle(Vec2 size) {
        this(Vec2.Zero, size)
    }

    @Override
    Shape copy() {
        return copyRectangle()
    }

    Rectangle copyRectangle() {
        return new Rectangle(pos, size)
    }

    @Override
    boolean equals(Object obj) {
        if (!(obj instanceof Rectangle))
            return false
        Rectangle rect = obj as Rectangle
        pos == rect.pos && size == rect.size
    }

    @Override
    String toString() {
        String.format("[Rectangle: @%s, %.2fx%.2f]", pos, size.x, size.y)
    }

    @Override
    Rectangle getBoundingBox() {
        copyRectangle()
    }

    Vec2 center() {
        pos + size * 0.5f
    }

    @Override
    void translate(float x, float y) {
        pos = pos.plus(x, y)
    }

    @Override
    void draw(Gfx gfx, boolean filled, float x, float y) {
        gfx.prepareShapes(filled ? ShapeRenderer.ShapeType.Filled : ShapeRenderer.ShapeType.Line)
        gfx.shapes.rect(x + pos.x as float, y + pos.y as float, size.x, size.y)
    }

    @Override
    boolean contains(float x, float y) {
        return x >= pos.x && y >= pos.y && x < pos.x + size.x && y < pos.y + size.y
    }

    @Override
    Polygon asPolygon() {
        def p = new Polygon()
        p << pos
        p << pos + size.x()
        p << pos + size
        p << pos + size.y()
        p
    }
}