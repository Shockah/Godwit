package pl.shockah.godwit.geom.polygon

import pl.shockah.godwit.geom.Vec2

interface Triangulator {
    int triangleCount()

    Vec2 trianglePoint(int tri, int i)

    void addPolyPoint(Vec2 point)

    boolean triangulate()
}