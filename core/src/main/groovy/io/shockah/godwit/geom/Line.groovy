package io.shockah.godwit.geom

import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import groovy.transform.CompileStatic
import io.shockah.godwit.gl.Gfx

/**
 * Created by michaldolas on 04.11.16.
 */
@CompileStatic
class Line extends Shape {
    Vec2 pos1
    Vec2 pos2

    Line(float x1, float y1, float x2, float y2) {
        this(new Vec2(x1, y1), new Vec2(x2, y2));
    }

    Line(Vec2 pos1, float x2, float y2) {
        this(pos1, new Vec2(x2, y2));
    }

    Line(float x1, float y1, Vec2 pos2) {
        this(new Vec2(x1, y1), pos2);
    }

    Line(Vec2 pos1, Vec2 pos2) {
        this.pos1 = pos1
        this.pos2 = pos2
    }

    @Override
    Shape copy() {
        copyLine()
    }

    Line copyLine() {
        new Line(pos1, pos2)
    }

    @Override
    boolean equals(Object obj) {
        if (!(obj instanceof Line))
            return false
        Line line = obj as Line
        pos1 == line.pos1 && pos2 == line.pos2
    }

    @Override
    String toString() {
        String.format("[Line: %s->%s]", pos1, pos2)
    }

    @Override
    Rectangle getBoundingBox() {
        float minX = Math.min(pos1.x, pos2.x)
        float minY = Math.min(pos1.y, pos2.y)
        float maxX = Math.max(pos1.x, pos2.x)
        float maxY = Math.max(pos1.y, pos2.y)
        new Rectangle(minX, minY, maxX - minX as float, maxY - minY as float)
    }

    @Override
    void translate(float x, float y) {
        pos1 = pos1.plus(x, y)
        pos2 = pos2.plus(x, y)
    }

    @Override
    void draw(Gfx gfx, boolean filled, float x, float y) {
        assert !filled
        gfx.prepareShapes(ShapeRenderer.ShapeType.Line)
        gfx.shapes.line(x + pos1.x as float ,y + pos1.y as float, x + pos2.x as float, y + pos2.y as float)
    }

    @Override
    boolean contains(float x, float y) {
        return false
    }
}