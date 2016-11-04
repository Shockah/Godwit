package io.shockah.godwit.geom.polygon

import groovy.transform.CompileStatic
import io.shockah.godwit.geom.Vec2

/**
 * Created by michaldolas on 04.11.16.
 */
@CompileStatic
class Extensions {
    static Triangulator leftShift(Triangulator self, Vec2 point) {
        self.addPolyPoint(point)
        self
    }
}