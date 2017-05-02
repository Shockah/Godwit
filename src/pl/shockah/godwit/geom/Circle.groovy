package pl.shockah.godwit.geom

import groovy.transform.CompileStatic
import pl.shockah.godwit.geom.polygon.Polygon
import pl.shockah.godwit.geom.polygon.Polygonable
import pl.shockah.godwit.gl.Gfx

@CompileStatic
class Circle extends Shape implements Polygonable {
    Vec2 pos
    float radius

    protected Vec2 lastPos = null
    protected int lastPrecision = -1
    protected Polygon lastPoly = null

    Circle(float x, float y, float radius) {
        this(new Vec2(x, y), radius)
    }

    Circle(float radius) {
        this(Vec2.Zero, radius)
    }

    Circle(Vec2 pos, float radius) {
        this.pos = pos
        this.radius = radius
    }

    @Override
    Shape copy() {
        return copyCircle()
    }

    Circle copyCircle() {
        return new Circle(pos, radius)
    }

    @Override
    boolean equals(Object obj) {
        if (!(obj instanceof Circle))
            return false
        Circle circle = obj as Circle
        return pos.equals(circle.pos) && radius == circle.radius
    }

    @Override
    int hashCode() {
        return pos.hashCode() * 31 + Float.hashCode(radius)
    }

    @Override
    String toString() {
        return String.format("[Circle: %s, radius %.2f]", pos, radius)
    }

    @Override
    Rectangle getBoundingBox() {
        return Rectangle.centered(pos, radius * 2f as float)
    }

    @Override
    void translate(float x, float y) {
        pos = pos.plus(x, y)
    }

    @Override
    void draw(Gfx gfx, boolean filled, float x, float y) {
        asPolygon().draw(gfx, filled, x, y)
    }

    @Override
    boolean contains(float x, float y) {
        return pos.minus(x, y).length() <= radius
    }

    @Override
    Polygon asPolygon() {
        return asPolygon(Math.ceil(Math.PI * radius * 0.5f) as int)
    }

    Polygon asPolygon(int precision) {
        if (lastPoly && lastPoly.pointCount == precision && lastPrecision == precision && lastPos == pos)
            return lastPoly

        Polygon p = new Polygon.NoHoles()
        for (int i in 0..<precision) {
            p << Vec2.angled(radius, 360f / precision * i as float) + pos
        }

        lastPos = pos
        lastPrecision = precision
        return lastPoly = p
    }
}