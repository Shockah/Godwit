package pl.shockah.godwit.geom.polygon

import groovy.transform.CompileStatic

@CompileStatic
interface Polygonable {
    Polygon asPolygon()
}