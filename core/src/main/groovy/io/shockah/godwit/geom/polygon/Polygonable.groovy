package io.shockah.godwit.geom.polygon

import groovy.transform.CompileStatic

/**
 * Created by michaldolas on 04.11.16.
 */
@CompileStatic
interface Polygonable {
    Polygon asPolygon()
}