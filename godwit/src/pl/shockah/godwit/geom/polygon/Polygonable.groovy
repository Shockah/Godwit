package pl.shockah.godwit.geom.polygon

import groovy.transform.CompileStatic

import javax.annotation.Nonnull

@CompileStatic
interface Polygonable {
	@Nonnull Polygon asPolygon()
}