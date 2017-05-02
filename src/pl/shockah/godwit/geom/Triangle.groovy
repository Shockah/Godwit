package pl.shockah.godwit.geom

import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import groovy.transform.CompileStatic
import pl.shockah.godwit.Math2
import pl.shockah.godwit.geom.polygon.Polygon
import pl.shockah.godwit.geom.polygon.Polygonable
import pl.shockah.godwit.gl.Gfx

@CompileStatic
class Triangle extends Shape implements Polygonable {
    Vec2 pos1
    Vec2 pos2
    Vec2 pos3

    Triangle(float x1, float y1, float x2, float y2, float x3, float y3) {
        this(new Vec2(x1, y1), new Vec2(x2, y2), new Vec2(x3, y3))
    }

    Triangle(float x1, float y1, Vec2 pos2, float x3, float y3) {
        this(new Vec2(x1, y1), pos2, new Vec2(x3, y3))
    }

    Triangle(Vec2 pos1, float x2, float y2, float x3, float y3) {
        this(pos1, new Vec2(x2, y2), new Vec2(x3, y3))
    }

    Triangle(float x1, float y1, float x2, float y2, Vec2 pos3) {
        this(new Vec2(x1, y1), new Vec2(x2, y2), pos3)
    }

    Triangle(float x1, float y1, Vec2 pos2, Vec2 pos3) {
        this(new Vec2(x1, y1), pos2, pos3)
    }

    Triangle(Vec2 pos1, float x2, float y2, Vec2 pos3) {
        this(pos1, new Vec2(x2, y2), pos3)
    }

    Triangle(Vec2 pos1, Vec2 pos2, Vec2 pos3) {
        this.pos1 = pos1
        this.pos2 = pos2
        this.pos3 = pos3
    }

    @Override
    Shape copy() {
	    return copyTriangle()
    }

    Triangle copyTriangle() {
	    return new Triangle(pos1, pos2, pos3)
    }

    @Override
    boolean equals(Object obj) {
        if (!(obj instanceof Triangle))
            return false
        Triangle triangle = obj as Triangle
	    return pos1 == triangle.pos1 && pos2 == triangle.pos2 && pos3 == triangle.pos3
    }

    @Override
    int hashCode() {
        return pos1.hashCode() * 31 ** 4 + pos2.hashCode() * 31 ** 2 + pos3.hashCode()
    }

    @Override
    String toString() {
	    return String.format("[Triangle: %s-%s-%s]", pos1, pos2, pos3)
    }

    @Override
    Rectangle getBoundingBox() {
        float minX = (float)Math2.min(pos1.x, pos2.x, pos3.x)
        float minY = (float)Math2.min(pos1.y, pos2.y, pos3.y)
        float maxX = (float)Math2.max(pos1.x, pos2.x, pos3.x)
        float maxY = (float)Math2.max(pos1.y, pos2.y, pos3.y)
	    return new Rectangle(minX, minY, maxX - minX as float, maxY - minY as float)
    }

    @Override
    void translate(float x, float y) {
        pos1 = pos1.plus(x, y)
        pos2 = pos2.plus(x, y)
        pos3 = pos3.plus(x, y)
    }

    @Override
    void draw(Gfx gfx, boolean filled, float x, float y) {
        gfx.prepareShapes(filled ? ShapeRenderer.ShapeType.Filled : ShapeRenderer.ShapeType.Line)
        gfx.shapes.triangle(x + pos1.x as float, y + pos1.y as float, x + pos2.x as float, y + pos2.y as float, x + pos3.x as float, y + pos3.y as float)
    }

    private float sign(float x1, float y1, float x2, float y2, float x3, float y3) {
	    return (x1 - x3) * (y2 - y3) - (x2 - x3) * (y1 - y3)
    }

    @Override
    boolean contains(float x, float y) {
        def b1 = sign(x, y, pos1.x, pos1.y, pos2.x, pos2.y) < 0f
        def b2 = sign(x, y, pos2.x, pos2.y, pos3.x, pos3.y) < 0f
        def b3 = sign(x, y, pos3.x, pos3.y, pos1.x, pos1.y) < 0f
	    return b1 == b2 && b2 == b3
    }

    @Override
    Polygon asPolygon() {
        def p = new Polygon()
        p << pos1
        p << pos2
        p << pos3
	    return p
    }
}