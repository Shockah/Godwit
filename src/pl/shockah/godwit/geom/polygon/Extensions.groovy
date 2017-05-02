package pl.shockah.godwit.geom.polygon

import groovy.transform.CompileStatic
import pl.shockah.godwit.geom.Vec2

@CompileStatic
class Extensions {
    static Triangulator leftShift(Triangulator self, Vec2 point) {
        self.addPolyPoint(point)
        return self
    }
}