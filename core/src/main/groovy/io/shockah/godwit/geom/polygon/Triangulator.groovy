package io.shockah.godwit.geom.polygon

import io.shockah.godwit.geom.Vec2

/**
 * Created by michaldolas on 04.11.16.
 */
interface Triangulator {
    int triangleCount()

    Vec2 trianglePoint(int tri, int i)

    void addPolyPoint(Vec2 point)

    boolean triangulate()
}