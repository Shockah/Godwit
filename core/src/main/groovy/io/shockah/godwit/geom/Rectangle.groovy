package io.shockah.godwit.geom

import groovy.transform.CompileStatic

/**
 * Created by michaldolas on 03.11.16.
 */
@CompileStatic
class Rectangle extends Shape implements Polygonable {
    Vec2 pos
    Vec2 size

    static Rectangle centered(float x, float y, float w, float h) {
        new Rectangle(x - w * 0.5f as float, y - h * 0.5f as float, w, h)
    }

    static Rectangle centered(float x, float y, Vec2 size) {
        centered(x, y, size.x, size.y)
    }

    static Rectangle centered(Vec2 pos, float w, float h) {
        centered(pos.x, pos.y, w, h)
    }

    static Rectangle centered(Vec2 pos, Vec2 size) {
        centered(pos.x, pos.y, size.x, size.y)
    }

    static Rectangle centered(float x, float y, float l) {
        centered(x, y, l, l)
    }

    static Rectangle centered(Vec2 pos, float l) {
        centered(pos.x, pos.y, l, l)
    }

    static Rectangle centered(float w, float h) {
        centered(0, 0, w, h)
    }

    static Rectangle centered(Vec2 size) {
        centered(0, 0, size.x, size.y)
    }

    Rectangle(float x, float y, float w, float h) {
        pos = new Vec2(x, y)
        size = new Vec2(w, h)
    }

    Rectangle(float x, float y, Vec2 size) {
        this(x, y, size.x, size.y)
    }

    Rectangle(Vec2 pos, float w, float h) {
        this(pos.x, pos.y, w, h)
    }

    Rectangle(Vec2 pos, Vec2 size) {
        this(pos.x, pos.y, size.x, size.y)
    }

    Rectangle(float x, float y, float l) {
        this(x, y, l, l)
    }

    Rectangle(Vec2 pos, float l) {
        this(pos.x, pos.y, l, l)
    }

    Rectangle(float w, float h) {
        this(0, 0, w, h)
    }

    Rectangle(Vec2 size) {
        this(0, 0, size.x, size.y)
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
            return false;
        Rectangle rect = obj as Rectangle;
        pos == rect.pos && size == rect.size;
    }

    @Override
    String toString() {
        String.format("[Rectangle: @%s, %.2fx%.2f]", pos, size.x, size.y);
    }

    @Override
    Rectangle getBoundingBox() {
        copyRectangle()
    }

    Vec2 center() {
        pos + size * 0.5f
    }
}